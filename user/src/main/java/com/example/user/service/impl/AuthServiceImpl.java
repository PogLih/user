package com.example.user.service.impl;

import com.example.common_component.request.LoginRequest;
import com.example.common_component.request.SignUpRequest;
import com.example.common_component.response.ResponseData;
import com.example.common_component.response.dto.LoginResponse;
import com.example.common_component.service.ServiceHandler.WriteEntityHandler;
import com.example.common_component.service.ServiceManager;
import com.example.data_component.entity.Role;
import com.example.data_component.entity.User;
import com.example.data_component.repository.RoleRepository;
import com.example.data_component.repository.UserRepository;
import com.example.data_component.specification.RoleSpecification;
import com.example.data_component.specification.UserSpecification;
import com.example.user.application.validation.impl.UserValid;
import com.example.user.entity.Account;
import com.example.user.service.AuthService;
import com.example.user.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private ServiceManager<User> serviceManager;
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
    Account account = modelMapper.map(user, Account.class);
    String jwt = jwtService.generateToken(account);
    String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
    LoginResponse loginResponse =
        LoginResponse.builder().jwt(jwt).refreshToken(refreshToken).build();
    return new UsernamePasswordAuthenticationToken(account, "N/A", account.getAuthorities());
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

  @Override
  public ResponseData signup(SignUpRequest signUpRequest) throws Exception {
    return ServiceManager
        .<User>builder().baseRequest(signUpRequest)
        .baseValid(userValid)
        .serviceHandler((WriteEntityHandler<User>) request -> {
          Role role =
              roleRepository.findOne(roleSpecification.getByName("user"))
                  .orElse(null);
          if (role == null) {
            role = Role.builder().name("user").build();
          }
          User user = modelMapper.map(request, User.class);
          if (CollectionUtils.isEmpty(user.getRoles())) {
            user.setRoles(Set.of(role));
          } else {
            user.getRoles().add(role);
          }
          userRepository.saveAndFlush(user);
          return user;
        }).build().execute();
  }
}
