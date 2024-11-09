package com.java.chatting.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class EncryptionKeyResponse {
     int id;
     int userId;
     String publicKey;
     String privateKey;
     LocalDateTime createdAt;
     LocalDateTime updatedAt;
}
