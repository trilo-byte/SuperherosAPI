package com.trilobyte.superheros.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheSpringConfig {

  public static final String SUPERHEROS_BY_ID = "heros_by_id";
  public static final String SUPERHEROS_BY_NAME = "heros_by_name";
}
