package com.java.chatting.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_attachments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatAttachment {

    @Id
    private int id; // Đảm bảo đồng bộ với Chat ID

    @OneToOne
    @MapsId
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Column(name = "file_url", length = 255)
    private String fileUrl;
}
