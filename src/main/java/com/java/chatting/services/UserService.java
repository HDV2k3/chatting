package com.java.chatting.services;


import com.java.chatting.repositories.clients.dto.response.UserProfileResponse;

public interface UserService {

    UserProfileResponse getProfile(int userId);
}
