package com.example.user.service.impl;

import com.example.common_component.dto.request.PermissionRequest;
import com.example.common_component.dto.response.PermissionResponse;
import com.example.data_component.entity.Permission;
import com.example.data_component.repository.PermissionRepository;
import com.example.user.service.PermissionService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PermissionServiceImpl implements PermissionService {

  private final PermissionRepository permissionRepository;
  private final ModelMapper modelMapper;
  ;

  public PermissionResponse create(PermissionRequest request) {
    Permission permission = modelMapper.map(request, Permission.class);
    permission = permissionRepository.save(permission);
    return modelMapper.map(permission, PermissionResponse.class);
  }

  public List<PermissionResponse> getAll() {
    var permissions = permissionRepository.findAll();
    return permissions.stream()
        .map(permission -> modelMapper.map(permission, PermissionResponse.class)).toList();
  }

  public void delete(String permission) {
    permissionRepository.deleteById(Long.valueOf(permission));
  }
}
