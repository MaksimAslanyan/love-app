package com.example.datinguserapispring.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum UserType {
    BOT("BOT"),
    USER("USER"),
    OWNER_BOT("OWNER_BOT");

    private final String value;

    UserType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static UserType fromValue(String value) {
        for (UserType userType : UserType.values()) {
            if (userType.value.equalsIgnoreCase(value)) {
                return userType;
            }
        }
        return null;
    }
}
