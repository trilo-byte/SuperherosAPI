package com.trilobyte.superheros.persistence.entities;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "superhero")
@lombok.Data
public class SuperheroEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", length = 150, nullable = false)
  private String name;

  @Column(name = "universe", length = 50, nullable = false)
  private String universe;

  @ElementCollection
  @CollectionTable(name = "superhero_powers", joinColumns = @JoinColumn(name = "superhero_id"))
  @Column(name = "power")
  private List<String> superpowers;
}
