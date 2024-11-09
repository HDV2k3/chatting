package com.java.chatting.dto.response;

import com.java.chatting.constants.MessageType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatResponse {
    private int id;
    private int senderId;
    private int receiverId;
    private String messageEncryptForSender;
    private String messageEncryptForReceiver;
    private MessageType messageType;
    private boolean isEncrypted;
    private LocalDateTime sentAt;
    private ChatStatusResponse chatStatus;
    private ChatAttachmentResponse chatAttachment;


}
