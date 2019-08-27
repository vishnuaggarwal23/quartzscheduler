package com.vishnu.aggarwal.core.enums;

/*
Created by vishnu on 12/8/19 4:06 PM
*/

import lombok.Getter;

@Getter
public enum Action {
    RESUME(com.vishnu.aggarwal.core.constants.Action.RESUME, com.vishnu.aggarwal.core.constants.Action.resume),
    PAUSE(com.vishnu.aggarwal.core.constants.Action.PAUSE, com.vishnu.aggarwal.core.constants.Action.pause),
    LIST(com.vishnu.aggarwal.core.constants.Action.LIST, com.vishnu.aggarwal.core.constants.Action.list);

    private String key;
    private String urlKey;

    Action(String key, String urlKey) {
        this.key = key;
        this.urlKey = urlKey;
    }
}
