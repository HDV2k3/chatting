package com.java.chatting.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DecryptionRequest {
    @NotNull
     String encryptedMessage;
    @NotNull
     String privateKey;
}
