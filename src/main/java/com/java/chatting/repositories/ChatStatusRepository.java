package com.java.chatting.repositories;

import com.java.chatting.constants.MessageStatus;
import com.java.chatting.entities.Chat;
import com.java.chatting.entities.ChatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatStatusRepository extends JpaRepository<ChatStatus, Integer> {
    int countByReceiverIdAndStatus(int receiverId, MessageStatus status);
    List<ChatStatus> findByReceiverIdAndStatus(int userId, MessageStatus messageStatus);

}
