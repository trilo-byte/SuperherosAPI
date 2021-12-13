package com.trilobyte.superheros.services;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.trilobyte.superheros.dto.SuperheroDto;
import com.trilobyte.superheros.dto.SuperheroReqDto;

public interface SuperheroService {

  SuperheroDto findById(@NotNull Long id);

  List<SuperheroDto> findAll(String name);

  SuperheroDto update(@NotNull Long superheroId, @NotNull @Valid SuperheroReqDto dto);

  SuperheroDto save(@NotNull @Valid SuperheroReqDto dto);

  boolean delete(@NotNull Long id);
}
