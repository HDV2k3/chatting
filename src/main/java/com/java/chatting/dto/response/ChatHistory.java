package com.java.chatting.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatHistory {
     int userId;
     String firstName;
     String lastName;
     String avatarUrl;
     String lastMessage;
     LocalDateTime lastMessageTime;
}
