package com.celebrate.security;

import com.celebrate.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static UserPrincipal getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof UserPrincipal)) {
            throw new UnauthorizedException();
        }
        return (UserPrincipal) auth.getPrincipal();
    }

    public static String getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public static boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserPrincipal;
    }

    public static boolean hasRole(String role) {
        if (!isAuthenticated()) return false;
        return getCurrentUser().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

    public static void requireRole(String... roles) {
        UserPrincipal user = getCurrentUser();
        for (String role : roles) {
            if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_" + role))) return;
        }
        throw new UnauthorizedException("Access denied for user type: " + user.getUserType());
    }
}
