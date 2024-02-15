package com.example.user.service.impl;

import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import com.example.user.request.LoginRequest;
import com.example.user.request.SignUpRequest;
import com.example.user.response.BaseResponse;
import com.example.user.service.AuthService;
import com.example.user.service.ServiceManager;
import com.example.user.specification.UserSpecification;
import com.example.user.valid.UserValid;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ServiceManager serviceManager;
    private final UserRepository userRepository;
    private final UserSpecification userSpecification;
    private final UserValid userValid;
    public Authentication authenticate(HttpServletRequest request) {

        return null;
    }

    public BaseResponse login(LoginRequest loginRequest){
        return serviceManager
                .setRequest(loginRequest)
                .setRepo(userRepository)
                .setSpec(userSpecification)
                .setValid(userValid)
                .setServiceHandle(new ServiceManager.ServiceHandler<User>() {
            @Override
            public User handleService() {
                return User.builder().username(loginRequest.getUsername()).password(loginRequest.getPassword()).build();
            }
        }).execute();
    }

    public BaseResponse signup(SignUpRequest signUpRequest){
        return null;
//        return serviceManager.setValid(signUpRequest).setRepo(userRepository).setSpec(userSpecification).setServiceHandle(() -> {
//            User user = User.builder().username("admin").password("password").build();
//            this.userRepository.saveAndFlush(user);
//        }).getResponse();
    }
}
