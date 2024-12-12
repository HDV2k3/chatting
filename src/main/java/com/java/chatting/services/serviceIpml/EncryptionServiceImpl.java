package com.java.chatting.services.serviceIpml;

import com.java.chatting.dto.request.ChatRequest;
import com.java.chatting.dto.request.DecryptionRequest;
import com.java.chatting.dto.response.ChatResponse;
import com.java.chatting.dto.response.DecryptedMessageResponse;
import com.java.chatting.dto.response.EncryptionKeyResponse;
import com.java.chatting.entities.EncryptionKey;
import com.java.chatting.exception.AppException;
import com.java.chatting.exception.ErrorCode;
import com.java.chatting.repositories.EncryptionKeyRepository;
import com.java.chatting.repositories.clients.UserClient;
import com.java.chatting.services.EncryptionService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EncryptionServiceImpl implements EncryptionService {

    EncryptionKeyRepository encryptionKeyRepository;
    UserClient userClient;

    @Override
    @Transactional
    public EncryptionKeyResponse generateKeysForUser() throws Exception {
        var user = userClient.getMyInfo();
        int userId =user.getData().getId();
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        if (encryptionKeyRepository.findByUserId(userId).isPresent()) {
            throw new AppException(ErrorCode.ENCRYPTION_KEY_ALREADY_EXISTS);
        }
        List<EncryptionKey> existingKeys = encryptionKeyRepository.findAllByUserId(userId);
        if (!existingKeys.isEmpty()) {
            throw new AppException(ErrorCode.ENCRYPTION_KEY_ALREADY_EXISTS);
        }
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        String publicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
        String privateKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
        EncryptionKey encryptionKey = new EncryptionKey();
        encryptionKey.setUserId(user.getData().getId());
        encryptionKey.setPublicKey(publicKey);
        encryptionKey.setPrivateKey(privateKey);
        encryptionKey.setCreatedAt(LocalDateTime.now());
        encryptionKey.setUpdatedAt(LocalDateTime.now());

        encryptionKey = encryptionKeyRepository.save(encryptionKey);
        return EncryptionKeyResponse.builder()
                .userId(encryptionKey.getUserId())
                .publicKey(encryptionKey.getPublicKey())
                .build();
    }

    public EncryptionKey generateKeysToEntityForUser(int userId) throws Exception {
        var user = userClient.getProfile(userId);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        if (encryptionKeyRepository.findByUserId(userId).isPresent()) {
            throw new AppException(ErrorCode.ENCRYPTION_KEY_ALREADY_EXISTS);
        }
        List<EncryptionKey> existingKeys = encryptionKeyRepository.findAllByUserId(userId);
        if (!existingKeys.isEmpty()) {
            throw new AppException(ErrorCode.ENCRYPTION_KEY_ALREADY_EXISTS);
        }
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        String publicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
        String privateKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
        EncryptionKey encryptionKey = new EncryptionKey();
        encryptionKey.setUserId(user.getData().getId());
        encryptionKey.setPublicKey(publicKey);
        encryptionKey.setPrivateKey(privateKey);
        encryptionKey.setCreatedAt(LocalDateTime.now());
        encryptionKey.setUpdatedAt(LocalDateTime.now());

        return encryptionKeyRepository.save(encryptionKey);
    }

    @Override
    public String getPublicKeyForUser(int userId) {
        // Kiểm tra xem người dùng đã có khóa công khai hay chưa
        var encryptionKey = encryptionKeyRepository.findByUserId(userId)
                .orElseGet(() -> {
                    EncryptionKey newKey = null;
                    try {
                        newKey = generateKeysToEntityForUser(userId);
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return newKey;
                });

        return encryptionKey.getPublicKey();
    }


    @Override
    public String getPrivateKeyForUser(int userId) {
        var user = encryptionKeyRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return user.getPrivateKey();
    }

    @Override
    public String encryptMessage(String message, String publicKey) throws Exception {

        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // Mã hóa tin nhắn
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedBytes = cipher.doFinal(messageBytes);
        String encryptedMessage = Base64.getEncoder().encodeToString(encryptedBytes);
        return encryptedMessage;
    }


    @Override
    public String encryptMessageForSender(String message, String publicKey) throws Exception {
        return encryptMessage(message, publicKey);
    }

    @Override
    public String encryptMessageForReceiver(String message, String publicKey) throws Exception {
        return encryptMessage(message, publicKey);
    }

    @Override
    public String decryptMessage(String encryptedMessage, int senderId, int receiverId) {
        try {
            String senderPrivateKey = getPrivateKeyForUser(senderId);
            String receiverPrivateKey = getPrivateKeyForUser(receiverId);
            try {
                String decryptedMessageForSender = decrypt(encryptedMessage, senderPrivateKey);
                return decryptedMessageForSender;
            } catch (Exception e) {
                String decryptedMessageForReceiver = decrypt(encryptedMessage, receiverPrivateKey);
                return decryptedMessageForReceiver;
            }
        } catch (Exception e) {
            throw new AppException(ErrorCode.DECRYPTION_FAILED);
        }
    }

    private String decrypt(String encryptedMessage, String privateKey) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedMessage);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes);
    }

}
