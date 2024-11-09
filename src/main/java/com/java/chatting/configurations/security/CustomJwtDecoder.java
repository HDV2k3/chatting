package com.java.chatting.configurations.security;

import com.java.chatting.exception.AppException;
import com.java.chatting.exception.ErrorCode;
import com.nimbusds.jwt.SignedJWT;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.text.ParseException;

/**
 * CustomJwtDecoder is a custom implementation of the JwtDecoder interface.
 * It is responsible for parsing and decoding JWT tokens and extracting their claims.
 */
@Component
public class CustomJwtDecoder implements JwtDecoder {

    /**
     * Decodes a JWT token by parsing it into a SignedJWT and extracting claims from it.
     *
     * @param token the JWT token as a string
     * @return Jwt a decoded Jwt object containing the token's claims and metadata
     * @throws JwtException if the token is invalid or cannot be parsed
     */
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            // Parse the token into a SignedJWT object to extract claims
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Return a Jwt object containing the token, issue time, expiration time, header, and claims
            return new Jwt(
                    token,
                    signedJWT.getJWTClaimsSet().getIssueTime().toInstant(),
                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(),
                    signedJWT.getHeader().toJSONObject(),
                    signedJWT.getJWTClaimsSet().getClaims()
            );
        } catch (ParseException e) {
            // Throw a JwtException if the token is invalid or cannot be parsed
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }
}
