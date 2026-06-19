package com.celebrate.security;

import com.celebrate.entity.OwnerEntity;
import com.celebrate.entity.RiderEntity;
import com.celebrate.entity.UserEntity;
import com.celebrate.repository.OwnerRepository;
import com.celebrate.repository.RiderRepository;
import com.celebrate.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final RiderRepository riderRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);
        if (token != null) {
            if (jwtProvider.validateToken(token)) {
                String userId = jwtProvider.getUserIdFromToken(token);
                String userType = jwtProvider.getUserTypeFromToken(token);
                String email = jwtProvider.getEmailFromToken(token);

                UserPrincipal principal = buildPrincipal(userId, email, userType);
                if (principal != null) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private UserPrincipal buildPrincipal(String userId, String email, String userType) {
        return switch (userType) {
            case "USER" -> userRepository.findById(userId)
                    .map(u -> UserPrincipal.builder()
                            .id(u.getId()).email(u.getEmail())
                            .password(u.getPassword()).userType("USER").build())
                    .orElse(null);
            case "OWNER", "ADMIN", "STAFF" -> ownerRepository.findById(userId)
                    .map(o -> UserPrincipal.builder()
                            .id(o.getId()).email(o.getEmail())
                            .password(o.getPassword()).userType(userType).build())
                    .orElse(null);
            case "RIDER" -> riderRepository.findById(userId)
                    .map(r -> UserPrincipal.builder()
                            .id(r.getId()).email(r.getEmail() != null ? r.getEmail() : r.getUsername())
                            .password(r.getPassword()).userType("RIDER").build())
                    .orElse(null);
            default -> null;
        };
    }

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
