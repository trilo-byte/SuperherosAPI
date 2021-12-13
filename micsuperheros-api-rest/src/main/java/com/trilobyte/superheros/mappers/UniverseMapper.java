package com.trilobyte.superheros.mappers;

import org.mapstruct.Mapper;

import com.trilobyte.superheros.dto.UniverseTypeDto;

@Mapper
public interface UniverseMapper {

  default UniverseTypeDto toUniverseType(final String universe) {
    if (universe == null) {
      return null;
    }

    for (final UniverseTypeDto type : UniverseTypeDto.values()) {
      if (universe.equalsIgnoreCase(type.getValue())) {
        return type;
      }
    }
    return UniverseTypeDto.OTHER;
  }
}
