package com.vishnu.aggarwal.core.constants;

/*
Created by vishnu on 2/9/18 12:16 AM
*/

import com.vishnu.aggarwal.core.constants.UrlMapping.Admin;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static com.vishnu.aggarwal.core.enums.JobType.API;
import static java.lang.String.format;

public class ThymeleafUrlMapping extends Admin {
    public static String createUnscheduledApiJobUri() {
        return format("%s%s%s", Api.BASE_URI, Api.Quartz.BASE_URI, Api.Quartz.CREATE_API_JOB);
    }

    public static String createScheduledApiSimpleJobUri() {
        return format("%s%s%s", Api.BASE_URI, Api.Quartz.BASE_URI, Api.Quartz.CREATE_API_JOB_SCHEDULED_SIMPLE);
    }

    public static String createScheduledApiCronJobUri() {
        return format("%s%s%s", Api.BASE_URI, Api.Quartz.BASE_URI, Api.Quartz.CREATE_API_JOB_SCHEDULED_CRON);
    }

    public static String currentLoggedInUserUri() {
        return format("%s%s%s", Api.BASE_URI, Api.User.BASE_URI, Api.User.CURRENT_LOGGED_IN_USER);
    }

    public static String dashboardUri() {
        return format("%s%s%s", Web.BASE_URI, Web.User.BASE_URI, Web.User.USER_DASHBOARD);
    }

    public static String createApiJobUri() {
        return format("%s%s%s%s%s%s%s", Web.BASE_URI, Web.Quartz.BASE_URI, Web.Quartz.CREATE_JOB, QUESTION_MARK, TYPE, EQUAL_TO, API);
    }

    public static String listJobsUri() {
        return format("%s%s%s", Web.BASE_URI, Web.Quartz.BASE_URI, Web.Quartz.LIST_JOBS);
    }
}
