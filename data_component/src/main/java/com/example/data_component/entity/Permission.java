package com.example.data_component.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "permission")
public class Permission extends BaseEntity {

  @Column(unique = true)
  private String permissionName;

  @ManyToMany(mappedBy = "permissions")
  private Set<Role> roles;

}