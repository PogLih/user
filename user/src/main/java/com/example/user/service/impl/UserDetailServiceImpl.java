package com.example.user.service.impl;

import static com.example.common_component.constant.UserConstant.USER_ROLE;

import com.example.common_component.dto.request.UserCreationRequest;
import com.example.common_component.dto.request.UserUpdateRequest;
import com.example.common_component.dto.response.UserResponse;
import com.example.common_component.service.ServiceManagerFactory;
import com.example.data_component.entity.Role;
import com.example.data_component.entity.User;
import com.example.data_component.repository.RoleRepository;
import com.example.data_component.repository.UserRepository;
import com.example.data_component.specification.RoleSpecification;
import com.example.data_component.specification.UserSpecification;
import com.example.user.application.validation.UserValid;
import com.example.user.exception.ApplicationException;
import com.example.user.exception.ErrorCode;
import com.example.user.service.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserSpecification userSpecification;
  private final RoleRepository roleRepository;
  private final RoleSpecification roleSpecification;
  private final ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;
  private final ServiceManagerFactory serviceManagerFactory;
  private final UserValid userValid;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findOne(
            userSpecification.getByName(username))
        .orElse(null);
    if (user == null) {
      throw new UsernameNotFoundException("User not exists");
    }
    Set<GrantedAuthority> authorities = user.getRoles().stream()
        .map((role) -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toSet());
    return new org.springframework.security.core.userdetails.User(username, user.getPassword(),
        authorities);
  }

  @Override
  public UserResponse signUp(UserCreationRequest request) {
    User user = modelMapper.map(request, User.class);
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    HashSet<Role> roles = new HashSet<>();
    Role role = roleRepository.findOne(roleSpecification.getByName(USER_ROLE)).orElseGet(() -> {
      return Role.builder().name(USER_ROLE).build();
    });
    roles.add(role);
    user.setRoles(roles);

    try {
      user = userRepository.saveAndFlush(user);
    } catch (DataIntegrityViolationException exception) {
      throw new ApplicationException(ErrorCode.USER_EXISTED);
    }

    return modelMapper.map(user, UserResponse.class);
  }

  @Override
  public UserResponse getMyInfo() {
    var context = SecurityContextHolder.getContext();
    String name = context.getAuthentication().getName();

    User user = userRepository.findOne(userSpecification.getByName(name))
        .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_EXISTED));

    return modelMapper.map(user, UserResponse.class);
  }

  @PostAuthorize("returnObject.username == authentication.name")
  @Override
  public UserResponse updateUser(String userId, UserUpdateRequest request) {
    User user = userRepository.findOne(userSpecification.getByName(userId))
        .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_EXISTED));

    modelMapper.map(user, request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    var roles = roleRepository.findAll(roleSpecification.getByRoles(request.getRoles()));
    user.setRoles(new HashSet<>(roles));

    return modelMapper.map(userRepository.save(user), UserResponse.class);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Override
  public void deleteUser(String userId) {
    userRepository.delete(userSpecification.getByName(userId));
  }

  //  @PreAuthorize("hasRole('ADMIN')")
  @Override
  public List<UserResponse> getUsers() {
    log.info("In method get Users");
    return userRepository.findAll().stream().map(user -> modelMapper.map(user, UserResponse.class))
        .toList();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Override
  public UserResponse getUser(String id) {
    return modelMapper.map(
        userRepository.findOne(userSpecification.getByName(id))
            .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_EXISTED)),
        UserResponse.class);
  }
}
