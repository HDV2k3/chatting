package com.java.chatting.controller;

import com.java.chatting.constants.MessageStatus;
import com.java.chatting.controller.helper.ChatHelper;
import com.java.chatting.dto.request.ChatRequest;
import com.java.chatting.dto.request.TypingRequest;
import com.java.chatting.dto.response.ChatHistory;
import com.java.chatting.dto.response.ChatResponse;
import com.java.chatting.dto.response.GenericApiResponse;
import com.java.chatting.facades.ChatFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

@Tag(name = "Chatting Controller", description = "API cho tính năng chat và quản lý tin nhắn")
@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "bearerAuth") // Yêu cầu JWT cho tất cả các endpoint trong controller này
public class ChatController {

    ChatFacade chatFacade;
    SimpMessagingTemplate messagingTemplate;
    ChatHelper chatHelper;

    @MessageMapping("/chat.sendMessage")
    @Operation(summary = "Send a message",
            description = "Gửi tin nhắn từ người gửi đến người nhận qua WebSocket.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<Void> sendMessage(@Payload ChatRequest messageReq, Principal principal) throws Exception {
        if (messageReq.getMessage() == null || messageReq.getMessage().trim().isEmpty()) {
            return GenericApiResponse.error("Message content is required");
        }

        String senderPublicKey = chatHelper.retrievePublicKey(messageReq.getSenderId());
        String receiverPublicKey = chatHelper.retrievePublicKey(messageReq.getReceiverId());

        String encryptedMessageForReceiver = chatHelper.encryptMessage(messageReq.getMessage(), receiverPublicKey, true);
        String encryptedMessageForSender = chatHelper.encryptMessage(messageReq.getMessage(), senderPublicKey, false);

        ChatResponse chat = chatFacade.saveChat(messageReq, encryptedMessageForReceiver, encryptedMessageForSender);

        String chatTopic = String.format("/topic/private-chat-%d-%d",
                Math.min(messageReq.getSenderId(), messageReq.getReceiverId()),
                Math.max(messageReq.getSenderId(), messageReq.getReceiverId()));

        messagingTemplate.convertAndSend(chatTopic, chat);
        return GenericApiResponse.success(null);
    }

    @MessageMapping("/chat.typing")
    @Operation(summary = "Handle typing status",
            description = "Gửi trạng thái 'đang gõ' từ người gửi đến người nhận qua WebSocket.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<Void> handleTyping(@Payload TypingRequest typingRequest) {
        String typingTopic = String.format("/topic/typing-%d-%d", typingRequest.getReceiverId(), typingRequest.getSenderId());
        messagingTemplate.convertAndSend(typingTopic, typingRequest);
        return GenericApiResponse.success(null);
    }

    @PostMapping("/send-message")
    @Operation(summary = "Send message via REST to WebSocket",
            description = "API REST để gửi tin nhắn, chuyển tin nhắn này tới WebSocket.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<Void> restSendMessage(@RequestBody ChatRequest messageReq, Principal principal) throws Exception {
        return sendMessage(messageReq, principal);
    }

    @PostMapping("/typing")
    @Operation(summary = "Typing status via REST to WebSocket",
            description = "API REST để gửi trạng thái 'đang gõ', chuyển trạng thái này tới WebSocket.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<Void> restHandleTyping(@RequestBody TypingRequest typingRequest) {
        return handleTyping(typingRequest);
    }

    @GetMapping("/history")
    @Operation(summary = "Get chat history",
            description = "Lấy lịch sử trò chuyện giữa người gửi và người nhận theo ID của hai người.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<List<ChatResponse>> getChatHistory(@RequestParam int senderId, @RequestParam int receiverId) {
        List<ChatResponse> chatHistory = chatFacade.getChatHistory(senderId, receiverId);
        return GenericApiResponse.success(chatHistory);
    }

    @PutMapping("/{chatId}/status")
    @Operation(summary = "Update message status",
            description = "Cập nhật trạng thái tin nhắn theo ID của tin nhắn và trạng thái mong muốn.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<ChatResponse> updateMessageStatus(@PathVariable int chatId, @RequestParam MessageStatus status) {
        ChatResponse updatedChat = chatFacade.updateMessageStatus(chatId, status);
        chatHelper.notifyMessageStatusUpdate(updatedChat.getId(), status);
        return GenericApiResponse.success(updatedChat);
    }

    @PutMapping("/mark-delivered/{userId}")
    @Operation(summary = "Mark messages as delivered",
            description = "Đánh dấu tất cả tin nhắn gửi đến người dùng cụ thể là đã 'được nhận'.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<Void> markMessagesAsDelivered(@PathVariable int userId) {
        chatFacade.markMessagesAsDelivered(userId);
        return GenericApiResponse.success(null);
    }

    @GetMapping("/unread")
    @Operation(summary = "Get unread message count",
            description = "Lấy số lượng tin nhắn chưa đọc của người dùng dựa trên ID của người dùng.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<Integer> getUnreadMessageCount(@RequestParam int userId) {
        int count = chatFacade.getUnreadMessagesCount(userId);
        return GenericApiResponse.success(count);
    }

    @GetMapping("/user-history")
    @Operation(summary = "Get user's chat history",
            description = "Lấy toàn bộ lịch sử trò chuyện của một người dùng cụ thể.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<List<ChatHistory>> getUserChatHistory(@RequestParam int userId) {
        List<ChatHistory> chatHistory = chatFacade.getUserChatHistory(userId);
        return GenericApiResponse.success(chatHistory);
    }
}
