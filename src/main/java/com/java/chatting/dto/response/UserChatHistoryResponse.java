package com.java.chatting.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserChatHistoryResponse {

     int receiverId;
     String messageEncryptForSender;
     String messageEncryptForReceiver;
     LocalDateTime sentAt;
     String firstName;
     String lastName;
     String urlAvatar;

     // Update constructor to match the query
     public UserChatHistoryResponse(int receiverId, String messageEncryptForSender,
                                    String messageEncryptForReceiver, LocalDateTime sentAt) {
          this.receiverId = receiverId;
          this.messageEncryptForSender = messageEncryptForSender;
          this.messageEncryptForReceiver = messageEncryptForReceiver;
          this.sentAt = sentAt;
     }
}
