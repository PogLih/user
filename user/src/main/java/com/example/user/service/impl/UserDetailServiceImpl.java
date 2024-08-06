package com.example.user.service.impl;

import com.example.common_component.request.LoginRequest;
import com.example.data_component.entity.User;
import com.example.data_component.repository.UserRepository;
import com.example.data_component.specification.UserSpecification;
import com.example.user.service.UserService;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserSpecification userSpecification;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findOne(
            userSpecification.getByName(LoginRequest.builder().username(username).build()))
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

}
