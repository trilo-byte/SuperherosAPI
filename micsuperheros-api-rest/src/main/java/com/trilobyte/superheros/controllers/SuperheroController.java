package com.trilobyte.superheros.controllers;

import com.trilobyte.superheros.aspect.annotation.Monitor;
import com.trilobyte.superheros.dto.SuperheroDto;
import com.trilobyte.superheros.dto.SuperheroReqDto;
import com.trilobyte.superheros.security.annotation.PreAuthAdmin;
import com.trilobyte.superheros.security.annotation.PreAuthUser;
import com.trilobyte.superheros.services.SuperheroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@PreAuthUser
@Monitor
public class SuperheroController implements SuperherosApiDelegate {

  private final NativeWebRequest request;

  private final SuperheroService superheroSrv;

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return Optional.ofNullable(request);
  }

  @Override
  public ResponseEntity<SuperheroDto> getSuperHero(final Long superheroId) {
    return ResponseEntity.ok(superheroSrv.findById(superheroId));
  }

  @Override
  public ResponseEntity<List<SuperheroDto>> getSuperHeroByName(final String name) {
    return ResponseEntity.ok(superheroSrv.findAll(StringUtils.hasText(name) ? name : ""));
  }

  @Override
  @PreAuthAdmin
  public ResponseEntity<SuperheroDto> addSuperhero(final SuperheroReqDto superheroReqDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(superheroSrv.save(superheroReqDto));
  }

  @Override
  @PreAuthAdmin
  public ResponseEntity<Void> deleteSuperhero(final Long superheroId) {
    superheroSrv.delete(superheroId);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PreAuthAdmin
  public ResponseEntity<SuperheroDto> updateSuperHero(
      final Long superheroId, final SuperheroReqDto superheroReqDto) {
    return new ResponseEntity<SuperheroDto>(HttpStatus.NOT_IMPLEMENTED);
  }
}
