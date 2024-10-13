package com.example.user.service;

import com.example.common_component.dto.request.UserCreationRequest;
import com.example.common_component.dto.request.UserUpdateRequest;
import com.example.common_component.dto.response.UserResponse;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

  public UserResponse signUp(UserCreationRequest request) throws Exception;

  public UserResponse getMyInfo();

  public List<UserResponse> getUsers();

  public UserResponse getUser(String userId);

  public void deleteUser(String userId);

  public UserResponse updateUser(String userId, UserUpdateRequest request);
}
