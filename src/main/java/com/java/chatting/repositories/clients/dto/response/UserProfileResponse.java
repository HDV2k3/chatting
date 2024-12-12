package com.java.chatting.repositories.clients.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    int id;
    String email;
    String firstName;
    String lastName;
    String avatar;
    Instant dob;
    Set<RoleResponse> roles;
}
