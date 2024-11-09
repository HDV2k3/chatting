package com.java.chatting.facades;

import com.java.chatting.dto.response.UserStatusResponse;
import com.java.chatting.entities.UserStatus;
import com.java.chatting.services.UserStatusService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserStatusFacade {
    UserStatusService userStatusService;

    public UserStatusResponse getUserStatus(int userId) {
        return userStatusService.getUserStatus(userId);
    }

    public UserStatusResponse updateUserStatus(int userId,  UserStatus.Status status) {
        return userStatusService.updateUserStatus(userId, status);
    }
}
