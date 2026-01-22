package com.nirmal.taskflow.security;

import com.nirmal.taskflow.domain.user.User;
import com.nirmal.taskflow.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.io.IOException;
import java.util.List;
import java.util.UUID;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        try{
            String token = authHeader.substring(7);
            String userId = jwtService.extractUserId(token);

            User user = userRepository.findById(UUID.fromString(userId))
                    .orElseThrow(() -> new RuntimeException("User not found"));

            var authority = new SimpleGrantedAuthority(
                    "ROLE_" + user.getRole().name()
            );

            var authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    List.of(authority)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        catch(Exception ex){
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}
