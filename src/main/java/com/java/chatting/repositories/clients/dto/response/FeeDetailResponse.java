package com.java.chatting.repositories.clients.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public  class FeeDetailResponse {
    String type;
    BigDecimal amount;
}
