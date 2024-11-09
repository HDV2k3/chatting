package com.java.chatting.services.serviceIpml;

import com.java.chatting.dto.response.UserStatusResponse;
import com.java.chatting.entities.UserStatus;
import com.java.chatting.exception.AppException;
import com.java.chatting.exception.ErrorCode;
import com.java.chatting.repositories.UserStatusRepository;
import com.java.chatting.repositories.clients.UserClient;
import com.java.chatting.services.UserStatusService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserStatusServiceImpl implements UserStatusService {
    UserStatusRepository userStatusRepository;
    UserClient userClient;
    @Override
    public UserStatusResponse updateUserStatus(int userId, UserStatus.Status status) {
        UserStatus existingStatus = userStatusRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        var user = userClient.getProfile(userId);
    if (user == null) {
        throw new AppException(ErrorCode.USER_NOT_FOUND);
    }
        UserStatus userStatus;


        if (existingStatus != null) {
            userStatus = existingStatus;
        } else {
            userStatus = new UserStatus();
            userStatus.setUserId(user.getData().getId());
        }


        userStatus.setStatus(status);
        userStatus.setLastOnline(LocalDateTime.now());
        userStatus = userStatusRepository.save(userStatus);
        return UserStatusResponse.builder().
        id(userStatus.getId()).
        userId(userStatus.getUserId()).
        status(userStatus.getStatus()).
        lastOnline(userStatus.getLastOnline()).
                build();
    }

    @Override
    public UserStatusResponse getUserStatus(int userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return UserStatusResponse.builder().
        id(userStatus.getId()).
        userId(userStatus.getUserId()).
        status(userStatus.getStatus()).
        lastOnline(userStatus.getLastOnline()).
                build();
    }
}
