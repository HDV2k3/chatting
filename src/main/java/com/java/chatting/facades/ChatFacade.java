package com.java.chatting.facades;

import com.java.chatting.constants.MessageStatus;
import com.java.chatting.dto.request.ChatRequest;
import com.java.chatting.dto.response.ChatHistory;
import com.java.chatting.dto.response.ChatResponse;
import com.java.chatting.dto.response.PageResponse;
import com.java.chatting.dto.response.UserChatHistoryResponse;
import com.java.chatting.entities.Chat;
import com.java.chatting.repositories.clients.dto.response.UserProfileResponse;
import com.java.chatting.services.ChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatFacade {
    ChatService chatService;

    public ChatResponse saveChat(ChatRequest request, String encryptedMessageForReceiver, String encryptedMessageForSender) {
        return chatService.saveChat(request, encryptedMessageForReceiver, encryptedMessageForSender);
    }


    public List<ChatResponse> getChatHistory(int senderId, int receiverId) {
        return chatService.getChatsHistory(senderId, receiverId);
    }

    public ChatResponse updateMessageStatus(int chatId, MessageStatus status) {
        return chatService.updateChatStatus(chatId, status);
    }

    public int getUnreadMessagesCount(int userId) {
        return chatService.getUnreadMessageCount(userId);
    }

    public void markMessagesAsDelivered(int userId) {
        chatService.markMessagesAsDelivered(userId);
    }

    public List<ChatHistory> getUserChatHistory(int userId) {
        return chatService.getUserChatHistory(userId);
    }
    public PageResponse<UserChatHistoryResponse> getUsersChatHistory(int senderId, int page, int size)
    {
        return chatService.getUsersChatHistory(senderId,page,size);
    }
    public Chat createInitialChat(int id )
    {
        return chatService.createInitialChat(id );
    }
}
