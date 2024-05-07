package com.example.user.service.impl;

import com.example.user.entity.Role;
import com.example.user.entity.User;
import com.example.user.repository.RoleRepository;
import com.example.user.repository.UserRepository;
import com.example.user.request.LoginRequest;
import com.example.user.request.SignUpRequest;
import com.example.user.response.BaseResponse;
import com.example.user.response.common.LoginResponse;
import com.example.user.response.common.SignUpResponse;
import com.example.user.service.AuthService;
import com.example.user.service.JWTService;
import com.example.user.service.ServiceManager;
import com.example.user.service.ServiceManager.ServiceHandler;
import com.example.user.specification.RoleSpecification;
import com.example.user.specification.UserSpecification;
import com.example.user.valid.UserValid;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ServiceManager<User> serviceManager;
    private final UserRepository userRepository;
    private final UserSpecification userSpecification;
    private final UserValid userValid;
    private final RoleRepository roleRepository;
    private final RoleSpecification roleSpecification;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public Authentication authenticate(HttpServletRequest request) {
        LoginRequest loginRequest = (LoginRequest) request;
        User user = userRepository.findOne(userSpecification.getByName(loginRequest)).orElse(null);
        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        LoginResponse loginResponse = LoginResponse.builder().jwt(jwt).refreshToken(refreshToken).build();
        return new UsernamePasswordAuthenticationToken(user, "N/A", user.getAuthorities());
    }

    public BaseResponse login(LoginRequest loginRequest) throws Exception {
        return serviceManager
                .setRequest(loginRequest)
                .setRepo(userRepository)
                .setSpec(userSpecification.getByName(loginRequest))
                .setValid(userValid)
                .setServiceHandle(
                        (ServiceHandler<User, LoginResponse>) () ->
                                User.builder().username(loginRequest.getUsername()).password(loginRequest.getPassword()).build()
                ).execute();
    }

    public BaseResponse signup(SignUpRequest signUpRequest) throws Exception {
        return serviceManager
                .setRequest(signUpRequest)
                .setRepo(userRepository)
                .setSpec(userSpecification.getByName(signUpRequest))
                .setValid(userValid)
                .setServiceHandle((ServiceHandler<User, SignUpResponse>) () -> {
                    Role role = roleRepository.findOne(roleSpecification.getByName("user")).orElse(null);
                    if (role == null) {
                        role = Role.builder().name("user").build();
                    }
                    return User.builder()
                            .username(signUpRequest.getUsername())
                            .password(passwordEncoder.encode(signUpRequest.getPassword()))
                            .email(signUpRequest.email())
                            .roles(Set.of(role)).build();
                }).execute();
    }
}
