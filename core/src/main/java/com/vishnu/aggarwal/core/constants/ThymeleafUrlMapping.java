package com.vishnu.aggarwal.core.constants;

/*
Created by vishnu on 2/9/18 12:16 AM
*/

import com.vishnu.aggarwal.core.constants.UrlMapping.Admin;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static com.vishnu.aggarwal.core.enums.JobType.API;
import static com.vishnu.aggarwal.core.enums.ScheduleType.CRON;
import static com.vishnu.aggarwal.core.enums.ScheduleType.SIMPLE;
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

    public static String createSimpleTriggerApiUri() {
        return format("%s%s%s", Api.BASE_URI, Api.Quartz.BASE_URI, Api.Quartz.CREATE_SIMPLE_TRIGGER);
    }

    public static String createCronTriggerApiUri() {
        return format("%s%s%s", Api.BASE_URI, Api.Quartz.BASE_URI, Api.Quartz.CREATE_CRON_TRIGGER);
    }

    public static String currentLoggedInUserUri() {
        return format("%s%s%s", Api.BASE_URI, Api.User.BASE_URI, Api.User.CURRENT_LOGGED_IN_USER);
    }

    public static String listJobsApiUri() {
        return format("%s%s%s", Api.BASE_URI, Api.Quartz.BASE_URI, Api.Quartz.FETCH_JOBS_OF_CURRENT_USER_GROUP);
    }

    public static String uniqueJobKeyValidationUri() {
        return format("%s%s%s", Api.BASE_URI, Api.Validation.BASE_URI, Api.Validation.UNIQUE_JOB_KEY_PER_GROUP);
    }

    public static String uniqueTriggerKeyValidationUri() {
        return format("%s%s%s", Api.BASE_URI, Api.Validation.BASE_URI, Api.Validation.UNIQUE_TRIGGER_KEY_PER_GROUP);
    }

    public static String jobKeysAutocomplete() {
        return format("%s%s%s", Api.BASE_URI, Api.Quartz.BASE_URI, Api.Quartz.JOB_KEYS_AUTOCOMPLETE);
    }

    public static String dashboardUri() {
        return format("%s%s%s", Web.BASE_URI, Web.User.BASE_URI, Web.User.USER_DASHBOARD);
    }

    public static String createApiJobUri() {
        return format("%s%s%s%s%s%s%s", Web.BASE_URI, Web.Quartz.BASE_URI, Web.Quartz.CREATE_JOB, QUESTION_MARK, TYPE, EQUAL_TO, API);
    }

    public static String createSimpleTriggerUri() {
        return format("%s%s%s%s%s%s%s", Web.BASE_URI, Web.Quartz.BASE_URI, Web.Quartz.CREATE_TRIGGER, QUESTION_MARK, TYPE, EQUAL_TO, SIMPLE);
    }

    public static String createCronTriggerUri() {
        return format("%s%s%s%s%s%s%s", Web.BASE_URI, Web.Quartz.BASE_URI, Web.Quartz.CREATE_TRIGGER, QUESTION_MARK, TYPE, EQUAL_TO, CRON);
    }

    public static String listJobsUri() {
        return format("%s%s%s", Web.BASE_URI, Web.Quartz.BASE_URI, Web.Quartz.LIST_JOBS);
    }
}
