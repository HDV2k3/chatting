package com.java.chatting.exception;

import com.java.chatting.dto.response.GenericApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler handles exceptions thrown throughout the application,
 * providing standardized error responses to clients.
 */
@ControllerAdvice
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * Handles all uncaught exceptions and provides a generic error response.
     * Logs the exception details and returns a response with a generic error code.
     *
     * @param exception the uncaught exception
     * @return ResponseEntity with a GenericApiResponse containing error details
     */

    @ExceptionHandler(MissingPathVariableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Cung cấp mã trạng thái HTTP phù hợp
    public ResponseEntity<GenericApiResponse<Void>> handleMissingPathVariable(MissingPathVariableException exception) {

        GenericApiResponse<Void> apiResponse = GenericApiResponse.<Void>builder()
                .responseCode(ErrorCode.MISSING_PATH_VARIABLE.getCode()) // Đảm bảo bạn đã định nghĩa ErrorCode này
                .message(ErrorCode.MISSING_PATH_VARIABLE.getMessage())
                .build();

        return ResponseEntity
                .status(ErrorCode.MISSING_PATH_VARIABLE.getStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericApiResponse<Void>> handleGenericException(Exception exception) {
        log.error("Unhandled exception: ", exception);

        GenericApiResponse<Void> apiResponse = GenericApiResponse.<Void>builder()
                .responseCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
                .build();

        return ResponseEntity
                .status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatusCode())
                .body(apiResponse);
    }
    /**
     * Handles application-specific exceptions (AppException) and provides
     * a detailed error response based on the error code contained in the exception.
     *
     * @param exception the application-specific exception
     * @return ResponseEntity with a GenericApiResponse containing error details
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<GenericApiResponse<Void>> handleAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        GenericApiResponse<Void> apiResponse = GenericApiResponse.<Void>builder()
                .responseCode(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }

    /**
     * Handles access denied exceptions when a user does not have permission
     * to access a resource. Provides a response with an appropriate error code.
     *
     * @return ResponseEntity with a GenericApiResponse containing error details
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<GenericApiResponse<Void>> handleAccessDeniedException() {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        GenericApiResponse<Void> apiResponse = GenericApiResponse.<Void>builder()
                .responseCode(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }

    /**
     * Handles validation exceptions thrown when method arguments are invalid.
     * Provides a response with a validation error message specifying the invalid field,
     * or a general validation error message if specific field details are not available.
     *
     * @param exception the validation exception
     * @return ResponseEntity with a GenericApiResponse containing error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericApiResponse<Void>> handleValidationException(MethodArgumentNotValidException exception) {
        String message;
        FieldError fieldError = exception.getFieldError();
        if (fieldError != null) {
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            message = String.format("Validation error on field '%s': %s", fieldName, errorMessage);
        } else {
            message = "Validation error occurred, but no specific field details are available.";
        }

        // Create and return the response
        GenericApiResponse<Void> apiResponse = GenericApiResponse.<Void>builder()
                .responseCode(ErrorCode.INVALID_KEY.getCode())
                .message(message)
                .build();

        return ResponseEntity
                .badRequest()
                .body(apiResponse);
    }

    /**
     * Handles exceptions related to unreadable or invalid request bodies.
     * Provides a response indicating that the request body could not be processed.
     *
     * @return ResponseEntity with a GenericApiResponse containing error details
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GenericApiResponse<Void>> handleHttpMessageNotReadableException() {
        GenericApiResponse<Void> apiResponse = GenericApiResponse.<Void>builder()
                .responseCode(ErrorCode.INVALID_KEY.getCode())
                .message("Request body is invalid or unreadable")
                .build();

        return ResponseEntity
                .badRequest()
                .body(apiResponse);
    }
}
