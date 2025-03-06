package com.example.user.service;

import com.example.common_component.dto.request.AuthenticationRequest;
import com.example.common_component.dto.request.IntrospectRequest;
import com.example.common_component.dto.request.LogoutRequest;
import com.example.common_component.dto.request.RefreshRequest;
import com.example.common_component.dto.response.AuthenticationResponse;
import com.example.common_component.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;
import java.text.ParseException;

public interface AuthService {


  public AuthenticationResponse signIn(AuthenticationRequest request) throws Exception;

  public IntrospectResponse verify(IntrospectRequest request) throws JOSEException, ParseException;

  public void logOut(LogoutRequest request) throws ParseException, JOSEException;

  public AuthenticationResponse refreshToken(RefreshRequest request);
}
