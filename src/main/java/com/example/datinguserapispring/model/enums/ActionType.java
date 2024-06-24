package com.example.datinguserapispring.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ActionType {

    PHOTO_APPROVE("PHOTO_APPROVE"),
    PHOTO_REJECT("PHOTO_REJECT"),
    USER_BLOCK("USER_BLOCK"),
    DIALOGUE_START("DIALOGUE_START");

    private final String value;

    ActionType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ActionType fromValue(String value) {
        for (ActionType actionType : ActionType.values()) {
            if (actionType.value.equalsIgnoreCase(value)) {
                return actionType;
            }
        }
        return null;
    }

}
