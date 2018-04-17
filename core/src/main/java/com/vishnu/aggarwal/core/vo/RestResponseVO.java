package com.vishnu.aggarwal.core.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Rest response vo.
 *
 * @param <T> the type parameter
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestResponseVO<T> {
    private T data;
    private Integer code;
    private String message;
}
