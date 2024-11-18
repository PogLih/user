package com.example.data_component.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@Table(name = "permission")
public class Permission extends BaseEntity {

  @Column(unique = true)
  private String name;
  private String description;

  @ManyToMany(mappedBy = "permissions", fetch = FetchType.EAGER)
  private Set<Role> roles;

  @Builder
  public Permission(String name, String description, Set<Role> roles) {
    this.name = name;
    this.description = description;
    this.roles = roles;
  }
}