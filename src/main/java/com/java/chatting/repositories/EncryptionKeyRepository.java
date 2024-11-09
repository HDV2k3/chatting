package com.java.chatting.repositories;

import com.java.chatting.entities.EncryptionKey;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EncryptionKeyRepository extends JpaRepository<EncryptionKey,Integer> {
    Optional<EncryptionKey> findByUserId(int userId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM EncryptionKey e WHERE e.userId = :userId")
    Optional<EncryptionKey> findByUserIdWithLock(int userId);
    List<EncryptionKey> findAllByUserId(int userId);
}
