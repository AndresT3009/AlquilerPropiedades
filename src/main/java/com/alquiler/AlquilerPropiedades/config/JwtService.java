package com.alquiler.AlquilerPropiedades.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Set;

@Slf4j
@Service
public class JwtService {

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 60000;

    public String generateToken(String email, Set<String> roles) {
        log.debug("Generating token for email: {}, roles: {}", email, roles);
        String token = Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
        log.debug("Generated token: {}", token);
        return token;
    }

    public String extractEmail(String token) {
        try {
            String email = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            log.debug("Extracted email from token: {}", email);
            return email;
        } catch (Exception e) {
            log.warn("Failed to extract email from token: {}", token, e);
            throw e;
        }
    }

    public boolean isTokenValid(String token, String email) {
        log.debug("Token validation result for email {}", email);
        String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
