package com.example.user.service;

import com.example.common_component.dto.request.RoleRequest;
import com.example.common_component.dto.response.RoleResponse;
import java.util.List;

public interface RoleService {

  public RoleResponse create(RoleRequest request);

  public List<RoleResponse> getAll();

  public void delete(String role);
}
