package com.example.user.controller;

import com.example.common_component.dto.ApiResponse;
import com.example.common_component.dto.request.PermissionRequest;
import com.example.common_component.dto.response.PermissionResponse;
import com.example.user.service.PermissionService;
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
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class PermissionController {

  private final PermissionService permissionService;

  @PostMapping
  public ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest permissionRequest) {
    return ApiResponse.<PermissionResponse>builder()
        .result(permissionService.create(PermissionRequest.builder().build())).build();
  }

  @GetMapping
  public ApiResponse<List<PermissionResponse>> getAll() {
    return ApiResponse.<List<PermissionResponse>>builder().result(permissionService.getAll())
        .build();
  }

  @DeleteMapping("/{permission}")
  public ApiResponse<Void> delete(@PathVariable String permission) {
    permissionService.delete(permission);
    return ApiResponse.<Void>builder().build();
  }
}
