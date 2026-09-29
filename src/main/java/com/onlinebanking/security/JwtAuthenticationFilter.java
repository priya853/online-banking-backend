package com.onlinebanking.security;

import com.onlinebanking.entity.User;
import com.onlinebanking.repository.UserRepository;
import com.onlinebanking.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository) {

        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {


        // 1. Get Authorization Header

        String authorizationHeader =
                request.getHeader("Authorization");



        // 2. Check Bearer Token

        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }



        // 3. Extract JWT Token

        String token =
                authorizationHeader.substring(7);



        // 4. Validate JWT

        if (jwtService.isTokenValid(token)) {


            // 5. Extract Email From JWT

            String email =
                    jwtService.extractEmail(token);



            // 6. Find User From Database

            User user = userRepository
                    .findByEmail(email)
                    .orElse(null);


            if (user != null) {


                // 7. Check User Status

                String status = user.getStatus();

                if ("BLOCKED".equalsIgnoreCase(status)) {

                    // Blocked user will NOT be authenticated
                    SecurityContextHolder
                            .clearContext();

                    response.setStatus(
                            HttpServletResponse.SC_FORBIDDEN
                    );

                    response.setContentType(
                            "application/json"
                    );

                    response.getWriter().write(
                            "{\"status\":403,\"message\":\"Your account is blocked. Please contact the administrator.\"}"
                    );

                    return;
                }



                // 8. Get User Role

                String role = user.getRole();

                if (role == null || role.isBlank()) {
                    role = "ROLE_USER";
                }



                // 9. Create Authentication Object

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                Collections.singletonList(
                                        new SimpleGrantedAuthority(role)
                                )
                        );



                // 10. Set Authentication

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }
        }



        // 11. Continue Filter Chain

       filterChain.doFilter(request, response);
    }
}