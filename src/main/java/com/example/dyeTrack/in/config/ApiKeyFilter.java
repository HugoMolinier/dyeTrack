package com.example.dyeTrack.in.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
@Order(1)
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${api.key}")
    private String apiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.startsWith("/api-docs") || path.startsWith("/swagger-ui") || path.startsWith("/webjars")) {
            filterChain.doFilter(request, response);
            return;
        }

        String headerKey = request.getHeader("X-API-KEY");
        if (headerKey == null || !headerKey.equals(apiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter()
                    .write("{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Invalid API Key\",\"path\":\""
                            + path + "\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
