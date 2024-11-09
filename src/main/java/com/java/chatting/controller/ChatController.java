package com.java.chatting.controller;
import com.java.chatting.constants.MessageStatus;
import com.java.chatting.controller.helper.ChatHelper;
import com.java.chatting.dto.request.ChatRequest;
import com.java.chatting.dto.request.TypingRequest;
import com.java.chatting.dto.response.ChatHistory;
import com.java.chatting.dto.response.ChatResponse;
import com.java.chatting.dto.response.GenericApiResponse;
import com.java.chatting.entities.Chat;
import com.java.chatting.facades.ChatFacade;

import com.java.chatting.repositories.EncryptionKeyRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Tag(name = "Chatting Controller")
@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {
    ChatFacade chatFacade;
    SimpMessagingTemplate messagingTemplate;
    ChatHelper chatHelper;

    @MessageMapping("/chat.sendMessage")
    public GenericApiResponse<Void> sendMessage(@Payload ChatRequest messageReq, Principal principal) throws Exception {
        // Check if the message is null or empty
        if (messageReq.getMessage() == null || messageReq.getMessage().trim().isEmpty()) {
            return GenericApiResponse.error("Message content is required");
        }

        // Retrieve public keys for sender and receiver
        String senderPublicKey = chatHelper.retrievePublicKey(messageReq.getSenderId());
        String receiverPublicKey = chatHelper.retrievePublicKey(messageReq.getReceiverId());



        // Encrypt messages for sender and receiver
        String encryptedMessageForReceiver = chatHelper.encryptMessage(messageReq.getMessage(), receiverPublicKey, true);
        String encryptedMessageForSender = chatHelper.encryptMessage(messageReq.getMessage(), senderPublicKey, false);

        // Save the encrypted chat message
        ChatResponse chat = chatFacade.saveChat(messageReq, encryptedMessageForReceiver, encryptedMessageForSender);

        // Create a topic for the chat
        String chatTopic = String.format("/topic/private-chat-%d-%d",
                Math.min(messageReq.getSenderId(), messageReq.getReceiverId()),
                Math.max(messageReq.getSenderId(), messageReq.getReceiverId()));

        messagingTemplate.convertAndSend(chatTopic, chat);
        return GenericApiResponse.success(null);
    }

    @MessageMapping("/chat.typing")
    public GenericApiResponse<Void> handleTyping(@Payload TypingRequest typingRequest) {
        String typingTopic = String.format("/topic/typing-%d-%d", typingRequest.getReceiverId(), typingRequest.getSenderId());
        messagingTemplate.convertAndSend(typingTopic, typingRequest);
        return GenericApiResponse.success(null);
    }

    @GetMapping("/history")
    public GenericApiResponse<List<ChatResponse>> getChatHistory(@RequestParam int senderId, @RequestParam int receiverId) {
        List<ChatResponse> chatHistory = chatFacade.getChatHistory(senderId, receiverId);
        return GenericApiResponse.success(chatHistory);
    }

    @PutMapping("/{chatId}/status")
    public GenericApiResponse<ChatResponse> updateMessageStatus(@PathVariable int chatId, @RequestParam MessageStatus status) {
        ChatResponse updatedChat = chatFacade.updateMessageStatus(chatId, status);
        chatHelper.notifyMessageStatusUpdate(updatedChat.getId(), status);
        return GenericApiResponse.success(updatedChat);
    }

    @PutMapping("/mark-delivered/{userId}")
    public GenericApiResponse<Void> markMessagesAsDelivered(@PathVariable int userId) {
        chatFacade.markMessagesAsDelivered(userId);
        return GenericApiResponse.success(null);
    }

    @GetMapping("/unread")
    public GenericApiResponse<Integer> getUnreadMessageCount(@RequestParam int userId) {
        int count = chatFacade.getUnreadMessagesCount(userId);
        return GenericApiResponse.success(count);
    }

    @GetMapping("/user-history")
    public GenericApiResponse<List<ChatHistory>> getUserChatHistory(@RequestParam int userId) {
        List<ChatHistory> chatHistory = chatFacade.getUserChatHistory(userId);
        return GenericApiResponse.success(chatHistory);
    }

}
