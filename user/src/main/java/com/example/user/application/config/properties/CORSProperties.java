package com.example.user.application.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Data
@Component
@ConfigurationProperties(prefix = "security.cors")
public class CORSProperties {

    private boolean enabled = true;
    private Long maxAge = 36400L;
    private Set<String> allowedOrigins = new HashSet<>();
    private Set<HttpMethod> allowedMethods = new HashSet<>();

}
