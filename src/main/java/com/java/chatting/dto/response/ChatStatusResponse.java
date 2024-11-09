package com.java.chatting.dto.response;

import com.java.chatting.constants.MessageStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatStatusResponse {
    private MessageStatus status;
    private LocalDateTime deliveredAt;
    private LocalDateTime readAt;
}
