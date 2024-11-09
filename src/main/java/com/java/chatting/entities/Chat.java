package com.java.chatting.entities;

import com.java.chatting.constants.MessageType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "chats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sender_id", nullable = false, length = 36)
    private int senderId;

    @Column(name = "receiver_id", nullable = false, length = 36)
    private int receiverId;

    @Column(name = "message_encrypt_for_sender", columnDefinition = "TEXT", nullable = false)
    private String messageEncryptForSender;
    @Column(name = "message_encrypt_for_receiver", columnDefinition = "TEXT", nullable = false)
    private String messageEncryptForReceiver;

    @Column(name = "is_encrypted", nullable = false)
    private boolean isEncrypted = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false, length = 20)
    private MessageType messageType;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    // Quan hệ với ChatStatus
    @OneToOne(mappedBy = "chat", cascade = CascadeType.ALL)
    private ChatStatus chatStatus;

    // Quan hệ với ChatAttachment
    @OneToOne(mappedBy = "chat", cascade = CascadeType.ALL)
    private ChatAttachment chatAttachment;
}
