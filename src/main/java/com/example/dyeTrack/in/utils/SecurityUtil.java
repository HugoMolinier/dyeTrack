package com.example.dyeTrack.in.utils;

import org.springframework.security.core.context.SecurityContextHolder;

import com.example.dyeTrack.core.exception.ForbiddenException;

public class SecurityUtil {

    public static Long getUserIdFromContext() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ForbiddenException("User is not authenticated");
        }

        try {
            return Long.valueOf(authentication.getPrincipal().toString());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Principal cannot be converted to Long: " + authentication.getPrincipal(), e);
        }
    }
}
