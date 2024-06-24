package com.example.user.common.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
@Data
@Entity
@Builder
@NoArgsConstructor
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    public Role(Long id, String name, Set<User> users, Set<Permission> permissions) {
        this.id = id;
        this.name = name;
        this.users = users;
        this.permissions = permissions;
    }
}