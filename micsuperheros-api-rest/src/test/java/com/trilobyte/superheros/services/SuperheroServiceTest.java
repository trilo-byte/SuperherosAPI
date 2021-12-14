package com.trilobyte.superheros.services;

import com.trilobyte.superheros.dto.SuperheroDto;
import com.trilobyte.superheros.dto.SuperheroReqDto;
import com.trilobyte.superheros.dto.UniverseTypeDto;
import com.trilobyte.superheros.exceptions.ApplicationException;
import com.trilobyte.superheros.exceptions.HeroNotFoundException;
import com.trilobyte.superheros.mappers.SuperheroMapper;
import com.trilobyte.superheros.persistence.entities.SuperheroEntity;
import com.trilobyte.superheros.persistence.repository.SuperherosRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.TransactionException;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SuperheroServiceTest.class})
public class SuperheroServiceTest {

  @Mock
  private SuperherosRepository repository;

  @Spy
  private SuperheroMapper mapper;

  @InjectMocks
  private SuperheroService service = new SuperheroServiceImpl();

  @Test
  public void findAll_withName_returnsEmptyList_whenNoSuperheroAdded() {
    // Given
    final String superheroName = "anySuperheroName";
    given(repository.findAll(any(Specification.class))).willReturn(Collections.emptyList());

    // When
    final List<SuperheroDto> response = service.findAll(superheroName);

    // Then
    then(repository).should().findAll(any(Specification.class));
    then(mapper).should().toDto(anyList());
    assertThat(response).isNotNull();
    assertThat(response).hasSize(0);
  }

  @Test
  public void findAll_withName_returnsASuperhero_whenSuperheroMatches() {
    // Given
    final String name = "anyName";
    final String universe = "other";
    final SuperheroDto hero = new SuperheroDto();
    hero.setName(name);
    hero.setUniverse(UniverseTypeDto.fromValue(universe));
    hero.setSuperpowers(Collections.emptyList());
    final List<SuperheroDto> list = Arrays.asList(hero);
    given(repository.findAll(any(Specification.class))).willReturn(list);
    given(mapper.toDto(anyList())).willReturn(list);

    // When
    final List<SuperheroDto> response = service.findAll(name);

    // Then
    then(repository).should().findAll(any(Specification.class));
    then(mapper).should().toDto(anyList());
    assertThat(response).isNotNull();
    assertThat(response).hasSize(1);
    assertEquals(name, response.get(0).getName());
    assertEquals(universe, response.get(0).getUniverse().getValue());
  }

  @Test
  public void findAll_withName_returnsMultipleSuperhero_whenMultipleMatches() {
    // Given
    final String name = "anyName";
    final String universe = "other";
    final String name2 = "anyName2";
    final String universe2 = "other";
    final SuperheroDto hero = new SuperheroDto();
    hero.setName(name);
    hero.setUniverse(UniverseTypeDto.fromValue(universe));
    hero.setSuperpowers(Collections.emptyList());
    final SuperheroDto hero2 = new SuperheroDto();
    hero2.setName(name2);
    hero2.setUniverse(UniverseTypeDto.fromValue(universe2));
    hero2.setSuperpowers(Collections.emptyList());
    final List<SuperheroDto> list = Arrays.asList(hero, hero2);
    given(repository.findAll(any(Specification.class))).willReturn(list);
    given(mapper.toDto(anyList())).willReturn(list);

    // When
    final List<SuperheroDto> response = service.findAll(name);

    // Then
    then(repository).should().findAll(any(Specification.class));
    then(mapper).should(times(1)).toDto(anyList());
    assertThat(response).isNotNull();
    assertThat(response).hasSize(2);
    assertEquals(name, response.get(0).getName());
    assertEquals(universe, response.get(0).getUniverse().getValue());
    assertEquals(name2, response.get(1).getName());
    assertEquals(universe2, response.get(1).getUniverse().getValue());
  }

