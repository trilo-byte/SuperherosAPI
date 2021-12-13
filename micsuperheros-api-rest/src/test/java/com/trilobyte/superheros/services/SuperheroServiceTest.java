package com.trilobyte.superheros.services;

import com.trilobyte.superheros.mappers.SuperheroMapper;
import com.trilobyte.superheros.persistence.repository.SuperherosRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SuperheroServiceTest.class})
public class SuperheroServiceTest {

  @Mock private SuperherosRepository repository;

  @Spy
  private SuperheroMapper mapper;

  @InjectMocks private SuperheroService service = new SuperheroServiceImpl();



}
