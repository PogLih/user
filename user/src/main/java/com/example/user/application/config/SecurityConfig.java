package com.example.user.application.config;

import com.example.user.application.config.properties.AuthenticationProperties;
import com.example.user.application.config.properties.CORSProperties;
import com.example.user.application.config.properties.CSRFProperties;
import com.example.user.application.config.properties.SessionProperties;
import com.example.user.application.filter.JwtRequestAuthenticateFilter;
import com.example.user.application.service.impl.UserDetailServiceImpl;
import com.example.user.common.enums.ErrorCodeEnum;
import com.example.user.common.response.BaseResponse;
import com.example.user.common.response.FailureResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
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

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    setSessionManager(http);
    setCSRF(http);
    setCORS(http);
    applyAuthenticationConfig(http);
    applyExceptionHandler(http);

    http.authenticationProvider(authenticationProvider())
        .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
        .userDetailsService(userDetailService);
    return http.build();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(userDetailService);
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
    return daoAuthenticationProvider;
  }

  private void setSessionManager(HttpSecurity http) throws Exception {
    http.sessionManagement((config) -> {
      config.sessionCreationPolicy(sessionProperties.getPolicy());
    });
  }

  private void setCSRF(HttpSecurity http) throws Exception {
    if (csrfProperties != null) {
      if (csrfProperties.isEnabled()) {
        RequestMatcher requireProtectRequests =
            new AntPathRequestMatcher(
                StringUtils.isEmpty(csrfProperties.getProtectList()) ? "/**"
                    : csrfProperties.getProtectList());
        RequestMatcher ignoreListRequest = new AntPathRequestMatcher(
            StringUtils.isEmpty(csrfProperties.getIgnoreList()) ? "/**"
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
        : null);//ex:"http://example.com" or "http://exa*.com"
    configuration.setAllowedMethods(
        !corsProperties.getAllowedMethods().isEmpty() ? corsProperties.getAllowedMethods()
            : null); //ex:GET, POST, PUT, DELETE, etc
    configuration.setAllowedHeaders(
        !corsProperties.getAllowedHeaders().isEmpty() ? corsProperties.getAllowedHeaders()
            : null);//ex:"Content-Type", "Authorization
    configuration.setExposedHeaders(
        !corsProperties.getExposedHeader().isEmpty() ? corsProperties.getExposedHeader() : null)
    ;//ex:"X-Custom-Header"
    configuration.setAllowCredentials(corsProperties.isAllowCredentials());
    configuration.setMaxAge(corsProperties.getMaxAge() != null ?
        corsProperties.getMaxAge() : 36400L);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;


  }

  private void applyAuthenticationConfig(HttpSecurity http) throws Exception {
    if (authenticationProperties.isByPassAuth()) {
      log.warn("*** WARNING: Authentication was bypassed. Do NOT use in production " +
          "deployments.");
      http.authorizeHttpRequests(config -> {
        config.anyRequest().permitAll();
      });
      return;
    }
    http.authorizeHttpRequests(config -> {
      if (!authenticationProperties.getPublicPaths().isEmpty()) {
        authenticationProperties.getPublicPaths().forEach(publicPath -> {
          publicPath.getMethods().forEach(method -> {
            config.requestMatchers(method, publicPath.getUrl()).permitAll();
          });
        });
      } else {
        config.requestMatchers("/**").permitAll();
      }
      config.anyRequest().authenticated();
    });
  }

  private void applyExceptionHandler(HttpSecurity http) throws Exception {
    http.exceptionHandling(config -> {
      config.authenticationEntryPoint(buildAuthenticationEntryPoint());
      config.accessDeniedHandler(buildAccessDeniedHandler());
    });
  }

  private AuthenticationEntryPoint buildAuthenticationEntryPoint() {
    return ((request, response, authException) -> {
      BaseResponse responseEntity = new FailureResponse().setErrorMessage("You need to " +
              "login first in order to perform this action.")
          .setErrorCode(ErrorCodeEnum.NEED_AUTHENTICATE);
      ResponseEntity<?> build = responseEntity.build();

      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter().write(new ObjectMapper().writeValueAsString(build.getBody()));
    });
  }

  private AccessDeniedHandler buildAccessDeniedHandler() {
    return ((request, response, accessDeniedException) -> {
      BaseResponse responseEntity = new FailureResponse().setErrorMessage("You don't have " +
          "required role to perform this action.").setErrorCode(ErrorCodeEnum.NOT_HAVE_PERMISSION);
      ResponseEntity<?> build = responseEntity.build();

      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(HttpStatus.FORBIDDEN.value());
      response.getWriter().write(new ObjectMapper().writeValueAsString(build.getBody()));
    });
  }
}
