package com.java.chatting.dto.response;

import com.java.chatting.entities.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatusResponse {

    private int id; // ID của trạng thái người dùng

    private int userId; // ID của người dùng

    private UserStatus.Status status; // Trạng thái của người dùng (ONLINE, OFFLINE, AWAY)

    private LocalDateTime lastOnline; // Thời gian người dùng trực tuyến cuối cùng
}
