package com.java.chatting.repositories;

import com.java.chatting.dto.response.GenericApiResponse;
import com.java.chatting.repositories.clients.RoomClient;
import com.java.chatting.repositories.clients.dto.response.RoomSalePostResponse;
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
public class RoomRepository {
    RoomClient roomClient;

    public RoomSalePostResponse getRoomById(int id) {
        RoomSalePostResponse result = null;
        GenericApiResponse<RoomSalePostResponse> clientResponse = roomClient.getRoomById(id);
        if (ObjectUtils.isNotEmpty(clientResponse)) {
            result = clientResponse.getData();
        }
        return result;
    }
}
