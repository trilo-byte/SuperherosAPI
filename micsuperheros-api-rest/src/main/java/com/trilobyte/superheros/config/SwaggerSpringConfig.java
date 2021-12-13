package com.trilobyte.superheros.config;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import com.trilobyte.superheros.config.annotation.ProfileNotProduction;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@ProfileNotProduction
public class SwaggerSpringConfig {

  @Value("${spring.application.name:app not defined}")
  protected String appName;

  @Value("${spring.application.version:version not defined}")
  protected String appVersion;

  @Bean
  public Docket apiCurrent() {
    return createDefDocket("com.trilobyte.superheros.controllers", "Current");
  }

  protected Docket createDefDocket(final String groupPackage, final String groupName) {
    final var docket =
        new Docket(DocumentationType.OAS_30)
            .forCodeGeneration(true)
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage(groupPackage))
            .paths(PathSelectors.any())
            .build()
            .securitySchemes(securitySchemes())
            .genericModelSubstitutes(ResponseEntity.class)
            .apiInfo(apiInfo())
            .pathMapping("/");
    if (StringUtils.hasText(groupName)) {
      docket.groupName(groupName);
    }
    return docket;
  }

  protected ApiInfo apiInfo() {
    return new ApiInfo(
        appName, getAppDescription(), appVersion, "", null, null, null, Collections.emptyList());
  }

  protected List<SecurityScheme> securitySchemes() {
    return Collections.singletonList(
        HttpAuthenticationScheme.BASIC_AUTH_BUILDER.name("Authorization").build());
  }

  private String getAppDescription() {
    return appName;
  }
}
