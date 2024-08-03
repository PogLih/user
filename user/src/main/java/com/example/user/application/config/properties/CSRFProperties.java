package com.example.user.application.config.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "security.crfs")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CSRFProperties {

  String ignoreList;
  String protectList;
  boolean withHttp;
  boolean enabled = false;

}
