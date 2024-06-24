package com.example.user.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Entity
@Accessors(chain = true)
@Table(name = "TOKEN")
@Data
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;

    @Column(name = "expired_at", nullable = false)
    private OffsetDateTime expiredAt;

    @Column(name = "used_at")
    private OffsetDateTime usedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
