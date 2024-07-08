package com.example.user.application.config.properties;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "security.cors")
public class CORSProperties {

  private boolean enabled;
  private boolean allowCredentials;
  private Long maxAge;
  private List<String> allowedOriginPatterns;
  private List<String> allowedMethods;
  private List<String> allowedHeaders;
  private List<String> exposedHeader;


}
