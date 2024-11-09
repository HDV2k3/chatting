package com.java.chatting.entities;

import com.java.chatting.constants.MessageStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatStatus {

    @Id
    private int id; // Đảm bảo đồng bộ với Chat ID

    @Column(name ="user_id")
    private int userId;

    @Column(name = "receiver_id")
    private int receiverId;
    @OneToOne
    @MapsId
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MessageStatus status;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;
}
