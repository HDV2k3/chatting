package com.java.chatting.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DecryptedMessageResponse {
    int id;
    int senderId;
    int receiverId;
    String messageForSender;
    String messageForReceiver;
    String messageType;
    String sentAt;
}
