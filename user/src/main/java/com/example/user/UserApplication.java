package com.example.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.example.data_component.entity")
@EnableJpaRepositories(basePackages = "com.example.data_component.repository")
@ComponentScan({"com.example.user", "com.example.common_component", "com.example.data_component"})
public class UserApplication extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(UserApplication.class);
  }

  public static void main(String[] args) {
    SpringApplication userApplication = new SpringApplication(UserApplication.class);
    userApplication.run(args);
  }
}
