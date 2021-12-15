package com.trilobyte.superheros.mappers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.trilobyte.superheros.dto.UniverseTypeDto;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UniverseMapperTest.class})
class UniverseMapperTest {

  @InjectMocks private UniverseMapper mapper = new UniverseMapperImpl();

  @Test
  void toUniverseType_returnsUniverse_whenValidUniverseIsProvided() {
    // Given
    final String marvel = "marvel";

    // When
    final UniverseTypeDto response = mapper.toUniverseType(marvel);

    // Then
    assertThat(response).isNotNull();
    assertEquals(marvel, response.getValue());
  }

  @Test
  void toUniverseType_returnOther_WhenNotValidUniverseIsProvided() {
    // Given
    final String universe = "anyUniverse";
    final String other = "other";

    // When
    final UniverseTypeDto response = mapper.toUniverseType(universe);

    // Then
    assertThat(response).isNotNull();
    assertEquals(other, response.getValue());
  }

  @Test
  void toUniverseType_returnsNull_WhenNullUniverseIsProvided() {
    // When
    final UniverseTypeDto response = mapper.toUniverseType(null);

    // Then
    assertThat(response).isNull();
  }
}
