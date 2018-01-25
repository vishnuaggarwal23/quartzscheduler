package com.vishnu.aggarwal.core.enums;

import lombok.Getter;

@Getter
public enum JobType {
    API("API");

    private String key;

    JobType(String key) {
        this.key = key;
    }
}
