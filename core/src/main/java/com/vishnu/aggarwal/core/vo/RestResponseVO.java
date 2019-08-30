package com.vishnu.aggarwal.core.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestResponseVO<T> {
    private T data;
    private Integer code;
    private String message;
}
