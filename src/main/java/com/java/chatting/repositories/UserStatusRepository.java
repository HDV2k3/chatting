package com.java.chatting.repositories;

import com.java.chatting.entities.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus,Integer> {
    Optional<UserStatus> findByUserId(int userId);
    Optional<UserStatus> findByUserIdAndStatus(int userId, String status);
    int countByStatus(String status);
    int countByUserIdAndStatus(int userId, String status);
}
