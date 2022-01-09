package com.trilobyte.superheros.mappers;

import com.trilobyte.superheros.dto.SuperheroDto;
import com.trilobyte.superheros.dto.SuperheroReqDto;
import com.trilobyte.superheros.dto.UniverseTypeDto;
import com.trilobyte.superheros.persistence.entities.SuperheroEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SuperheroMapperTest.class})
class SuperheroMapperTest {

  @Mock private UniverseMapper universeMapper;

  @InjectMocks private SuperheroMapper mapper = new SuperheroMapperImpl();

  @Test
  void toEntity_returnsEntity_fromValidSuperheroDto() {
    // Given
    final String name = "anyName";
    final String universe = "other";
    final String superpower = "flight";
    final SuperheroDto dto = new SuperheroDto();
    dto.setName(name);
    dto.setUniverse(UniverseTypeDto.fromValue(universe));
    dto.setSuperpowers(Arrays.asList(superpower));

    // When
    final SuperheroEntity response = mapper.toEntity(dto);

    // Then
    assertThat(response).isNotNull();
    assertEquals(name, response.getName());
    assertEquals(universe, response.getUniverse());
    assertEquals(1, response.getSuperpowers().size());
    assertEquals(superpower, response.getSuperpowers().get(0));
  }

  @Test
  void toEntity_returnNull_fromNullSuperheroDto() {
    // Given

    // When
    final SuperheroEntity response = mapper.toEntity(null);

    // Then
    assertThat(response).isNull();
  }

  @Test
  void toDto_returnSuperheroDto_fromValidEntity() {
    // Given
    final String name = "anyName";
    final String universe = "other";
    final String superpower = "flight";
    final SuperheroEntity entity = new SuperheroEntity();
    entity.setName(name);
    entity.setUniverse(universe);
    entity.setSuperpowers(Arrays.asList(superpower));
    given(universeMapper.toUniverseType(anyString())).willReturn(UniverseTypeDto.OTHER);

    // When
    final SuperheroDto response = mapper.toDto(entity);

    // Then
    then(universeMapper).should().toUniverseType(anyString());
    assertThat(response).isNotNull();
    assertEquals(name, response.getName());
    assertEquals(universe, response.getUniverse().getValue());
    assertEquals(1, response.getSuperpowers().size());
    assertEquals(superpower, response.getSuperpowers().get(0));
  }

  @Test
  void toDto_returnSuperheroDto_fromNotValidEntity() {
    // Given
    final SuperheroEntity entity = new SuperheroEntity();
    given(universeMapper.toUniverseType(null)).willReturn(null);

    // When
    final SuperheroDto response = mapper.toDto(entity);

    // Then
    then(universeMapper).should().toUniverseType(null);
    assertThat(response).isNotNull();
    assertThat(response.getName()).isNull();
    assertThat(response.getUniverse()).isNull();
    assertThat(response.getSuperpowers()).isNotNull();
    assertEquals(0, response.getSuperpowers().size());
  }

  @Test
  void toDto_returnSuperheroDtoList_fromValidEntityList() {
    // Given
    final StringBuilder name = new StringBuilder("anyName");
    final StringBuilder name2 = name.append("2");
    final String universe = "other";
    final StringBuilder superpower = new StringBuilder("flight");
    final StringBuilder superpower2 = superpower.append("2");
    final SuperheroEntity entity = new SuperheroEntity();
    final SuperheroEntity entity2 = new SuperheroEntity();
    entity.setName(name.toString());
    entity2.setName(name2.toString());
    entity.setUniverse(universe);
    entity2.setUniverse(universe);
    entity.setSuperpowers(Arrays.asList(superpower.toString()));
    entity2.setSuperpowers(Arrays.asList(superpower2.toString()));
    given(universeMapper.toUniverseType(anyString())).willReturn(UniverseTypeDto.OTHER);

    // When
    final List<SuperheroDto> response = mapper.toDto(Arrays.asList(entity, entity2));

    // Then
    then(universeMapper).should(times(2)).toUniverseType(anyString());
    assertThat(response).isNotNull();
    assertEquals(2, response.size());
    assertEquals(name.toString(), response.get(0).getName());
    assertEquals(universe, response.get(0).getUniverse().getValue());
    assertEquals(1, response.get(0).getSuperpowers().size());
    assertEquals(superpower.toString(), response.get(0).getSuperpowers().get(0));
    assertEquals(name2.toString(), response.get(1).getName());
    assertEquals(universe, response.get(1).getUniverse().getValue());
    assertEquals(1, response.get(1).getSuperpowers().size());
    assertEquals(superpower2.toString(), response.get(1).getSuperpowers().get(0));
  }

  @Test
  void toDto_returnsDto_FromValidReqDto() {
    // Given
    final String name = "anyName";
    final String universe = "other";
    final String superpower = "flight";
    final SuperheroReqDto reqDto = new SuperheroReqDto();
    reqDto.setName(name);
    reqDto.setUniverse(UniverseTypeDto.fromValue(universe));
    reqDto.superpowers(Arrays.asList(superpower));
    given(universeMapper.toUniverseType(anyString())).willReturn(UniverseTypeDto.OTHER);

    // When
    final SuperheroDto response = mapper.toDto(reqDto);

    // Then
    assertThat(response).isNotNull();
    assertEquals(response.getName(), name);
    assertEquals(universe, response.getUniverse().getValue());
    assertEquals(1, response.getSuperpowers().size());
    assertEquals(superpower, response.getSuperpowers().get(0));
  }
}
