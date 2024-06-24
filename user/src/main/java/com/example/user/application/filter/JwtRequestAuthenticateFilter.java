package com.example.user.application.filter;

import com.example.user.application.service.AuthService;
import com.example.user.application.service.JWTService;
import com.example.user.application.service.UserService;
import com.mysql.cj.util.StringUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Log4j2
@RequiredArgsConstructor
public class JwtRequestAuthenticateFilter extends OncePerRequestFilter {
    private final AuthService authService;
    private final UserService userService;
    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (StringUtils.isNullOrEmpty(authHeader) || StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        if (StringUtils.isNullOrEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(userEmail);
            if(jwtService.isTokenValid(jwt,userDetails)){
                SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                emptyContext.setAuthentication(token);
                SecurityContextHolder.setContext(emptyContext);
            }
        }
    }
}
