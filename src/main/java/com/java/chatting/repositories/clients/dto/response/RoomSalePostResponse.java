package com.java.chatting.repositories.clients.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomSalePostResponse {
    String id;
    String roomId;
    String title;
    String description;
    RoomInfoResponse roomInfo;
    RoomUtilityResponse roomUtility;
    PricingDetailsResponse pricingDetails;
    Instant availableFromDate;
    String contactInfo;
    String additionalDetails;
    String status;
    Instant createdDate;
    Instant lastModifiedDate;
    String createdBy;
    String modifiedBy;
    BigDecimal fixPrice;
    String created;
    int userId;
    String statusShow;
    int index;
    private Long remainingFeaturedTime; // Thời gian còn lại tính bằng giây
    private String remainingFeaturedTimeFormatted; // Thời gian còn lại đã format (VD: "2 giờ 30 phút")
}
