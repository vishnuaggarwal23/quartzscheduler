package com.vishnu.aggarwal.core.enums;

import lombok.Getter;

/**
 * The enum Repeat type.
 */
@Getter
public enum RepeatType {
    /**
     * Repeat by second repeat type.
     */
    REPEAT_BY_SECOND("Second"),
    /**
     * Repeat by minute repeat type.
     */
    REPEAT_BY_MINUTE("Minute"),
    /**
     * Repeat by hour repeat type.
     */
    REPEAT_BY_HOUR("Hour");

    private String displayText;

    RepeatType(String displayText) {
        this.displayText = displayText;
    }
}
