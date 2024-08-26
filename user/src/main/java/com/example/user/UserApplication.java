package com.example.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.example"})
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
