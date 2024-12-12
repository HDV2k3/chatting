package com.java.chatting.facades;

import com.java.chatting.dto.request.ChatRequest;
import com.java.chatting.dto.response.DecryptedMessageResponse;
import com.java.chatting.dto.response.EncryptionKeyResponse;
import com.java.chatting.services.EncryptionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EncryptionFacade {
   private final   EncryptionService encryptionService;

    public EncryptionFacade(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public EncryptionKeyResponse generateKeysForUser() throws Exception  {
        return encryptionService.generateKeysForUser();
    }

    public Optional<String> getPublicKeyForUser(int userId) {
        return encryptionService.getPublicKeyForUser(userId).describeConstable();
    }

    public String getPrivateKeyForUser(int userId) {
        return encryptionService.getPrivateKeyForUser(userId);
    }

    public String encryptMessageForSender(String message, String publicKey) throws Exception{
        return encryptionService.encryptMessageForSender(message, publicKey);
    }

    public String encryptMessageForReceiver(String message, String publicKey) throws Exception{
        return encryptionService.encryptMessageForReceiver(message, publicKey);
    }
    public String encryptMessage(String message, String publicKey) throws Exception {
        return encryptionService.encryptMessage(message, publicKey);
    }

    public String decryptMessage(String messages, int senderId, int receiverId) throws Exception {
        return encryptionService.decryptMessage(messages, senderId, receiverId);
    }
}
