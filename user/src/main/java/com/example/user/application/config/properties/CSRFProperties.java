package com.example.user.application.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "security.crfs")
public class CSRFProperties {

    public String ignoreList;
    public String protectList;
    private boolean enabled = false;

}
