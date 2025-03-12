package com.seven.morningstar.config.security;

import com.seven.morningstar.user_management.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;
    Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                try {
                    Claims claims = jwtService.extractClaims(token);
                    String username = claims.getSubject();
                    if (username != null) {
                        var user = userService.loadUserByUsername(username);
                        if (jwtService.isTokenValid(claims)) {
                            request.setAttribute("subject", username);
                            request.setAttribute("role", claims.get("role"));
                            request.setAttribute("privileges", claims.get("privileges"));

                            log.info("JWT Token Valid");
                            UsernamePasswordAuthenticationToken authenticationToken =
                                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }else log.error("JWT token is not valid");
                    } else log.error("Username is null");
                } catch (Exception exception) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, exception.getMessage());
                }
            }else log.warn("No JWT token available");
        }
        filterChain.doFilter(request, response);
    }
}
