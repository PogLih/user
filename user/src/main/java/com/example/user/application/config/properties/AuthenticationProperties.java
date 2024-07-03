package com.example.user.application.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Component
@ConfigurationProperties(prefix = "security.authentication")
public class AuthenticationProperties {

    @Data
    public static class PublicPath {

        private String url;
        private Set<HttpMethod> methods = new HashSet<>();

    }

    private boolean byPassAuth = false;
    private List<PublicPath> publicPaths = new ArrayList<>();


}
