package com.java.chatting.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GenericApiResponse<T> {

    @Schema(description = "Response code indicating the result of the API call", example = "1019000")
    @Builder.Default
    Integer responseCode = 101000;

    @Schema(description = "Data or payload returned by the API")
    T data;

    @Schema(description = "Message providing additional information about the response")
    String message;

    @JsonIgnore
    public boolean isSuccess() {
        return responseCode != null && responseCode >= 1000 && responseCode < 2000;
    }

    public static <T> GenericApiResponse<T> success(T data) {
        return GenericApiResponse.<T>builder()
                .responseCode(101000)
                .data(data)
                .message("Success")
                .build();
    }

    @SuppressWarnings("unused")
    public static <T> GenericApiResponse<T> error(String errorMessage) {
        return GenericApiResponse.<T>builder()
                .responseCode(101001)
                .data(null)
                .message(errorMessage)
                .build();
    }
}
