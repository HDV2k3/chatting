package com.java.chatting.configurations.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.java.chatting.dto.response.GenericApiResponse;
import com.java.chatting.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * JwtAuthenticationEntryPoint handles unauthenticated access attempts by returning a standardized JSON error response.
 * This is invoked when a user tries to access a protected resource without proper authentication.
 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Commences an authentication failure response.
     *
     * @param request        the HttpServletRequest that resulted in the AuthenticationException
     * @param response       the HttpServletResponse to be used to return the response
     * @param authException  the exception that caused the entry point to be invoked
     * @throws IOException if an input or output error occurs while writing the error response
     */
    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        // Define the error code for unauthenticated access
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        // Set the HTTP status code and content type for the response
        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Create a generic API response object with the error details
        GenericApiResponse<?> apiResponse = GenericApiResponse.builder()
                .data(errorCode.getCode())  // Set error code as the response data
                .message(errorCode.getMessage())  // Set error message
                .build();

        // Convert the API response object to JSON and write it to the response
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));

        // Ensure the response is flushed and sent to the client
        response.flushBuffer();
    }
}
