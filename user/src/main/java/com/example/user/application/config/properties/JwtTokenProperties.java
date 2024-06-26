package com.example.user.application.config.properties;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class JwtTokenProperties {
    private String issuer;
    private Integer ttlInSecond;
    private String type;
    private String secretKeyPath;
    private String privateKeyPath;
    private String publicKeyPath;
}
