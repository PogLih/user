package com.example.data_component.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.sql.Timestamp;
import java.time.Instant;
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
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Timestamp createAt;

  @Column(name = "modified_at", columnDefinition = "TIMESTAMP")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Timestamp modifiedAt;

  @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Timestamp deletedAt;

  @PrePersist()
  public void handlePrePersist() {
    this.createAt = Timestamp.from(Instant.now());
  }

  @PreUpdate
  public void handlePreUpdate() {
    this.modifiedAt = Timestamp.from(Instant.now());
  }

}
