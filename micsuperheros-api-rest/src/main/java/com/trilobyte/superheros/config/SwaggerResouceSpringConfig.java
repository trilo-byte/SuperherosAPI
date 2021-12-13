package com.trilobyte.superheros.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.trilobyte.superheros.config.annotation.ProfileNotProduction;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Configuration
@ProfileNotProduction
public class SwaggerResouceSpringConfig {

  @Primary
  @Bean
  public SwaggerResourcesProvider swaggerResourcesProvider(
      final InMemorySwaggerResourcesProvider defaultResourcesProvider) {
    return () -> {
      final var wsResource = new SwaggerResource();
      wsResource.setSwaggerVersion(DocumentationType.OAS_30.getVersion());
      wsResource.setName("Publish");
      // must be in src/main/resources/static
      wsResource.setUrl("/superhero-rest.yml");

      final List<SwaggerResource> resources = new ArrayList<>(defaultResourcesProvider.get());
      resources.add(wsResource);
      return resources;
    };
  }
}
