package com.example.user.application.config.properties;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class RefreshTokenProperties {
    private String issuer;
    private Integer ttlInSecond;
}
