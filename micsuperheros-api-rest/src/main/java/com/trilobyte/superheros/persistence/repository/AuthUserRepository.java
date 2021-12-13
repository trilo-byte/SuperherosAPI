package com.trilobyte.superheros.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trilobyte.superheros.persistence.entities.AuthUserEntity;

public interface AuthUserRepository extends JpaRepository<AuthUserEntity, Long> {

  Optional<AuthUserEntity> findOneByEmail(String email);
}
