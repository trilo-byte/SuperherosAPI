package com.trilobyte.superheros.api.rest.controllers;

import com.trilobyte.superheros.SuperherosApplication;
import com.trilobyte.superheros.persistence.entities.SuperheroEntity;
import com.trilobyte.superheros.persistence.repository.SuperherosRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SuperherosApplication.class)
@AutoConfigureMockMvc
@WithMockUser(username = "admin@mock.es", password = "", roles = "ADMIN")
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SuperheroControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SuperherosRepository repository;

    @Test
    public void givenAValidId_whenGetById_thenReturnsASuperhero() throws Exception {
        final long id = 1l;
        final String name = "superman";
        final String universe = "other";
        final String superpower = "Flight";
        createSuperHeroEntity(name, universe, superpower);

        mvc.perform(get("/superheros/"+id).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.universe", is(universe)))
                .andExpect(jsonPath("$.superpowers[0]", is(superpower)));
    }

    @Test
    public void givenAnInValidId_whenGetById_thenReturnsA404() throws Exception {
        final long id = 1l;

        mvc.perform(get("/superheros/"+id).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenNoIdAndNoName_whenGet_thenReturnsASuperhero() throws Exception {
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
    public void givenNoIdAndNoName_whenGet_thenReturnsMultipleSuperhero() throws Exception {
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
    public void givenAValidName_whenGetByName_thenReturnsASuperhero() throws Exception {
        final String name = "superman";
        final String universe = "other";
        final String superpower = "Flight";
        createSuperHeroEntity(name, universe, superpower);

        mvc.perform(get("/superheros/").contentType(MediaType.APPLICATION_JSON)
                        .queryParam("name", name))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name", is(name)))
                .andExpect(jsonPath("$[0].universe", is(universe)))
                .andExpect(jsonPath("$[0].superpowers[0]", is(superpower)));
    }

    @Test
    public void givenAValidName_whenGetByName_thenReturnsMultipleSuperhero() throws Exception {
        final String name = "superman";
        final String name2 = "spiderman";
        final String universe = "other";
        final String superpower = "Flight";
        final String superpower2 = "Crawl";
        final String queryParam = "man";
        createSuperHeroEntity(name, universe, superpower);
        createSuperHeroEntity(name2, universe, superpower2);

        mvc.perform(get("/superheros/").contentType(MediaType.APPLICATION_JSON)
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
    public void givenANoValidName_whenGetByName_thenReturnsNoSuperhero() throws Exception {
        final String name = "superman";
        final String name2 = "spiderman";
        final String universe = "other";
        final String superpower = "Flight";
        final String superpower2 = "Crawl";
        final String queryParam = "Batman";
        createSuperHeroEntity(name, universe, superpower);
        createSuperHeroEntity(name2, universe, superpower2);

        mvc.perform(get("/superheros/").contentType(MediaType.APPLICATION_JSON)
                        .queryParam("name", queryParam))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(equalTo(0))));
    }

    private void createSuperHeroEntity(String name, String universe, String superpower) {

        SuperheroEntity superheroEntity = new SuperheroEntity();
        superheroEntity.setName(name);
        superheroEntity.setUniverse(universe);
        superheroEntity.setSuperpowers(Arrays.asList(superpower));

        repository.save(superheroEntity);
    }



}
