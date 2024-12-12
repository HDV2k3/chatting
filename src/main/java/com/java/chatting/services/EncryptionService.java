package com.java.chatting.services;

import com.java.chatting.dto.request.ChatRequest;
import com.java.chatting.dto.response.ChatResponse;
import com.java.chatting.dto.response.DecryptedMessageResponse;
import com.java.chatting.dto.response.EncryptionKeyResponse;

import java.util.List;


public interface EncryptionService {
    EncryptionKeyResponse generateKeysForUser() throws Exception;
    String getPublicKeyForUser(int userId) ;
    String getPrivateKeyForUser(int userId);
    String encryptMessage(String message, String publicKeyString) throws Exception;
    String encryptMessageForSender(String message, String publicKey) throws  Exception;
    String encryptMessageForReceiver(String message, String publicKey) throws  Exception;
    String decryptMessage(String encryptedMessage, int senderId, int receiverId);
}
