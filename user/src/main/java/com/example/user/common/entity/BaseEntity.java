package com.example.user.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = false, updatable =
            false)
    private LocalDateTime createAt;

    @Column(name = "modified_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @PrePersist()
    public void handlePrePersist() {
        this.createAt = LocalDateTime.now();
    }

    @PreUpdate
    public void handlePreUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

}
