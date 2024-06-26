package com.example.user.common.entity;

import com.example.user.common.enums.TokenTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

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
