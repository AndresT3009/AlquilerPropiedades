package com.alquiler.AlquilerPropiedades.config;

import com.alquiler.AlquilerPropiedades.domain.security.UserSecurityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserSecurityService userSecurityService;

    public JwtFilter(JwtService jwtService, UserSecurityService userSecurityService) {
        this.jwtService = jwtService;
        this.userSecurityService = userSecurityService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        log.debug("Incoming request: method={}, URI={}, Authorization={}",
                request.getMethod(), request.getRequestURI(), token);
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            log.debug("Extracted JWT token: {}", token);
            String email = jwtService.extractEmail(token);
            log.debug("Extracted email from token: {}", email);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.debug("Email is valid and no authentication is present in SecurityContext.");
                UserDetails userDetails = userSecurityService.loadUserByUsername(email);
                log.debug("Loaded UserDetails for email: {}", email);

                if (jwtService.isTokenValid(token, userDetails.getUsername())) {
                    log.debug("Token is valid for email: {}", email);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.debug("Authentication set for email: {}", email);

                } else {
                    log.warn("Invalid or expired token for email: {}", email);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"userMessage\": \"Session expired\", \"technicalMessage\": \"Your session has expired. Please log in again.\"}");
                    return;
                }
            } else {
                log.debug("Email is null or authentication is already present.");
            }
        } else if (token == null) {
            log.debug("No Authorization header present in request.");
        } else {
            log.warn("Invalid Authorization header format: {}", token);
        }
        chain.doFilter(request, response);
    }
}
