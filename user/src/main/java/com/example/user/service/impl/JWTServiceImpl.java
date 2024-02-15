package com.example.user.service.impl;

import com.example.user.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.*;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.InputStream;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
@Service
public class JWTServiceImpl implements JWTService {
    private String generateToken(UserDetails userDetails) {
        return Jwts.builder().subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignKey(), new SecureDigestAlgorithm<Key, Key>() {
                    @Override
                    public byte[] digest(SecureRequest<InputStream, Key> inputStreamKeySecureRequest) throws SecurityException {
                        return new byte[0];
                    }

                    @Override
                    public boolean verify(VerifySecureDigestRequest<Key> keyVerifySecureDigestRequest) throws SecurityException {
                        return false;
                    }

                    @Override
                    public String getId() {
                        return null;
                    }
                }).compact();
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token,Claims::getExpiration).before(new Date());
    }

    private Key getSignKey() {
        byte[] key = Decoders.BASE64.decode("user");
        return Keys.hmacShaKeyFor(key);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimsResoilvers){
        final Claims claims = extractAllClaims(token);
        return claimsResoilvers.apply(claims);

    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith((SecretKey) getSignKey()).build().parseSignedClaims(token).getPayload();
    }
}
