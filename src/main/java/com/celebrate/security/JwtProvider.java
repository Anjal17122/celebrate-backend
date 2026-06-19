package com.celebrate.security;

import com.celebrate.exception.UnauthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtProvider {

    @Value("${app.jwt.secret:celebrate-secret-key-must-be-at-least-32-chars-long}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400000}")
    private long jwtExpiration;

    public String generateToken(String userId, String email, String userType) {
        return Jwts.builder()
                .subject(userId)
                .claims(Map.of("email", email, "userType", userType))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignKey())
                .compact();
    }

    public String getUserIdFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    public String getUserTypeFromToken(String token) {
        return (String) parseClaims(token).get("userType");
    }

    public String getEmailFromToken(String token) {
        return (String) parseClaims(token).get("email");
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token is Expired",e);
            return false;
//            throw new UnauthorizedException("Token has expired. Please log in again.");
        } catch (JwtException e) {
            throw new UnauthorizedException("Invalid token.");
        }
    }

    public int getExpirationSeconds() {
        return (int) (jwtExpiration / 1000);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(
                java.util.Base64.getEncoder().encodeToString(jwtSecret.getBytes()));
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
