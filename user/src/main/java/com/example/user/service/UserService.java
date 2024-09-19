package com.example.user.service;

import com.example.common_component.dto.request.UserCreationRequest;
import com.example.common_component.dto.request.UserUpdateRequest;
import com.example.common_component.dto.response.UserResponse;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

  UserResponse signUp(UserCreationRequest request) throws Exception;

  UserResponse getMyInfo();

  List<UserResponse> getUsers();

  UserResponse getUser(String userId);

  void deleteUser(String userId);

  UserResponse updateUser(String userId, UserUpdateRequest request);
}
