package com.trilobyte.superheros.persistence.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.StringUtils;

import com.trilobyte.superheros.persistence.entities.SuperheroEntity;
import com.trilobyte.superheros.persistence.entities.SuperheroEntity_;

public interface SuperherosRepository
    extends JpaRepository<SuperheroEntity, Long>, JpaSpecificationExecutor<SuperheroEntity> {

  public static Specification<SuperheroEntity> nameContains(final String name) {
    if (StringUtils.hasText(name)) {
      return (hero, cq, cb) ->
          cb.like(cb.lower(hero.get(SuperheroEntity_.NAME)), new StringBuilder("%").append(name.toLowerCase()).append("%").toString());
    }
    return null;
  }
}
