package com.java.chatting.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    // Uncategorized error
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid key", HttpStatus.BAD_REQUEST),
    PUBLIC_KEY_NOT_FOUND(1002, "Public key not found", HttpStatus.NOT_FOUND),
    MESSAGE_NOT_FOUND(1003, "Message not found", HttpStatus.NOT_FOUND),
    DECRYPTION_FAILED(1004, "Decryption failed", HttpStatus.INTERNAL_SERVER_ERROR),
    // Authentication and authorization errors
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    ENCRYPTION_FAILED(1008, "Invalid public key format. Please provide a valid Base64 encoded key.", HttpStatus.INTERNAL_SERVER_ERROR),
    // Token-related errors
    INVALID_TOKEN(1011, "Token is invalid or tampered with", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN(1012, "Token has expired", HttpStatus.UNAUTHORIZED),
    TOKEN_NOT_PROVIDED(1013, "Token not provided", HttpStatus.BAD_REQUEST),
    INVALID_RESET_TOKEN(1017, "Reset password token is invalid or expired", HttpStatus.BAD_REQUEST),

    // Password reset errors
    RESET_PASSWORD_FAILED(1018, "Failed to reset password", HttpStatus.INTERNAL_SERVER_ERROR),

    // Request errors
    MISSING_CREDENTIALS(1023, "Missing credentials", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS(1024, "Invalid credentials", HttpStatus.UNAUTHORIZED),

    // Room-related errors
    ROOM_CREATION_FAILED(2001, "Room creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    ROOM_UPDATE_FAILED(2002, "Room update failed", HttpStatus.INTERNAL_SERVER_ERROR),
    ROOM_DELETION_FAILED(2003, "Room deletion failed", HttpStatus.INTERNAL_SERVER_ERROR),
    ROOM_NOT_FOUND(2004, "Room not found", HttpStatus.NOT_FOUND),
    ROOM_ALREADY_EXISTS(2005, "Room already exists", HttpStatus.BAD_REQUEST),

    // Building-related errors
    BUILDING_CREATION_FAILED(3001, "Building creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    BUILDING_UPDATE_FAILED(3002, "Building update failed", HttpStatus.INTERNAL_SERVER_ERROR),
    BUILDING_DELETION_FAILED(3003, "Building deletion failed", HttpStatus.INTERNAL_SERVER_ERROR),
    BUILDING_NOT_FOUND(3004, "Building not found", HttpStatus.NOT_FOUND),
    BUILDING_ALREADY_EXISTS(3005, "Building already exists", HttpStatus.BAD_REQUEST),

    // Post-related errors
    POST_CREATION_FAILED(4001, "Post creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    POST_UPDATE_FAILED(4002, "Post update failed", HttpStatus.INTERNAL_SERVER_ERROR),
    POST_DELETION_FAILED(4003, "Post deletion failed", HttpStatus.INTERNAL_SERVER_ERROR),
    POST_NOT_FOUND(4004, "Post not found", HttpStatus.NOT_FOUND),
    POST_ALREADY_EXISTS(4005, "Post already exists", HttpStatus.BAD_REQUEST),

    // Featured-related errors
    FEATURED_CREATION_FAILED(5001, "Featured creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    FEATURED_UPDATE_FAILED(5002, "Featured update failed", HttpStatus.INTERNAL_SERVER_ERROR),
    FEATURED_DELETION_FAILED(5003, "Featured deletion failed", HttpStatus.INTERNAL_SERVER_ERROR),
    FEATURED_NOT_FOUND(5004, "Featured not found", HttpStatus.NOT_FOUND),
    FEATURED_ALREADY_EXISTS(5005, "Featured already exists", HttpStatus.BAD_REQUEST),

    // Promotional-related errors
    PROMOTIONAL_CREATION_FAILED(6001, "Promotional creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    PROMOTIONAL_UPDATE_FAILED(6002, "Promotional update failed", HttpStatus.INTERNAL_SERVER_ERROR),
    PROMOTIONAL_DELETION_FAILED(6003, "Promotional deletion failed", HttpStatus.INTERNAL_SERVER_ERROR),
    PROMOTIONAL_NOT_FOUND(6004, "Promotional not found", HttpStatus.NOT_FOUND),
    PROMOTIONAL_ALREADY_EXISTS(6005, "Promotional already exists", HttpStatus.BAD_REQUEST),

    //  CAROUSEL-related errors
    CAROUSEL_CREATION_FAILED(6001, " CAROUSEL creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    CAROUSEL_UPDATE_FAILED(6002, " CAROUSEL update failed", HttpStatus.INTERNAL_SERVER_ERROR),
    CAROUSEL_DELETION_FAILED(6003, " CAROUSEL deletion failed", HttpStatus.INTERNAL_SERVER_ERROR),
    CAROUSEL_NOT_FOUND(6004, " CAROUSEL not found", HttpStatus.NOT_FOUND),
    CAROUSEL_ALREADY_EXISTS(6005, " CAROUSEL already exists", HttpStatus.BAD_REQUEST),

    //  NEWS-related errors
    NEWS_CREATION_FAILED(6001, " NEWS creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    NEWS_UPDATE_FAILED(6002, " NEWS update failed", HttpStatus.INTERNAL_SERVER_ERROR),
    NEWS_DELETION_FAILED(6003, " NEWS deletion failed", HttpStatus.INTERNAL_SERVER_ERROR),
    NEWS_NOT_FOUND(6004, " NEWS not found", HttpStatus.NOT_FOUND),
    NEWS_ALREADY_EXISTS(6005, " NEWS already exists", HttpStatus.BAD_REQUEST),

    //  BANNERS-related errors
    BANNER_CREATION_FAILED(6001, " NEWS creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    BANNER_UPDATE_FAILED(6002, " NEWS update failed", HttpStatus.INTERNAL_SERVER_ERROR),
    BANNER_DELETION_FAILED(6003, " NEWS deletion failed", HttpStatus.INTERNAL_SERVER_ERROR),
    BANNER_NOT_FOUND(6004, " NEWS not found", HttpStatus.NOT_FOUND),
    BANNER_ALREADY_EXISTS(6005, " NEWS already exists", HttpStatus.BAD_REQUEST),
    // CATEGORY-related errors
    CATEGORY_CREATION_FAILED(6001, " CATEGORY creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    CATEGORY_UPDATE_FAILED(6002, " CATEGORY update failed", HttpStatus.INTERNAL_SERVER_ERROR),
    CATEGORY_DELETION_FAILED(6003, " CATEGORY deletion failed", HttpStatus.INTERNAL_SERVER_ERROR),
    CATEGORY_NOT_FOUND(6004, " CATEGORY not found", HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXISTS(6005, " CATEGORY already exists", HttpStatus.BAD_REQUEST),

    // Additional errors
    SAVE_FAILED(7001, "Save failed", HttpStatus.BAD_REQUEST),
    IMAGE_NOT_FOUND(8001, "Image not found", HttpStatus.NOT_FOUND),
    FILE_UPLOAD_FAILED(8002, "File upload failed", HttpStatus.BAD_REQUEST),
    CHAT_NOT_FOUND(8003, "Chat not found", HttpStatus.NOT_FOUND),
    // user
    MISSING_PATH_VARIABLE(9001, "Required path variable is missing", HttpStatus.BAD_REQUEST),
    ENCRYPTION_KEY_ALREADY_EXISTS(9002, "Encryption key already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(9001, "User not found", HttpStatus.NOT_FOUND);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
