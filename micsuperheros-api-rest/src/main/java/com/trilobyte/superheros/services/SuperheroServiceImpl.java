package com.trilobyte.superheros.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.trilobyte.superheros.dto.SuperheroDto;
import com.trilobyte.superheros.dto.SuperheroReqDto;
import com.trilobyte.superheros.exceptions.HeroNotFoundException;
import com.trilobyte.superheros.mappers.SuperheroMapper;
import com.trilobyte.superheros.persistence.entities.SuperheroEntity;
import com.trilobyte.superheros.persistence.repository.SuperherosRepository;

@Service
@Validated
@Transactional(readOnly = true)
public class SuperheroServiceImpl implements SuperheroService {

  private static final String SUPERHEROS_BY_ID = "heros_by_id";
  private static final String SUPERHEROS_BY_NAME = "heros_by_name";

  @Autowired private SuperherosRepository repository;

  @Autowired private SuperheroMapper mapper;

  @Override
  @Cacheable(value = SUPERHEROS_BY_ID, key = "#id")
  public SuperheroDto findById(final Long id) {

    final SuperheroEntity result =
        repository
            .findById(id)
            .orElseThrow(() -> new HeroNotFoundException("{error.hero.notFound}", id));
    return mapper.toDto(result);
  }

  @Override
  @Cacheable(value = SUPERHEROS_BY_NAME, key = "#name")
  public List<SuperheroDto> findAll(final String name) {
    final List<SuperheroEntity> result =
        repository.findAll(SuperherosRepository.nameContains(name));
    return mapper.toDto(result);
  }

  @Override
  @Transactional
  @CachePut(value = SUPERHEROS_BY_ID, key = "#result.id")
  @CacheEvict(value = SUPERHEROS_BY_NAME, allEntries = true)
  public SuperheroDto update(final Long superheroId, final SuperheroReqDto dto) {
    if (!repository.existsById(superheroId)) {
      throw new HeroNotFoundException("Id not found");
    }
    final SuperheroEntity entity = mapper.toEntity(mapper.toDto(dto));
    entity.setId(superheroId);
    final SuperheroEntity result = repository.save(entity);
    return mapper.toDto(result);
  }

  @Override
  @Transactional
  @CachePut(value = SUPERHEROS_BY_ID, key = "#result.id")
  @CacheEvict(value = SUPERHEROS_BY_NAME, allEntries = true)
  public SuperheroDto save(final SuperheroReqDto dto) {
    final SuperheroEntity entity = mapper.toEntity(mapper.toDto(dto));
    final SuperheroEntity result = repository.save(entity);
    return mapper.toDto(result);
  }

  @Override
  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = SUPERHEROS_BY_ID, key = "#id"),
        @CacheEvict(value = SUPERHEROS_BY_NAME, allEntries = true)
      })
  public void delete(final Long id) {
    final Optional<SuperheroEntity> entity = repository.findById(id);
    if (entity.isEmpty()) {
      throw new HeroNotFoundException("{error.hero.notFound}", id);
    }
    repository.delete(entity.get());
  }
}
