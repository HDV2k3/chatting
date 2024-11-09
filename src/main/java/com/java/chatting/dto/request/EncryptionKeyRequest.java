package com.java.chatting.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EncryptionKeyRequest {
     int userId;
     String publicKey;
     String privateKey;
}
