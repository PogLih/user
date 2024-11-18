package com.example.user.controller;

import com.example.common_component.dto.ApiResponse;
import com.example.common_component.dto.request.RoleRequest;
import com.example.common_component.dto.response.RoleResponse;
import com.example.user.service.RoleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@Slf4j
public class RoleController {

  private final RoleService roleService;


  @PostMapping
  public ApiResponse<RoleResponse> test(@RequestBody RoleRequest request) {
    return ApiResponse.<RoleResponse>builder()
        .result(roleService.create(request)).build();
  }

  @GetMapping
  public ApiResponse<List<RoleResponse>> getAll() {
    return ApiResponse.<List<RoleResponse>>builder().result(roleService.getAll()).build();
  }

  @DeleteMapping("/{role}")
  public ApiResponse<Void> delete(@PathVariable String role) {
    roleService.delete(role);
    return ApiResponse.<Void>builder().build();
  }
}
