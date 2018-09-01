package com.vishnu.aggarwal.core.constants;

/*
Created by vishnu on 2/9/18 12:16 AM
*/

import static java.lang.String.format;

public class ThymeleafUrlMapping extends UrlMapping {
    public static String createUnscheduledApiJobUri() {
        return format("%s%s%s", Admin.Api.BASE_URI, Admin.Api.Quartz.BASE_URI, Admin.Api.Quartz.CREATE_API_JOB);
    }

    public static String createScheduledApiSimpleJobUri() {
        return format("%s%s%s", Admin.Api.BASE_URI, Admin.Api.Quartz.BASE_URI, Admin.Api.Quartz.CREATE_API_JOB_SCHEDULED_SIMPLE);
    }

    public static String createScheduledApiCronJobUri() {
        return format("%s%s%s", Admin.Api.BASE_URI, Admin.Api.Quartz.BASE_URI, Admin.Api.Quartz.CREATE_API_JOB_SCHEDULED_CRON);
    }
}
