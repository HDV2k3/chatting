package com.java.chatting.services;

import com.java.chatting.dto.response.UserStatusResponse;
import com.java.chatting.entities.UserStatus;

public interface UserStatusService {
    UserStatusResponse updateUserStatus(int userId, UserStatus.Status status);
    UserStatusResponse getUserStatus(int userId);
}
