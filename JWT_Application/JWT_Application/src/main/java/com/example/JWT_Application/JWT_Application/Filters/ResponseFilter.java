package com.example.JWT_Application.JWT_Application.Filters;

import com.example.JWT_Application.JWT_Application.DTO.AuthenticationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ResponseFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        responseWrapper.copyBodyToResponse();

        if (responseWrapper.getStatus() == HttpStatus.OK.value()) {
            String responseBody = new String(responseWrapper.getContentAsByteArray());
            AuthenticationResponse authenticationResponse = new ObjectMapper().readValue(responseBody, AuthenticationResponse.class);
            String token = authenticationResponse.getToken();
            responseWrapper.setHeader("Authorization","Bearer "+token);
        }
    }

}
