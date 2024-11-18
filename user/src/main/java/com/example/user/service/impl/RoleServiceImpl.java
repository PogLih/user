package com.example.user.service.impl;

import com.example.common_component.dto.request.RoleRequest;
import com.example.common_component.dto.response.RoleResponse;
import com.example.data_component.entity.Permission;
import com.example.data_component.entity.Role;
import com.example.data_component.repository.PermissionRepository;
import com.example.data_component.repository.RoleRepository;
import com.example.data_component.specification.PermissionSpecification;
import com.example.user.service.RoleService;
import java.util.HashSet;
import java.util.List;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

  private final ModelMapper modelMapper;
  private final RoleRepository roleRepository;
  private final PermissionRepository permissionRepository;
  private final PermissionSpecification permissionSpecification;

  public RoleResponse create(RoleRequest request) {
    Role role = modelMapper.map(request, Role.class);

    List<Permission> permissions = permissionRepository.findAll(
        permissionSpecification.getByIds(request.getPermissions()));
    role = roleRepository.save(role);
    role.setPermissions(new HashSet<>(permissions));
    role = roleRepository.save(role);
    return modelMapper.map(role, RoleResponse.class);
  }

  public List<RoleResponse> getAll() {
    return roleRepository.findAll().stream().map(role -> modelMapper.map(role, RoleResponse.class))
        .toList();
  }

  public void delete(String role) {
    roleRepository.deleteById(Long.valueOf(role));
  }

}
