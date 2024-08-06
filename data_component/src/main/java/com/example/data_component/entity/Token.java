package com.example.data_component.entity;

import com.example.common_component.enums.TokenTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Entity
@Accessors(chain = true)
@Table(name = "TOKEN")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token extends BaseEntity {

  private String token;
  private TokenTypeEnum tokenType;


  @Column(name = "expired_at", nullable = false)
  private OffsetDateTime expiredAt;

  @Column(name = "used_at")
  private OffsetDateTime usedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;
}
