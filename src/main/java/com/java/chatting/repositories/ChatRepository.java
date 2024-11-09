package com.java.chatting.repositories;

import com.java.chatting.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    List<Chat> findBySenderIdAndReceiverIdOrderBySentAtAsc(int senderId, int receiverId);


    @Query("""
            SELECT DISTINCT userId, MAX(lastMessageTime) as lastMessageTime
            FROM (
                SELECT 
                    CASE 
                        WHEN c.senderId = :userId THEN c.receiverId 
                        ELSE c.senderId 
                    END as userId,
                    c.sentAt as lastMessageTime
                FROM Chat c 
                WHERE c.senderId = :userId OR c.receiverId = :userId
            ) as subquery
            GROUP BY userId
            ORDER BY lastMessageTime DESC
            """)
    List<Object[]> findChatHistoryUserIds(@Param("userId") int userId);

    @Query("""
            SELECT c FROM Chat c 
            WHERE (c.senderId = :user1Id AND c.receiverId = :user2Id) 
               OR (c.senderId = :user2Id AND c.receiverId = :user1Id) 
            ORDER BY c.sentAt DESC
            """)
    List<Chat> findChatsBetweenUsers(
            @Param("user1Id") int user1Id,
            @Param("user2Id") int user2Id);
}
