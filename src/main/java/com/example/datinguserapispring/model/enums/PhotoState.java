package com.example.datinguserapispring.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PhotoState {
    REJECT("REJECT"),
    WAITING_TO_APPROVE("WAITING_TO_APPROVE"),
    APPROVE("APPROVE");

    private final String value;

    PhotoState(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PhotoState fromValue(String value) {
        for (PhotoState photoState : PhotoState.values()) {
            if (photoState.value.equalsIgnoreCase(value)) {
                return photoState;
            }
        }
        return null;
    }
}
