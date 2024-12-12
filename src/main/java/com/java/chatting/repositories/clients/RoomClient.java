package com.java.chatting.repositories.clients;

import com.java.chatting.configurations.security.AuthenticationRequestInterceptor;
import com.java.chatting.dto.response.GenericApiResponse;
import com.java.chatting.repositories.clients.dto.response.RoomSalePostResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "marketing", url = "${service.url.marketing}", configuration = {AuthenticationRequestInterceptor.class})
public interface RoomClient {
    @GetMapping(value = "/marketing/post-by-id/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    GenericApiResponse<RoomSalePostResponse> getRoomById(@PathVariable("id") int id);
}
