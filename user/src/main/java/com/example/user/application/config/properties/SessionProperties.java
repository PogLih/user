package com.example.user.application.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "security.session")
public class SessionProperties {

    private SessionCreationPolicy policy = SessionCreationPolicy.STATELESS;

}
