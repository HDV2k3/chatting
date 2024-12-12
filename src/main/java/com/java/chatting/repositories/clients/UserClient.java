package com.java.chatting.repositories.clients;


import com.java.chatting.configurations.security.AuthenticationRequestInterceptor;
import com.java.chatting.dto.response.GenericApiResponse;
import com.java.chatting.repositories.clients.dto.response.UserProfileResponse;
import com.java.chatting.repositories.clients.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user", url = "${service.url.user}", configuration = {AuthenticationRequestInterceptor.class})

public interface UserClient {
    @GetMapping(value = "/users/get-by-id/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    GenericApiResponse<UserProfileResponse> getProfile(
            @PathVariable int userId);
    @GetMapping(value = "/users/my-info",produces = MediaType.APPLICATION_JSON_VALUE)
    GenericApiResponse<UserResponse> getMyInfo();
}
