package com.example.user.application.config;

import com.example.common_component.dto.ApiResponse;
import com.example.common_component.enums.ErrorCodeEnum;
import com.example.common_component.validation.ErrorObject;
import com.example.user.application.config.properties.AuthenticationProperties;
import com.example.user.application.config.properties.CORSProperties;
import com.example.user.application.config.properties.CSRFProperties;
import com.example.user.application.config.properties.SessionProperties;
import com.example.user.application.filter.JwtRequestAuthenticateFilter;
import com.example.user.service.impl.UserDetailServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Log4j2
public class SecurityConfig {

  private final SessionProperties sessionProperties;
  private final CSRFProperties csrfProperties;
  private final CORSProperties corsProperties;
  private final AuthenticationProperties authenticationProperties;
  private final UserDetailServiceImpl userDetailService;
  private final JwtRequestAuthenticateFilter filter;
  private final PasswordEncoder passwordEncoder;
  @Value("${jwt.signKey}")
  private String SIGN_KEY;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    setSessionManager(http);
    setCSRF(http);
    setCORS(http);
    applyAuthenticationConfig(http);
    applyExceptionHandler(http);
//    http.authenticationProvider(authenticationProvider())
//        .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
//        .userDetailsService(userDetailService);
    http.oauth2ResourceServer(config -> {
      config.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())
          .jwtAuthenticationConverter(jwtAuthenticationConverter()));
    });
    return http.build();
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
    jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("scope");
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    SecretKey secretKey = new SecretKeySpec(SIGN_KEY.getBytes(), "HS512");
    return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS512).build();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(userDetailService);
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
    return daoAuthenticationProvider;
  }

  @Bean
  public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private void setSessionManager(HttpSecurity http) throws Exception {
    http.sessionManagement((config) -> {
      config.sessionCreationPolicy(sessionProperties.getPolicy());
    });
  }

  private void setCSRF(HttpSecurity http) throws Exception {
    if (csrfProperties != null && csrfProperties.isEnabled()) {
      RequestMatcher requireProtectRequests = new AntPathRequestMatcher(
          !StringUtils.hasLength(csrfProperties.getProtectList()) ? "/**"
              : csrfProperties.getProtectList());
      RequestMatcher ignoreListRequest = new AntPathRequestMatcher(
          !StringUtils.hasLength(csrfProperties.getIgnoreList()) ? "/**"
              : csrfProperties.getIgnoreList());

      http.csrf(httpSecurityCsrfConfigurer -> {
        httpSecurityCsrfConfigurer.csrfTokenRepository(
                csrfProperties.isWithHttp() ? new CookieCsrfTokenRepository()
                    : CookieCsrfTokenRepository.withHttpOnlyFalse())
            .ignoringRequestMatchers(ignoreListRequest)
            .requireCsrfProtectionMatcher(requireProtectRequests);
      });
    } else {
      http.csrf(AbstractHttpConfigurer::disable);
    }
  }

  private void setCORS(HttpSecurity http) throws Exception {
    if (corsProperties != null && corsProperties.isEnabled()) {
      http.cors((config) -> {
        config.configurationSource(this.buildCORSConfiguration());
      });
    } else {
      http.cors(AbstractHttpConfigurer::disable);
      log.debug("CORS is disabled. Allowed all request from all origins");
    }
  }

  private CorsConfigurationSource buildCORSConfiguration() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOriginPatterns(!corsProperties.getAllowedOriginPatterns().isEmpty()
        ? corsProperties.getAllowedOriginPatterns()
        : List.of("*"));//ex:"http://example.com" or "http://exa*.com"
    configuration.setAllowedMethods(
        corsProperties.getAllowedMethods().isEmpty() ? Arrays.asList("GET", "POST", "PUT", "DELETE",
            "OPTIONS") : corsProperties.getAllowedMethods());
    configuration.setAllowedHeaders(
        corsProperties.getAllowedHeaders().isEmpty() ? Arrays.asList("Content-Type",
            "Authorization") : corsProperties.getAllowedHeaders());
    configuration.setExposedHeaders(
        corsProperties.getExposedHeader().isEmpty() ? List.of("X-Custom-Header")
            : corsProperties.getExposedHeader());
    configuration.setAllowCredentials(corsProperties.isAllowCredentials());
    configuration.setMaxAge(
        corsProperties.getMaxAge() != null ? corsProperties.getMaxAge() : 3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  private void applyAuthenticationConfig(HttpSecurity http) throws Exception {
    if (authenticationProperties.isByPassAuth()) {
      log.warn("*** WARNING: Authentication was bypassed. Do NOT use in production deployments.");
      http.authorizeHttpRequests(config -> config.anyRequest().permitAll());
      return;
    }

    http.authorizeHttpRequests(config -> {
      if (!authenticationProperties.getPublicPaths().isEmpty()) {
        authenticationProperties.getPublicPaths().forEach(publicPath -> {
          publicPath.getMethods()
              .forEach(method -> config.requestMatchers(method, publicPath.getUrl()).permitAll());
        });
      } else {
        config.requestMatchers("/**").permitAll();
      }
      if (!authenticationProperties.getPrivatePaths().isEmpty()) {
        authenticationProperties.getPrivatePaths().forEach(privatePath -> {
          privatePath.getMethods().forEach(method -> {
            if (StringUtils.hasLength(privatePath.getAuthority()) &&
                !StringUtils.hasLength(privatePath.getPermissions())) {
              config.requestMatchers(method, privatePath.getUrl())
                  .hasRole(privatePath.getAuthority());
            } else if (!StringUtils.hasLength(privatePath.getAuthority()) &&
                StringUtils.hasLength(privatePath.getPermissions())) {
              config.requestMatchers(method, privatePath.getUrl())
                  .hasAuthority(privatePath.getPermissions());
            }
            if (StringUtils.hasLength(privatePath.getAuthority()) &&
                StringUtils.hasLength(privatePath.getPermissions())) {
              config.requestMatchers(method, privatePath.getUrl())
                  .access(hasRoleAndAuthority(privatePath.getAuthority(),
                      privatePath.getPermissions()));

            }
          });
        });
      }
      config.anyRequest().authenticated();
    });
  }

  private AuthorizationManager<RequestAuthorizationContext> hasRoleAndAuthority(String role,
      String permission) {
    return (authentication, context) -> {
      if (authentication.get().getAuthorities().isEmpty()) {
        return new AuthorizationDecision(false);
      }

      Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();
      boolean hasRole = authorities.contains(new SimpleGrantedAuthority("ROLE_" + role));
      boolean hasPermission = authorities.contains(new SimpleGrantedAuthority(permission));

      return new AuthorizationDecision(hasRole && hasPermission);
    };
  }

  private void applyExceptionHandler(HttpSecurity http) throws Exception {
    http.exceptionHandling(config -> {
      config.authenticationEntryPoint(buildAuthenticationEntryPoint());
      config.accessDeniedHandler(buildAccessDeniedHandler());
    });
  }

  private AuthenticationEntryPoint buildAuthenticationEntryPoint() {
    return (request, response, authException) -> {
      ApiResponse responseData = ApiResponse.builder().result(
          ErrorObject.builder().message("You need to login first in order to perform this action.")
              .code(ErrorCodeEnum.NEED_AUTHENTICATE).build()).build();
      ResponseEntity<?> build = ResponseEntity.of(Optional.of(responseData));

      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter().write(new ObjectMapper().writeValueAsString(build.getBody()));
    };
  }

  private AccessDeniedHandler buildAccessDeniedHandler() {
    return (request, response, accessDeniedException) -> {
      ApiResponse responseData = ApiResponse.builder().result(
          ErrorObject.builder().message("You don't have " + "required role to perform this action.")
              .code(ErrorCodeEnum.NOT_HAVE_PERMISSION).build()).build();
      ResponseEntity<?> build = ResponseEntity.of(Optional.of(responseData));

      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(HttpStatus.FORBIDDEN.value());
      response.getWriter().write(new ObjectMapper().writeValueAsString(build.getBody()));
    };
  }
}
