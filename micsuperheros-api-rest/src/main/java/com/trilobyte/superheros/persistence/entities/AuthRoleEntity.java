package com.trilobyte.superheros.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "auth_role")
@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class AuthRoleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "role", length = 20, nullable = false)
  private String role;
}
