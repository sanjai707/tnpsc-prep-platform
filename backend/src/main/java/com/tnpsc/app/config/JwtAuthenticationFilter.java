package com.tnpsc.app.config;

import com.tnpsc.app.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.context.annotation.Lazy;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final AuthService authService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, AuthService authService) {
        this.jwtUtils = jwtUtils;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("===== DEBUG JWT FILTER =====");
        System.out.println("DEBUG ONLY - REMOVE AFTER INVESTIGATION: Request URL = " + request.getRequestURI());
        String header = request.getHeader("Authorization");
        System.out.println("DEBUG ONLY - REMOVE AFTER INVESTIGATION: Authorization header present = " + (header != null));
        System.out.println("DEBUG ONLY - REMOVE AFTER INVESTIGATION: Authorization header value = " + (header == null ? "NULL" : header.startsWith("Bearer ") ? "Bearer ****" : header));
        String token = null;
        String email = null;
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            if (jwtUtils.validateToken(token)) {
                email = jwtUtils.getUsernameFromJwt(token);
            }
        }
        System.out.println("DEBUG ONLY - REMOVE AFTER INVESTIGATION: token present = " + (token != null));
        System.out.println("DEBUG ONLY - REMOVE AFTER INVESTIGATION: extracted email = " + email);
        System.out.println("DEBUG ONLY - REMOVE AFTER INVESTIGATION: SecurityContext auth before = " + SecurityContextHolder.getContext().getAuthentication());
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = authService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println("DEBUG ONLY - REMOVE AFTER INVESTIGATION: SecurityContext auth after = " + SecurityContextHolder.getContext().getAuthentication());
        }
        filterChain.doFilter(request, response);
    }
}
