package com.java.chatting.services.serviceIpml;


import com.java.chatting.repositories.UserRepository;
import com.java.chatting.repositories.clients.dto.response.UserProfileResponse;
import com.java.chatting.services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Override
    public UserProfileResponse getProfile(int userId) {
        return userRepository.getUserProfile(userId);
    }
}
