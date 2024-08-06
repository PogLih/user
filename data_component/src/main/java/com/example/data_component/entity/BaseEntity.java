package com.example.data_component.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

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
