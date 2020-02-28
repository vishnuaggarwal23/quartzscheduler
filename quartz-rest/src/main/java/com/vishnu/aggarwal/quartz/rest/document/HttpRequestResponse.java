package com.vishnu.aggarwal.quartz.rest.document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/*
Created by vishnu on 24/5/18 9:59 AM
*/

@Document(collection = "http_request_response")
@Getter
@Setter
@CommonsLog
@ToString
public class HttpRequestResponse extends BaseDocument<String> {

    HttpRequest httpRequest;
    HttpResponse httpResponse;
    @Id
    private String id;
}
