package com.java.chatting.controller;

import com.java.chatting.dto.response.EncryptionKeyResponse;
import com.java.chatting.dto.response.GenericApiResponse;
import com.java.chatting.dto.response.MessageResponse;
import com.java.chatting.facades.EncryptionFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Encryption Controller", description = "API cho các thao tác mã hóa và quản lý khóa mã hóa.")
@Slf4j
@RestController
@RequestMapping("/api/v1/encryption")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "bearerAuth") // Áp dụng yêu cầu xác thực JWT cho tất cả các endpoint trong controller này
public class EncryptionController {

    EncryptionFacade encryptionFacade;

    @PostMapping("/keys")
    @Operation(summary = "Generate encryption keys",
            description = "Tạo cặp khóa mã hóa cho một người dùng cụ thể theo ID.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<EncryptionKeyResponse> generateKeys(@RequestParam int userId) throws Exception {
        var key = encryptionFacade.generateKeysForUser(userId);
        return GenericApiResponse.success(key);
    }

    @GetMapping("/public-key")
    @Operation(summary = "Get public key",
            description = "Lấy khóa công khai của người dùng theo ID.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<Optional<String>> getPublicKey(@RequestParam int userId) {
        return GenericApiResponse.success(encryptionFacade.getPublicKeyForUser(userId));
    }

    @GetMapping("/private-key")
    @Operation(summary = "Get private key",
            description = "Lấy khóa riêng của người dùng theo ID. Chỉ dùng trong trường hợp đặc biệt.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<String> getPrivateKey(@RequestParam int userId) {
        var result = encryptionFacade.getPrivateKeyForUser(userId);
        return GenericApiResponse.success(result);
    }

    @GetMapping("/encrypt")
    @Operation(summary = "Encrypt a message",
            description = "Mã hóa một tin nhắn bằng khóa công khai được cung cấp.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<String> encryptMessage(@RequestParam String message, @RequestParam String publicKey) {
        try {
            String encryptedMessage = encryptionFacade.encryptMessage(message, publicKey);
            return GenericApiResponse.success(encryptedMessage);
        } catch (Exception e) {
            return GenericApiResponse.error("Encryption failed: " + e.getMessage());
        }
    }

    @PostMapping("/decrypt")
    @Operation(summary = "Decrypt a message",
            description = "Giải mã tin nhắn bằng khóa riêng của người nhận và khóa công khai của người gửi.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<String> decryptMessage(@RequestBody MessageResponse message,
                                                     @RequestParam int senderId,
                                                     @RequestParam int receiverId) throws Exception {
        var decryptedMessages = encryptionFacade.decryptMessage(message.getMessages(), senderId, receiverId);
        return GenericApiResponse.success(decryptedMessages);
    }
}