  @Test
  public void findById_returnsAHero_whenIdIsPresent() {
    // Given
    final String name = "anyName";
    final String universe = "other";
    final SuperheroDto hero = new SuperheroDto();
    hero.setName(name);
    hero.setUniverse(UniverseTypeDto.fromValue(universe));
    hero.setSuperpowers(Collections.emptyList());
    final SuperheroEntity heroEntity = new SuperheroEntity();
    heroEntity.setName(name);
    heroEntity.setUniverse(universe);
    given(repository.findById(anyLong())).willReturn(Optional.of(heroEntity));
    given(mapper.toDto(any(SuperheroEntity.class))).willReturn(hero);

    // When
    final SuperheroDto response = service.findById(1L);

    // Then
    then(repository).should().findById(anyLong());
    then(mapper).should().toDto(any(SuperheroEntity.class));
    assertThat(response).isNotNull();
    assertEquals(name, response.getName());
    assertEquals(universe, response.getUniverse().getValue());
  }

  @Test
  public void findById_returnsNoHero_whenIdNotPresent() {
    // Given
    given(repository.findById(anyLong())).willReturn(Optional.empty());

    // When
    final HeroNotFoundException thrown =
            Assertions.assertThrows(
                    HeroNotFoundException.class,
                    () -> {
                      service.findById(1L);
                    });

    // Then
    then(mapper).should(never()).toDto(anyList());
    Assertions.assertEquals("Id not found", thrown.getMessage());
  }

  @Test
  public void save_returnsSuperhero_whenSuperheroIsAdded() {
    // Given
    final String name = "anyName";
    final String universe = "other";
    final List<String> superpowers= Arrays.asList("Flight");
    final SuperheroEntity hero = new SuperheroEntity();
    hero.setName(name);
    hero.setUniverse(universe);
    hero.setSuperpowers(superpowers);
    final SuperheroDto heroDto = new SuperheroDto();
    heroDto.setName(name);
    heroDto.setUniverse(UniverseTypeDto.fromValue(universe));
    heroDto.setSuperpowers(superpowers);
    given(repository.save(any(SuperheroEntity.class))).willReturn(hero);
    given(mapper.toEntity(any(SuperheroDto.class))).willReturn(hero);
    given(mapper.toDto(any(SuperheroEntity.class))).willReturn(heroDto);
    given(mapper.toDto(any(SuperheroReqDto.class))).willReturn(heroDto);

    // When
    final SuperheroDto response = service.save(new SuperheroReqDto());

    // Then
    then(repository).should().save(any(SuperheroEntity.class));
    then(mapper).should(times(1)).toDto(any(SuperheroEntity.class));
    then(mapper).should().toEntity(any(SuperheroDto.class));
    then(mapper).should().toDto(any(SuperheroReqDto.class));
    assertThat(response).isNotNull();
    assertEquals(name, response.getName());
    assertEquals(universe, response.getUniverse().getValue());
    assertThat(!response.getSuperpowers().isEmpty());
  }

  @Test
  public void update_returnsSuperhero_whenExistingSuperhero() {
    // Given
    final Long superHeroId = 1L;
    final String superpower = "fly";
    final String name = "anyName";
    final String universe = "other";
    final List<String> superPowers = Arrays.asList(superpower);
    final SuperheroReqDto reqDto = new SuperheroReqDto();
    reqDto.setName(name);
    reqDto.setUniverse(UniverseTypeDto.fromValue(universe));
    reqDto.setSuperpowers(superPowers);
    final SuperheroDto dto = new SuperheroDto();
    dto.setName(name);
    dto.setUniverse(UniverseTypeDto.fromValue(universe));
    dto.setSuperpowers(superPowers);

    final SuperheroEntity entity = new SuperheroEntity();
    entity.setId(superHeroId);
    entity.setName(name);
    entity.setUniverse(universe);
    entity.setSuperpowers(superPowers);
    given(mapper.toEntity(any(SuperheroDto.class))).willReturn(entity);
    given(mapper.toDto(any(SuperheroEntity.class))).willReturn(dto);
    given(mapper.toDto(any(SuperheroReqDto.class))).willReturn(dto);
    given(repository.save(any(SuperheroEntity.class))).willReturn(entity);

    // When
    final SuperheroDto response = service.update(superHeroId, reqDto);

    // Then
    then(mapper).should().toEntity(any(SuperheroDto.class));
    then(mapper).should().toDto(any(SuperheroEntity.class));
    then(mapper).should().toDto(any(SuperheroReqDto.class));
    then(repository).should().save(any(SuperheroEntity.class));
    assertEquals(name, response.getName());
    assertEquals(universe, response.getUniverse().getValue());
    assertEquals(1, response.getSuperpowers().size());
    assertEquals(superpower, response.getSuperpowers().get(0));
  }

}
