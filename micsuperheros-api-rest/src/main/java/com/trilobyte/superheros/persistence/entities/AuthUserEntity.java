package com.trilobyte.superheros.persistence.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "auth_user")
@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class AuthUserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name", length = 100, nullable = false)
  private String firstName;

  @Column(name = "last_name", length = 200, nullable = false)
  private String lastName;

  @Column(name = "email", length = 70, nullable = false, unique = true)
  private String email;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "auth_user_roles",
      joinColumns = @JoinColumn(name = "id_user"),
      inverseJoinColumns = @JoinColumn(name = "id_role"))
  Set<AuthRoleEntity> roles;
}
