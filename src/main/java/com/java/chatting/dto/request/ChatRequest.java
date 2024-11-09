package com.java.chatting.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor

@Getter
@Setter
public class ChatRequest {
    private int id;
    private int senderId;
    private int receiverId;
    private String messageEncryptForSender;
    private String messageEncryptForReceiver;
    private String messageType;
    private String sentAt;
    private boolean isEncrypted;
    private String getFileUrl;
    private String message;


    public String getFileUrl() {
        return getFileUrl;
    }
}
