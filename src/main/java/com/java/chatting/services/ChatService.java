package com.java.chatting.services;

import com.java.chatting.constants.MessageStatus;
import com.java.chatting.dto.request.ChatRequest;
import com.java.chatting.dto.response.ChatHistory;
import com.java.chatting.dto.response.ChatResponse;

import java.util.List;

public interface ChatService {
    ChatResponse saveChat(ChatRequest request, String encryptedMessageForReceiver, String encryptedMessageForSender);

    List<ChatResponse> getChatsHistory(int senderId, int receiverId);

    ChatResponse updateChatStatus(int chatId, MessageStatus status);

    int getUnreadMessageCount(int userId);

    void  markMessagesAsDelivered(int userId);
    List<ChatHistory> getUserChatHistory(int userId);
}
