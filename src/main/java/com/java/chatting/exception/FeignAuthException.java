package com.java.chatting.exception;

// Custom exception
public class FeignAuthException extends RuntimeException {
    public FeignAuthException(String message) {
        super(message);
    }
}