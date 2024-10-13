package com.example.user;

import static com.example.common_component.constant.UserConstant.ADMIN_ROLE;

import com.example.data_component.entity.Permission;
import com.example.data_component.entity.Role;
import com.example.data_component.entity.User;
import com.example.data_component.repository.UserRepository;
import com.example.data_component.specification.UserSpecification;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationStart {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final UserSpecification userSpecification;

  @Bean
  public ApplicationRunner applicationRunner() {
    return args -> {
      if (!userRepository.exists(userSpecification.getByName("admin"))) {
        User admin = User.builder().username("admin").password(passwordEncoder.encode("admin"))
            .roles(Set.of(
                Role.builder().name(ADMIN_ROLE).permissions(Set.of(Permission.builder().name("all")
                    .build())).build()))
            .build();
        userRepository.saveAndFlush(admin);
      }
    };
  }

}
