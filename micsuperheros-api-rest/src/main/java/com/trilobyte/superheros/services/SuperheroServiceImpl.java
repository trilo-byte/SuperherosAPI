package com.trilobyte.superheros.services;

import com.trilobyte.superheros.config.CacheSpringConfig;
import com.trilobyte.superheros.dto.SuperheroDto;
import com.trilobyte.superheros.dto.SuperheroReqDto;
import com.trilobyte.superheros.exceptions.HeroNotFoundException;
import com.trilobyte.superheros.mappers.SuperheroMapper;
import com.trilobyte.superheros.persistence.entities.SuperheroEntity;
import com.trilobyte.superheros.persistence.repository.SuperherosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
@Transactional(readOnly = true)
public class SuperheroServiceImpl implements SuperheroService {

  @Autowired private SuperherosRepository repository;

  @Autowired private SuperheroMapper mapper;

  @Override
  @Cacheable(value = CacheSpringConfig.SUPERHEROS_BY_ID, key = "#id")
  public SuperheroDto findById(final Long id) {

    final SuperheroEntity result =
            repository.findById(id).orElseThrow(() -> new HeroNotFoundException("Id not found"));
    return mapper.toDto(result);
  }

  @Override
  @Cacheable(value = CacheSpringConfig.SUPERHEROS_BY_NAME, key = "#name")
  public List<SuperheroDto> findAll(final String name) {
    final List<SuperheroEntity> result =
            repository.findAll(SuperherosRepository.nameContains(name));
    return mapper.toDto(result);
  }

  @Override
  @Transactional
  @CachePut(value = CacheSpringConfig.SUPERHEROS_BY_ID, key = "#result.id")
  @CacheEvict(value = CacheSpringConfig.SUPERHEROS_BY_NAME, allEntries = true)
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
  @CachePut(value = CacheSpringConfig.SUPERHEROS_BY_ID, key = "#result.id")
  @CacheEvict(value = CacheSpringConfig.SUPERHEROS_BY_NAME, allEntries = true)
  public SuperheroDto save(final SuperheroReqDto dto) {
    final SuperheroEntity entity = mapper.toEntity(mapper.toDto(dto));
    final SuperheroEntity result = repository.save(entity);
    return mapper.toDto(result);
  }

  @Override
  @Transactional
  @Caching(
      evict = {
        @CacheEvict(
            value = CacheSpringConfig.SUPERHEROS_BY_ID,
            key = "#id",
            condition = "#result == true"),
        @CacheEvict(
            value = CacheSpringConfig.SUPERHEROS_BY_NAME,
            allEntries = true,
            condition = "#result == true")
      })
  public boolean delete(final Long id) {
    final Optional<SuperheroEntity> entity = repository.findById(id);
    if (entity.isEmpty()) {
      return false;
    }
    repository.delete(entity.get());
    return true;
  }
}
