package com.example.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.example.common_component", "com.example.data_component"})
public class UserApplication {

  public static void main(String[] args) {
    SpringApplication userApplication = new SpringApplication(UserApplication.class);
    userApplication.run(args);

  }

}
