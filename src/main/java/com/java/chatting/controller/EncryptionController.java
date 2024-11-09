package com.java.chatting.controller;

import com.java.chatting.dto.response.EncryptionKeyResponse;
import com.java.chatting.dto.response.GenericApiResponse;
import com.java.chatting.dto.response.MessageResponse;
import com.java.chatting.facades.EncryptionFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Tag(name = "Encryption Controller")
@Slf4j
@RestController
@RequestMapping("/api/v1/encryption")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EncryptionController {
    EncryptionFacade encryptionFacade;

    @PostMapping("/keys")
    public GenericApiResponse<EncryptionKeyResponse> generateKeys(@RequestParam int userId) throws Exception {
        var key = encryptionFacade.generateKeysForUser(userId);
        return GenericApiResponse.success(key);
    }

    @GetMapping("/public-key")
    public GenericApiResponse<Optional<String>> getPublicKey(@RequestParam int userId) {
        return GenericApiResponse.success(encryptionFacade.getPublicKeyForUser(userId));
    }

    @GetMapping("/private-key")
    public GenericApiResponse<String> getPrivateKey(@RequestParam int userId) {
        var result = encryptionFacade.getPrivateKeyForUser(userId);
        return GenericApiResponse.success(result);
    }

    @GetMapping("/encrypt")
    public GenericApiResponse<String> encryptMessage(@RequestParam String message, @RequestParam String publicKey) {
        try {
            String encryptedMessage = encryptionFacade.encryptMessage(message, publicKey);
            return GenericApiResponse.success(encryptedMessage);
        } catch (Exception e) {
            return GenericApiResponse.error("Encryption failed: " + e.getMessage());
        }
    }

    @PostMapping("/decrypt")
    public GenericApiResponse<String> decryptMessage(@RequestBody MessageResponse message,
                                                                             @RequestParam int senderId, @RequestParam int receiverId) throws Exception {
        var decryptedMessages =
                encryptionFacade.decryptMessage(message.getMessages(), senderId, receiverId);
        return GenericApiResponse.success(decryptedMessages);
    }
}
