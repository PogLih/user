package com.example.user.application.service.impl;

import com.example.user.application.service.UserService;
import com.example.user.common.entity.User;
import com.example.user.database.repository.UserRepository;
import com.example.user.common.request.LoginRequest;
import com.example.user.database.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserSpecification userSpecification;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findOne(userSpecification.getByName(LoginRequest.builder().username(username).build())).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("User not exists");
        }
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }

}
