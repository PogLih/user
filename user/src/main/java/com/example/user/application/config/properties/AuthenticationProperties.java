package com.example.user.application.config.properties;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "security.authentication")
public class AuthenticationProperties {

  @Data
  public static class PublicPath {

    private String url;
    private Set<HttpMethod> methods = new HashSet<>();

  }

  @Data
  @EqualsAndHashCode(callSuper=false)
  public static class PrivatePath extends PublicPath {

    private String authority;
    private String permissions;
  }

  private boolean byPassAuth = false;
  private List<PublicPath> publicPaths = new ArrayList<>();
  private List<PrivatePath> privatePaths = new ArrayList<>();


}
