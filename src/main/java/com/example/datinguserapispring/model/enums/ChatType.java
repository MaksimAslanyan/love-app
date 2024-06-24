package com.example.datinguserapispring.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum ChatType {
    IS_PREMIUM("IS_PREMIUM"),
    LESBIAN("LESBIAN"),
    GAY("GAY"),
    INACTIVE("INACTIVE"),
    MALE_FEMALE("MALE_FEMALE"),
    FEMALE_MALE("FEMALE_MALE"),
    NULL("NULL");

    private final String value;

    ChatType(String value) {
        this.value = value;
    }
    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ChatType fromValue(String value) {
        for (ChatType chatType : ChatType.values()) {
            if (chatType.value.equalsIgnoreCase(value)) {
                return chatType;
            }
        }
        return null;
    }

}
