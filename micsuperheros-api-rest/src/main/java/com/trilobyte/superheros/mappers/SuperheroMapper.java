package com.trilobyte.superheros.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.trilobyte.superheros.dto.SuperheroDto;
import com.trilobyte.superheros.dto.SuperheroReqDto;
import com.trilobyte.superheros.persistence.entities.SuperheroEntity;

@Mapper(uses = UniverseMapper.class)
public interface SuperheroMapper {

  @Mapping(target = "universe", source = "universe.value")
  SuperheroEntity toEntity(SuperheroDto dto);

  SuperheroDto toDto(SuperheroEntity superhero);

  List<SuperheroDto> toDto(List<SuperheroEntity> superhero);

  @Mapping(target = "id", ignore = true)
  SuperheroDto toDto(SuperheroReqDto request);
}
