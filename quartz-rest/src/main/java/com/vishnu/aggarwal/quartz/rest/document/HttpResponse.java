package com.vishnu.aggarwal.quartz.rest.document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Locale;
import java.util.Map;

/*
Created by vishnu on 24/5/18 9:59 AM
*/

@Document(collection = "http_response")
@Getter
@Setter
@CommonsLog
@ToString
public class HttpResponse extends BaseDocument<String> {

    @Id
    private String id;

    private Integer status;
    private Map headers;
    private String payload;
    private Locale locale;
    private String characterEncoding;
    private String contentType;
    private String customRequestId;
}
