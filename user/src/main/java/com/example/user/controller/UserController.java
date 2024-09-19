package com.example.user.controller;

import com.example.common_component.dto.ApiResponse;
import com.example.common_component.dto.request.UserCreationRequest;
import com.example.common_component.dto.request.UserUpdateRequest;
import com.example.common_component.dto.response.UserResponse;
import com.example.user.service.UserService;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

  private final UserService userService;

  @PostConstruct
  public void init() {
    log.info("UserAPI initialized");
  }

  @PostMapping(value = {"/sign-up"})
  public ApiResponse<UserResponse> handleSignUpRequest(
      @RequestBody UserCreationRequest signUpRequest) throws Exception {
    log.info("Received sign-up request");
    return ApiResponse.<UserResponse>builder().result(userService.signUp(signUpRequest)).build();
  }

  @GetMapping("/my-info")
  ApiResponse<UserResponse> getMyInfo() {
    return ApiResponse.<UserResponse>builder()
        .result(userService.getMyInfo())
        .build();
  }

  @GetMapping("/getUsers")
  ApiResponse<List<UserResponse>> getUsers() {
    return ApiResponse.<List<UserResponse>>builder()
        .result(userService.getUsers())
        .build();
  }

  @GetMapping("/{userId}")
  ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
    return ApiResponse.<UserResponse>builder()
        .result(userService.getUser(userId))
        .build();
  }

  @DeleteMapping("/{userId}")
  ApiResponse<String> deleteUser(@PathVariable String userId) {
    userService.deleteUser(userId);
    return ApiResponse.<String>builder().result("User has been deleted").build();
  }

  @PutMapping("/{userId}")
  ApiResponse<UserResponse> updateUser(@PathVariable String userId,
      @RequestBody UserUpdateRequest request) {
    return ApiResponse.<UserResponse>builder()
        .result(userService.updateUser(userId, request))
        .build();
  }
}
