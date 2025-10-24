package com.example.dyeTrack.in.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.micrometer.common.lang.Nullable;

@Configuration
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    // Liste configurable des endpoints publics
    private static final String[] SECURED_URLS = {
            "/api/user/getUserConnected",
            "/api/preset-seances/**",
            "/api/Exercise/create",
            "/api/Exercise/createMultiple",
            "/api/user/delete/**",
            "/api/Exercise/update/**",

    };

    private final ApiKeyFilter apiKeyFilter;

    public SecurityConfig(JWTAuthenticationFilter jwtAuthenticationFilter, @Nullable ApiKeyFilter apiKeyFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.apiKeyFilter = apiKeyFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(SECURED_URLS).authenticated()
                        .anyRequest().permitAll())
                .anonymous(an -> an.disable());
        if (apiKeyFilter != null) {
            http.addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class);
        }
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
