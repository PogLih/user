package com.example.user.application.service.impl;

import com.example.user.application.service.AuthService;
import com.example.user.application.service.JWTService;
import com.example.user.common.entity.Role;
import com.example.user.common.entity.User;
import com.example.user.common.implement.UserValid;
import com.example.user.common.interfaces.ServiceHandler;
import com.example.user.common.interfaces.ServiceManager;
import com.example.user.common.request.BaseRequest;
import com.example.user.common.request.LoginRequest;
import com.example.user.common.request.SignUpRequest;
import com.example.user.common.response.BaseResponse;
import com.example.user.common.response.dto.LoginResponse;
import com.example.user.database.repository.RoleRepository;
import com.example.user.database.repository.UserRepository;
import com.example.user.database.specification.RoleSpecification;
import com.example.user.database.specification.UserSpecification;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;


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
    private final ModelMapper modelMapper;

    public Authentication authenticate(HttpServletRequest request) {
        LoginRequest loginRequest = (LoginRequest) request;
        User user = userRepository.findOne(userSpecification.getByName(loginRequest)).orElse(null);
        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        LoginResponse loginResponse =
                LoginResponse.builder().jwt(jwt).refreshToken(refreshToken).build();
        return new UsernamePasswordAuthenticationToken(user, "N/A", user.getAuthorities());
    }

//    public BaseResponse login(LoginRequest loginRequest) throws Exception {
//        return serviceManager
//                .setRequest(loginRequest)
//                .setRepo(userRepository)
//                .setSpec(userSpecification.getByName(loginRequest))
//                .setValid(userValid)
//                .setServiceHandle(new ServiceManager.ServiceHandler<User, LoginResponse>() {
//                                      @Override
//                                      public LoginResponse handleService(User user) {
//                                          return LoginResponse.builder().jwt("test")
//                                          .refreshToken("test").build();
//                                      }
//                                  }
//                ).execute();
//    }

    public BaseResponse signup(SignUpRequest signUpRequest) throws Exception {
        return serviceManager
                .setRequest(signUpRequest)
                .setRepo(userRepository)
                .setSpec(userSpecification.getByName(signUpRequest))
                .setValid(userValid)
                .setServiceHandle(new ServiceHandler.WriteEntityHandler<User>() {
                    @Override
                    public User onChangeWriteEntityHandled(BaseRequest request) {
                        Role role =
                                roleRepository.findOne(roleSpecification.getByName("user")).orElse(null);
                        if (role == null) {
                            role = Role.builder().name("user").build();
                        }
                        return modelMapper.map(request, User.class);
                    }
                }).execute();
    }
}
