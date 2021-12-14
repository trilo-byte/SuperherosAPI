package com.trilobyte.superheros.mappers;

import com.trilobyte.superheros.dto.UniverseTypeDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UniverseMapperTest.class})
public class UniverseMapperTest {

    @InjectMocks
    private UniverseMapper mapper = new UniverseMapperImpl();

    @Test
    public void toUniverseType_returnsUniverse_whenValidUniverseIsProvided() {
        // Given
        final String marvel = "marvel";

        // When
        UniverseTypeDto response = mapper.toUniverseType(marvel);

        // Then
        assertThat(response).isNotNull();
        assertEquals(marvel, response.getValue());
    }
    
    @Test
    public void toUniverseType_returnOther_WhenNotValidUniverseIsProvided() {
        // Given
        final String universe = "anyUniverse";
        final String other = "other";

        // When
        UniverseTypeDto response = mapper.toUniverseType(universe);

        // Then
        assertThat(response).isNotNull();
        assertEquals(other, response.getValue());
    }

    @Test
    public void toUniverseType_returnsNull_WhenNullUniverseIsProvided() {
        // Given
        final String other = "other";

        // When
        UniverseTypeDto response = mapper.toUniverseType(null);

        // Then
        assertThat(response).isNull();
    }
}
