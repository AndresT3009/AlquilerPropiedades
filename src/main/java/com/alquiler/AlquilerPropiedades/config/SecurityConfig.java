package com.alquiler.AlquilerPropiedades.config;

import com.alquiler.AlquilerPropiedades.controllers.CustomAuthenticationEntryPoint;
import com.alquiler.AlquilerPropiedades.domain.security.UserSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Slf4j
public class SecurityConfig {
    private final UserSecurityService userSecurityService;
    private final JwtFilter jwtFilter;
    private final AccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


    public SecurityConfig(UserSecurityService userSecurityService, JwtFilter jwtFilter,
                          AccessDeniedHandler accessDeniedHandler, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.userSecurityService = userSecurityService;
        this.jwtFilter = jwtFilter;
        this.accessDeniedHandler = accessDeniedHandler;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("Configuring SecurityFilterChain...");
        http.csrf().disable()
                .authorizeRequests()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/clients/save").permitAll()
                .requestMatchers("/api/clients/all").hasRole("ADMIN")
                .requestMatchers("/api/clients/search").hasRole("ADMIN")
                .requestMatchers("/api/properties/all").permitAll()
                .requestMatchers("/api/properties/search").permitAll()
                .requestMatchers("/api/properties/reserve").permitAll()
                .requestMatchers("/api/properties/search").hasRole("ADMIN")
                .requestMatchers("/api/properties/delete").hasRole("ADMIN")
                .requestMatchers("/api/properties/update").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        log.info("SecurityFilterChain configured successfully.");
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        log.info("Creating AuthenticationManager...");
        AuthenticationManager manager = http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userSecurityService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
        log.info("AuthenticationManager created successfully.");
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Initializing PasswordEncoder (BCryptPasswordEncoder)...");
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        log.info("PasswordEncoder initialized successfully.");
        return encoder;
    }
}
