package com.vishnu.aggarwal.core.dto;

/*
Created by vishnu on 18/8/18 10:48 PM
*/

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Getter
@Setter
@ToString
public class ErrorResponseDTO {
    private Object id;
    private Date date;
    private String message;
    private List<Object> details;

    public ErrorResponseDTO(Object id, Date date, String message, List<Object> details) {
        this.id = id;
        this.date = date;
        this.message = message;
        this.details = isEmpty(details) ? null : details;
    }
}
