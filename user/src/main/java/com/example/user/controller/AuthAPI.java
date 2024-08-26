package com.example.user.controller;


import com.example.common_component.request.SignUpRequest;
import com.example.common_component.response.ResponseData;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AuthAPI {

  @PostConstruct
  public void init() {
    log.info("AuthAPI initialized");
  }

  @PostMapping(value = {"/sign-up"})
  public ResponseData handleSignUpRequest(@RequestBody SignUpRequest loginRequest) {
    log.info("Received sign-up request");
    return new ResponseData(loginRequest);
  }

  @GetMapping("/**")
  public String catchAll() {
    log.info("Catch-all endpoint hit");
    return "Catch-all response";
  }
}
