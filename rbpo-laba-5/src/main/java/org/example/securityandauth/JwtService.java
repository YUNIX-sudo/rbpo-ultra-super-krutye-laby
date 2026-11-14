package org.example.securityandauth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${jwt.secret:super-secret-key-must-be-at-least-256-bits-long-for-hs256-compliance-12345678}")
    private String secret;

    @Value("${jwt.access-token-expiration-seconds:900}")
    private long accessExpirationSeconds;

    @Value("${jwt.refresh-token-expiration-seconds:604800}")
    private long refreshExpirationSeconds;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public long getAccessExpirationSeconds() {
        return accessExpirationSeconds;
    }

    public long getRefreshExpirationSeconds() {
        return refreshExpirationSeconds;
    }

    public String generateAccessToken(String username, String role, Instant issuedAt, Instant expiration) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(username)
                .claim("type", "access")
                .claim("role", role)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(String username, Instant issuedAt, Instant expiration) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(username)
                .claim("type", "refresh")
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateAccessToken(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }
        try {
            Claims claims = parseClaims(token);
            if (claims.getId() == null || claims.getId().isBlank()) {
                return false;
            }
            if (claims.getSubject() == null || claims.getSubject().isBlank()) {
                return false;
            }
            if (claims.getIssuedAt() == null || claims.getExpiration() == null) {
                return false;
            }
            if (claims.getExpiration().toInstant().isBefore(Instant.now())) {
                return false;
            }
            return "access".equalsIgnoreCase(claims.get("type", String.class));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }
        try {
            Claims claims = parseClaims(token);
            if (claims.getId() == null || claims.getId().isBlank()) {
                return false;
            }
            if (claims.getSubject() == null || claims.getSubject().isBlank()) {
                return false;
            }
            if (claims.getIssuedAt() == null || claims.getExpiration() == null) {
                return false;
            }
            if (claims.getExpiration().toInstant().isBefore(Instant.now())) {
                return false;
            }
            return "refresh".equalsIgnoreCase(claims.get("type", String.class));
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    public String extractType(String token) {
        Claims claims = parseClaims(token);
        return claims.get("type", String.class);
    }

    public Instant extractExpiration(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration().toInstant();
    }
}
