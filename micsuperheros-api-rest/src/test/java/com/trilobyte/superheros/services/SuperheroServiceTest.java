package com.trilobyte.superheros.services;

import com.trilobyte.superheros.dto.SuperheroDto;
import com.trilobyte.superheros.dto.UniverseTypeDto;
import com.trilobyte.superheros.mappers.SuperheroMapper;
import com.trilobyte.superheros.persistence.repository.SuperherosRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SuperheroServiceTest.class})
public class SuperheroServiceTest {

  @Mock private SuperherosRepository repository;

  @Spy
  private SuperheroMapper mapper;

  @InjectMocks private SuperheroService service = new SuperheroServiceImpl();

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
    assertEquals(response.get(0).getName(), name);
    assertEquals(response.get(0).getUniverse().getValue(), universe);
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
    assertEquals(response.get(0).getName(), name);
    assertEquals(response.get(0).getUniverse().getValue(), universe);
    assertEquals(response.get(1).getName(), name2);
    assertEquals(response.get(1).getUniverse().getValue(), universe2);
  }

}
