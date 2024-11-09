package com.java.chatting.repositories;


import com.java.chatting.dto.response.GenericApiResponse;
import com.java.chatting.repositories.clients.UserClient;
import com.java.chatting.repositories.clients.dto.response.UserProfileResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRepository {
    UserClient userClient;

    public UserProfileResponse getUserProfile(int userId) {
        UserProfileResponse result = null;
        GenericApiResponse<UserProfileResponse> clientResponse = userClient.getProfile(userId);

        if (ObjectUtils.isNotEmpty(clientResponse)) {
            result = clientResponse.getData();
        }
        return result;
    }
}
