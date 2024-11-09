package com.java.chatting.controller.helper;

import com.java.chatting.constants.MessageStatus;
import com.java.chatting.dto.request.StatusRequest;
import com.java.chatting.dto.response.EncryptionKeyResponse;
import com.java.chatting.facades.EncryptionFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatHelper {
    private final EncryptionFacade encryptionFacade;
    private final SimpMessagingTemplate messagingTemplate;
    public String retrievePublicKey(int userId) {
        return encryptionFacade.getPublicKeyForUser(userId)
                .orElseGet(() -> generatePublicKey(userId));

    }

    public String generatePublicKey(int userId) {
        try {
            EncryptionKeyResponse newKey = encryptionFacade.generateKeysForUser(userId);
            return newKey.getPublicKey();
        } catch (NoSuchAlgorithmException e) {
            log.error("Error generating public key for user {}", userId, e);
            return null; // Handle according to your application's needs
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String encryptMessage(String message, String publicKey, boolean isReceiver) throws Exception {
        return isReceiver
                ? encryptionFacade.encryptMessageForReceiver(message, publicKey)
                : encryptionFacade.encryptMessageForSender(message, publicKey);
    }

    public void notifyMessageStatusUpdate(int chatId, MessageStatus status) {
        StatusRequest returnStatus = new StatusRequest(chatId, status.toString());
        String messageStatusTopic = String.format("/topic/message-status-%d", chatId);
        messagingTemplate.convertAndSend(messageStatusTopic, returnStatus);
    }
}
