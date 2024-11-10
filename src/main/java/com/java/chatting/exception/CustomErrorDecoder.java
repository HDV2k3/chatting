package com.java.chatting.exception;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 401) {
            return new FeignAuthException("Authentication failed when calling user service");
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}