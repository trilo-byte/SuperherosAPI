package com.trilobyte.superheros.api.rest.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilobyte.superheros.SuperherosApplication;
import com.trilobyte.superheros.dto.SuperheroReqDto;
import com.trilobyte.superheros.dto.UniverseTypeDto;
import com.trilobyte.superheros.persistence.entities.SuperheroEntity;
import com.trilobyte.superheros.persistence.repository.SuperherosRepository;

@ActiveProfiles("test")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = SuperherosApplication.class)
@AutoConfigureMockMvc
@WithMockUser(username = "admin@mock.es", password = "", roles = "ADMIN")
@Sql(
    scripts = {
      "/db/migration/V1.0.0__Heroes_Create.sql",
      "/db/migration/V1.1.0__Security_Create.sql"
    },
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class SuperheroControllerTestIT {

  @Autowired private MockMvc mvc;

  @Autowired private SuperherosRepository repository;

  @Test
  void givenAValidId_whenGetById_thenReturnsASuperhero() throws Exception {
    final long id = 1L;
    final String name = "superman";
    final String universe = "other";
    final String superpower = "Flight";
    createSuperHeroEntity(name, universe, superpower);

    mvc.perform(get("/superheros/" + id).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is(name)))
        .andExpect(jsonPath("$.universe", is(universe)))
        .andExpect(jsonPath("$.superpowers[0]", is(superpower)));
  }

  @Test
  void givenAnInValidId_whenGetById_thenReturnsA404() throws Exception {
    final long id = 1L;

    mvc.perform(get("/superheros/" + id).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void givenNoIdAndNoName_whenGet_thenReturnsASuperhero() throws Exception {
    final String name = "superman";
    final String universe = "other";
    final String superpower = "Flight";
    createSuperHeroEntity(name, universe, superpower);

    mvc.perform(get("/superheros/").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
        .andExpect(jsonPath("$[0].name", is(name)))
        .andExpect(jsonPath("$[0].universe", is(universe)))
        .andExpect(jsonPath("$[0].superpowers[0]", is(superpower)));
  }

  @Test
  void givenNoIdAndNoName_whenGet_thenReturnsMultipleSuperhero() throws Exception {
    final String name = "superman";
    final String name2 = "spiderman";
    final String universe = "other";
    final String superpower = "Flight";
    final String superpower2 = "Crawl";
    createSuperHeroEntity(name, universe, superpower);
    createSuperHeroEntity(name2, universe, superpower2);

    mvc.perform(get("/superheros/").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
        .andExpect(jsonPath("$[0].name", is(name)))
        .andExpect(jsonPath("$[0].universe", is(universe)))
        .andExpect(jsonPath("$[0].superpowers[0]", is(superpower)))
        .andExpect(jsonPath("$[1].name", is(name2)))
        .andExpect(jsonPath("$[1].superpowers[0]", is(superpower2)));
  }

  @Test
  void givenAValidName_whenGetByName_thenReturnsASuperhero() throws Exception {
    final String name = "superman";
    final String universe = "other";
    final String superpower = "Flight";
    createSuperHeroEntity(name, universe, superpower);

    mvc.perform(
            get("/superheros/").contentType(MediaType.APPLICATION_JSON).queryParam("name", name))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
        .andExpect(jsonPath("$[0].name", is(name)))
        .andExpect(jsonPath("$[0].universe", is(universe)))
        .andExpect(jsonPath("$[0].superpowers[0]", is(superpower)));
  }

  @Test
  void givenAValidName_whenGetByName_thenReturnsMultipleSuperhero() throws Exception {
    final String name = "superman";
    final String name2 = "spiderman";
    final String universe = "other";
    final String superpower = "Flight";
    final String superpower2 = "Crawl";
    final String queryParam = "man";
    createSuperHeroEntity(name, universe, superpower);
    createSuperHeroEntity(name2, universe, superpower2);

    mvc.perform(
            get("/superheros/")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("name", queryParam))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
        .andExpect(jsonPath("$[0].name", is(name)))
        .andExpect(jsonPath("$[0].universe", is(universe)))
        .andExpect(jsonPath("$[0].superpowers[0]", is(superpower)))
        .andExpect(jsonPath("$[1].name", is(name2)))
        .andExpect(jsonPath("$[1].superpowers[0]", is(superpower2)));
  }

  @Test
  void givenANoValidName_whenGetByName_thenReturnsNoSuperhero() throws Exception {
    final String name = "superman";
    final String name2 = "spiderman";
    final String universe = "other";
    final String superpower = "Flight";
    final String superpower2 = "Crawl";
    final String queryParam = "Batman";
    createSuperHeroEntity(name, universe, superpower);
    createSuperHeroEntity(name2, universe, superpower2);

    mvc.perform(
            get("/superheros/")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("name", queryParam))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(equalTo(0))));
  }

  @Test
  void givenAValidSuperhero_whenAddSuperhero_thenReturnsASuperhero() throws Exception {
    final String name = "superman";
    final String universe = "other";
    final String superpower = "Flight";
    final SuperheroReqDto reqDto = createSuperHeroReqDto(name, universe, superpower);
    final ObjectMapper mapper = new ObjectMapper();
    final String reqDtoJson = mapper.writeValueAsString(reqDto);

    mvc.perform(post("/superheros").contentType(MediaType.APPLICATION_JSON).content(reqDtoJson))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is(name)))
        .andExpect(jsonPath("$.universe", is(universe)))
        .andExpect(jsonPath("$.superpowers[0]", is(superpower)));
  }

  @Test
  void givenANoValidSuperhero_whenAddSuperhero_thenReturnsBadRequest() throws Exception {
    final String name = "superman";
    final String universe = "other";
    final String superpower = "Flight";
    final SuperheroReqDto reqDto = createSuperHeroReqDto(name, universe, superpower);
    final ObjectMapper mapper = new ObjectMapper();
    final String reqDtoJson = mapper.writeValueAsString(reqDto).replace(universe, "anyUniverse");

    mvc.perform(post("/superheros").contentType(MediaType.APPLICATION_JSON).content(reqDtoJson))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void givenAValidId_whenDeleteSuperhero_returnsVoid() throws Exception {
    final long id = 1L;
    final String name = "superman";
    final String universe = "other";
    final String superpower = "Flight";
    createSuperHeroEntity(name, universe, superpower);

    mvc.perform(delete("/superheros/" + id).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNoContent());
  }

  @Test
  void givenANotExistId_whenDeleteSuperhero_returnsVoid() throws Exception {
    mvc.perform(delete("/superheros/999").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void givenANonValidId_whenDeleteSuperhero_returnsBadRequest() throws Exception {
    mvc.perform(delete("/superheros/ssss").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void givenAValidSuperhero_whenUpdateSuperhero_thenReturnsASuperhero() throws Exception {
    final String name = "superman";
    final String universe = "other";
    final String universe2 = "marvel";
    final String superpower = "Flight";

    createSuperHeroEntity(name, universe, superpower);
    final SuperheroReqDto reqDto = createSuperHeroReqDto(name.concat("2"), universe2, superpower);
    final ObjectMapper mapper = new ObjectMapper();
    final String reqDtoJson = mapper.writeValueAsString(reqDto);

    mvc.perform(put("/superheros/1").contentType(MediaType.APPLICATION_JSON).content(reqDtoJson))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is(name.concat("2"))))
        .andExpect(jsonPath("$.universe", is(universe2)))
        .andExpect(jsonPath("$.superpowers[0]", is(superpower)));
  }

  @Test
  void givenANonValidSuperhero_whenUpdateSuperhero_thenReturnsNotFound() throws Exception {
    final String name = "superman";
    final String universe = "other";
    final String superpower = "Flight";

    final SuperheroReqDto reqDto = createSuperHeroReqDto(name, universe, superpower);
    final ObjectMapper mapper = new ObjectMapper();
    final String reqDtoJson = mapper.writeValueAsString(reqDto);

    mvc.perform(put("/superheros/1").contentType(MediaType.APPLICATION_JSON).content(reqDtoJson))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void givenANonPath_returnsMethodNotAllowed() throws Exception {
    mvc.perform(delete("/superheros/").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isMethodNotAllowed());
  }

  private void createSuperHeroEntity(
      final String name, final String universe, final String superpower) {

    final SuperheroEntity superheroEntity = new SuperheroEntity();
    superheroEntity.setName(name);
    superheroEntity.setUniverse(universe);
    superheroEntity.setSuperpowers(Arrays.asList(superpower));

    repository.save(superheroEntity);
  }

  private SuperheroReqDto createSuperHeroReqDto(
      final String name, final String universe, final String superpower) {

    final SuperheroReqDto superheroReqDto = new SuperheroReqDto();
    superheroReqDto.setName(name);
    superheroReqDto.setUniverse(UniverseTypeDto.fromValue(universe));
    superheroReqDto.setSuperpowers(Arrays.asList(superpower));

    return superheroReqDto;
  }
}
