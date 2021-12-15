package com.trilobyte.superheros.api.rest.controllers;

import com.trilobyte.superheros.SuperherosApplication;
import com.trilobyte.superheros.persistence.entities.SuperheroEntity;
import com.trilobyte.superheros.persistence.repository.SuperherosRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SuperherosApplication.class)
@AutoConfigureMockMvc
@WithMockUser(username = "admin@mock.es", password = "", roles = "ADMIN")
@ActiveProfiles("test")
public class SuperheroControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SuperherosRepository repository;

    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }

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

    private void createSuperHeroEntity(String name, String universe, String superpower) {

        SuperheroEntity superheroEntity = new SuperheroEntity();
        superheroEntity.setName(name);
        superheroEntity.setUniverse(universe);
        superheroEntity.setSuperpowers(Arrays.asList(superpower));

        repository.save(superheroEntity);
    }

}
