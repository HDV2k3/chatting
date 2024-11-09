package com.java.chatting.constants;

import lombok.Getter;

@Getter
public enum BucketConstants {
    BUCKET_NAME("datpt-ce669.appspot.com"),
    BUILDING_FOLDER("building/"),
    MARKETING_FOLDER("marketing/"),
    ROOM_FOLDER("room/"),
    CAROUSEL_FOLDER("carousel/"),
    MARKET_AND_TREND_FOLDER("news/"),
    REAL_ESTATE_EXPERIENCE_FOLDER("exp/"),
    INCENTIVE_FOLDER("incentive/"),
    CATEGORY_FOLDER("category/"),
    URL_FIREBASE_API("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media");
    private final String value;

    BucketConstants(String value) {
        this.value = value;
    }
}
