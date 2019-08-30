package com.vishnu.aggarwal.core.co;

import lombok.*;
import org.springframework.http.HttpMethod;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class APIJobDataCO {
    private HttpMethod requestType;
    private String requestUrl;
    private Collection<APIHeaderCO> requestHeaders;
}

