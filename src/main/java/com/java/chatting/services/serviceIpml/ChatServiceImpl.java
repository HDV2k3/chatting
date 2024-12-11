package com.java.chatting.services.serviceIpml;

import com.java.chatting.constants.MessageStatus;
import com.java.chatting.constants.MessageType;
import com.java.chatting.dto.request.ChatRequest;
import com.java.chatting.dto.response.ChatHistory;
import com.java.chatting.dto.response.ChatResponse;
import com.java.chatting.dto.response.PageResponse;
import com.java.chatting.dto.response.UserChatHistoryResponse;
import com.java.chatting.entities.Chat;
import com.java.chatting.entities.ChatAttachment;
import com.java.chatting.entities.ChatStatus;
import com.java.chatting.exception.AppException;
import com.java.chatting.exception.ErrorCode;
import com.java.chatting.mappers.ChatMapper;
import com.java.chatting.repositories.ChatAttachmentRepository;
import com.java.chatting.repositories.ChatRepository;
import com.java.chatting.repositories.ChatStatusRepository;
import com.java.chatting.repositories.UserRepository;
import com.java.chatting.repositories.clients.UserClient;
import com.java.chatting.repositories.clients.dto.response.UserProfileResponse;
import com.java.chatting.services.ChatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final UserRepository userRepository;
    private final ChatStatusRepository chatStatusRepository;
    private final ChatAttachmentRepository chatAttachmentRepository;
    private final UserClient userClient;

    @Override
    @Transactional
    public ChatResponse saveChat(ChatRequest request, String encryptedMessageForReceiver, String encryptedMessageForSender) {
        var sender = userRepository.getUserProfile(request.getSenderId());
        var receiver = userRepository.getUserProfile(request.getReceiverId());
        if (sender == null || receiver == null) throw new AppException(ErrorCode.USER_NOT_FOUND);

        Chat chatEntity = Chat.builder()
                .senderId(request.getSenderId())
                .receiverId(request.getReceiverId())
                .isEncrypted(request.isEncrypted())
                .messageEncryptForSender(encryptedMessageForSender)
                .messageEncryptForReceiver(encryptedMessageForReceiver)
                .messageType(determineMessageType(request))
                .sentAt(LocalDateTime.now())
                .build();
        chatEntity = chatRepository.save(chatEntity);

        ChatStatus chatStatus = ChatStatus.builder()
                .chat(chatEntity)
                .receiverId(request.getReceiverId())
                .userId(request.getSenderId())
                .deliveredAt(LocalDateTime.now())
                .status(MessageStatus.SENT)
                .build();
        chatStatusRepository.save(chatStatus);

        if (request.getFileUrl() != null && !request.getFileUrl().isEmpty()) {
            ChatAttachment chatAttachment = new ChatAttachment();
            chatAttachment.setChat(chatEntity);
            chatAttachment.setFileUrl(request.getFileUrl());
            chatAttachmentRepository.save(chatAttachment);
        }

        return chatMapper.entityToResponse(chatEntity);
    }

    private MessageType determineMessageType(ChatRequest request) {
        return request.getFileUrl() != null && !request.getFileUrl().isEmpty() ? MessageType.FILE : MessageType.TEXT;
    }

    @Override
    public List<ChatResponse> getChatsHistory(int senderId, int receiverId) {
        List<Chat> allMessages = new ArrayList<>();
        allMessages.addAll(chatRepository.findBySenderIdAndReceiverIdOrderBySentAtAsc(senderId, receiverId));
        allMessages.addAll(chatRepository.findBySenderIdAndReceiverIdOrderBySentAtAsc(receiverId, senderId));

        return allMessages.stream()
                .sorted(Comparator.comparing(Chat::getSentAt))
                .map(chatMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChatResponse updateChatStatus(int chatId, MessageStatus status) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new AppException(ErrorCode.CHAT_NOT_FOUND));

        ChatStatus chatStatus = chat.getChatStatus();
        if (chatStatus == null) {
            chatStatus = new ChatStatus();
            chatStatus.setChat(chat);
        }
        chatStatus.setStatus(status);
        if (status == MessageStatus.DELIVERED) {
            chatStatus.setDeliveredAt(LocalDateTime.now());
        } else if (status == MessageStatus.READ) {
            chatStatus.setReadAt(LocalDateTime.now());
        }
        chat.setChatStatus(chatStatus);
        chatRepository.save(chat);

        return chatMapper.entityToResponse(chat);
    }

    @Override
    public int getUnreadMessageCount(int userId) {
        return chatStatusRepository.countByReceiverIdAndStatus(userId, MessageStatus.SENT);
    }

    @Override
    public void markMessagesAsDelivered(int userId) {
        List<ChatStatus> undeliveredMessages = chatStatusRepository.findByReceiverIdAndStatus(userId, MessageStatus.SENT);
        for (ChatStatus chat : undeliveredMessages) {
            chat.setStatus(MessageStatus.DELIVERED);
            chat.setDeliveredAt(LocalDateTime.now());
            chatStatusRepository.save(chat);
        }
    }

    @Override
    public List<ChatHistory> getUserChatHistory(int currentUserId) {
        List<Object[]> chatHistoryData = chatRepository.findChatHistoryUserIds(currentUserId);

        List<ChatHistory> chatHistoryList = chatHistoryData.stream()
                .map(data -> {
                    int userId = ((Number) data[0]).intValue();
                    var user = userRepository.getUserProfile(userId);

                    ChatHistory dto = new ChatHistory();
                    dto.setUserId(userId);
                    dto.setFirstName(user.getFirstName());
                    dto.setLastName(user.getLastName());

                    List<Chat> lastChats = chatRepository.findChatsBetweenUsers(currentUserId, userId);
                    if (!lastChats.isEmpty()) {
                        Chat lastChat = lastChats.get(0);
                        dto.setLastMessage(lastChat.getMessageEncryptForSender());
                        dto.setLastMessageTime(lastChat.getSentAt());
                    }
                    return dto;
                })
                .collect(Collectors.toList());

        if (chatHistoryList.stream().noneMatch(history -> history.getUserId() == 1)) {
            var userAdmin = userRepository.getUserProfile(1);
            ChatHistory user17History = new ChatHistory();
            user17History.setUserId(1);
            user17History.setFirstName(userAdmin.getFirstName());
            user17History.setLastName(userAdmin.getLastName());
            user17History.setLastMessage("Chưa có tin nhắn nào");
            user17History.setLastMessageTime(null);
            chatHistoryList.add(user17History);
        }

        return chatHistoryList;
    }

    @Override
    public PageResponse<UserChatHistoryResponse> getUsersChatHistory(int senderId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<UserChatHistoryResponse> pageData = chatRepository.getUsersChatHistory(senderId, pageable);
        List<Integer> receiverIds = pageData.getContent().stream()
                .map(UserChatHistoryResponse::getReceiverId)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, UserProfileResponse> userProfiles = new HashMap<>();
        for (Integer receiverId : receiverIds) {
            var userProfileResponse = userClient.getProfile(receiverId);
            if (userProfileResponse != null && userProfileResponse.getData() != null) {
                userProfiles.put(receiverId, userProfileResponse.getData());
            }
        }
        List<UserChatHistoryResponse> responses = new ArrayList<>();
        for (UserChatHistoryResponse chatHistory : pageData.getContent()) {
            int receiverId = chatHistory.getReceiverId();
            UserProfileResponse user = userProfiles.get(receiverId);
            if (user != null) {
                UserChatHistoryResponse response = UserChatHistoryResponse.builder()
                        .receiverId(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .urlAvatar(null)
                        .messageEncryptForReceiver(chatHistory.getMessageEncryptForReceiver())
                        .messageEncryptForSender(chatHistory.getMessageEncryptForSender())
                        .sentAt(chatHistory.getSentAt())
                        .build();

                responses.add(response);
            }
        }
        return PageResponse.<UserChatHistoryResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(responses)
                .build();
    }


}
