package com.example.datinguserapispring.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ParentType {
    ADMIN("ADMIN"),
    OWNER("OWNER");

    private final String value;

    ParentType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ParentType fromValue(String value) {
        for (ParentType parentType : ParentType.values()) {
            if (parentType.value.equalsIgnoreCase(value)) {
                return parentType;
            }
        }
        return null;
    }
}
