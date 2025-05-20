package com.vitorazevedo.todosimple.security;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitorazevedo.todosimple.exceptions.GlobalExceptionHandler;
import com.vitorazevedo.todosimple.models.User;

public class JWTAuthenticatorFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticatorManager;

    private JWTUtil jwtUtil;

    public JWTAuthenticatorFilter(AuthenticationManager authenticatorManager, JWTUtil jwtUtil) {
        setAuthenticationFailureHandler(new GlobalExceptionHandler());
        this.authenticatorManager = authenticatorManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            User userCredentials = new ObjectMapper().readValue(request.getInputStream(), User.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userCredentials.getUsername(), userCredentials.getPassword(), new ArrayList<>());

            Authentication authentication = this.authenticatorManager.authenticate(authToken); 
            return authentication;               
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain filterChain, Authentication authetication) throws IOException, ServletException {
            UserSpringSecurity userSpringSecurity = (UserSpringSecurity) authetication.getPrincipal();
            String username = userSpringSecurity.getUsername();
            String token = this.jwtUtil.generateToken(username);
            response.setHeader("Authorization", "Bearer " + token);
            response.setHeader("access-control-expose-headers", "Authorization");
        }

}



