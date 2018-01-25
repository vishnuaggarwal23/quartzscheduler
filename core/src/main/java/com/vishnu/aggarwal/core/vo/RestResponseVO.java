package com.vishnu.aggarwal.core.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponseVO<T> {
    private T data;
    private Integer responseCode;
    private String message;
}
