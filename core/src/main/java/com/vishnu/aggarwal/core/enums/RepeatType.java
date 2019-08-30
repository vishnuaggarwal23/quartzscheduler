package com.vishnu.aggarwal.core.enums;

import lombok.Getter;

import java.util.List;

import static java.util.Arrays.asList;

@Getter
public enum RepeatType {
    REPEAT_BY_SECOND("Second"),
    REPEAT_BY_MINUTE("Minute"),
    REPEAT_BY_HOUR("Hour");

    private String displayText;

    RepeatType(String displayText) {
        this.displayText = displayText;
    }

    public List<RepeatType> getValues() {
        return asList(values());
    }
}
