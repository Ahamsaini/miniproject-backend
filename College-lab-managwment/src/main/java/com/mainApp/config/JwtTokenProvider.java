package com.mainApp.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mainApp.model.User;


import java.util.Date;

import javax.crypto.SecretKey;

@Component
@Slf4j
public class JwtTokenProvider {
    
    @Value("${app.jwt.secret:}")
    private String jwtSecret;
    
    @Value("${app.jwt.access-token-validity:3600000}")
    private Long accessTokenValidity;
    
    @Value("${app.jwt.refresh-token-validity:2592000000}")
    private Long refreshTokenValidity;
    
    private SecretKey getSigningKey() {
        if (jwtSecret == null || jwtSecret.isEmpty()) {
            log.error("JWT Secret is not configured! Please set 'app.jwt.secret' in application.properties.");
            // Return a temporary key to prevent crash if absolutely necessary, 
            // but throwing an exception here is safer as it indicates a configuration error.
            throw new IllegalStateException("JWT Secret is not configured. Check application.properties.");
        }
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    
    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenValidity);
    }
    
    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenValidity);
    }
    
    private String generateToken(User user, Long validity) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validity);
        
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }
    
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
        return claims.getSubject();
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        }
        return false;
    }
    
    public Long getAccessTokenValidity() {
        return accessTokenValidity;
    }
}
