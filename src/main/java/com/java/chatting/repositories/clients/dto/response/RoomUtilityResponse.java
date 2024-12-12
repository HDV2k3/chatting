package com.java.chatting.repositories.clients.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public  class RoomUtilityResponse {
    Map<String, Boolean> furnitureAvailability;
    Map<String, Boolean> amenitiesAvailability;
}