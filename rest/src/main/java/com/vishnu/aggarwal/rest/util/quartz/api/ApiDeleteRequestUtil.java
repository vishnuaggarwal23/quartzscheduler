package com.vishnu.aggarwal.rest.util.quartz.api;

import lombok.extern.apachecommons.CommonsLog;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Map;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.MediaType.parseMediaType;
import static org.springframework.util.CollectionUtils.isEmpty;

@CommonsLog
public class ApiDeleteRequestUtil extends ApiRequestUtil {

    public ApiDeleteRequestUtil() {
        super(DELETE);
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        final JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        if (isEmpty(jobDataMap)) {
            this.jobExecutionException = new JobExecutionException("");
            throw this.jobExecutionException;
        }

        this.url = getRequestUrl(jobDataMap);

        final Map<String, String> headers = getHeaders(jobDataMap);

        for (String headerKey : headers.keySet()) {
            if (headerKey.equalsIgnoreCase(AUTH)) {
                this.httpHeaders.add(X_AUTH_TOKEN, headers.get(headerKey));
            } else if (headerKey.equalsIgnoreCase(CONTENT_TYPE)) {
                this.httpHeaders.setContentType(parseMediaType(headers.get(headerKey)));
            } else {
                this.httpHeaders.add(headerKey, headers.get(headerKey));
            }
        }

        constructAndSaveJobTriggerResponse(this.jobTriggerResponseRepoService, this.restTemplate, this.url, DELETE, this.httpHeaders, context);
    }
}
