package com.example.user.common.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@NoArgsConstructor
@Table(name = "role")
public class Role extends BaseEntity {

    @Column(unique = true)
    private String name;
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;

    @Builder
    public Role(String name, Set<User> users, Set<Permission> permissions) {
        this.name = name;
        this.users = users;
        this.permissions = permissions;
    }
}