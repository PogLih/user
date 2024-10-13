package com.example.user.service;

import com.example.common_component.dto.request.PermissionRequest;
import com.example.common_component.dto.response.PermissionResponse;
import java.util.List;

public interface PermissionService {

  public PermissionResponse create(PermissionRequest request);

  public List<PermissionResponse> getAll();

  public void delete(String permission);

}
