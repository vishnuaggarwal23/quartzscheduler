package com.vishnu.aggarwal.core.constants;

/*
Created by vishnu on 2/9/18 12:16 AM
*/

import com.vishnu.aggarwal.core.constants.UrlMapping.Admin;
import com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Web.Quartz;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static com.vishnu.aggarwal.core.enums.ScheduleType.CRON;
import static com.vishnu.aggarwal.core.enums.ScheduleType.SIMPLE;
import static java.lang.String.format;

public class ThymeleafUrlMapping extends Admin {

    private static String getValidationApiUrl(String uriSuffix) {
        return format("%s%s%s", Admin.Api.BASE_URI, Admin.Api.Validation.BASE_URI, uriSuffix);
    }

    public static final String uniqueJobKeyApiUri() {
        return getValidationApiUrl(Admin.Api.Validation.UNIQUE_JOB_KEY_PER_GROUP);
    }

    public static final String uniqueTriggerKeyApiUri() {
        return getValidationApiUrl(Admin.Api.Validation.UNIQUE_TRIGGER_KEY_PER_GROUP);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static String getQuartzApiUri(String uriSuffix) {
        return format("%s%s%s", Admin.Api.BASE_URI, Admin.Api.Quartz.BASE_URI, uriSuffix);
    }

    public static final String createOrUpdateJobUri() {
        return getQuartzApiUri(Admin.Api.Quartz.CREATE_UPDATE_JOB);
    }

    public static final String showJobUri() {
        return getQuartzApiUri(Admin.Api.Quartz.SHOW_JOB);
    }

    public static final String deleteJobUri() {
        return getQuartzApiUri(Admin.Api.Quartz.DELETE_JOB);
    }

    public static final String deleteJobsUri() {
        return getQuartzApiUri(Admin.Api.Quartz.DELETE_JOBS);
    }

    public static final String resumeJobUri() {
        return getQuartzApiUri(Admin.Api.Quartz.RESUME_JOB);
    }

    public static final String resumeJobsUri() {
        return getQuartzApiUri(Admin.Api.Quartz.RESUME_JOBS);
    }

    public static final String pauseJobUri() {
        return getQuartzApiUri(Admin.Api.Quartz.PAUSE_JOB);
    }

    public static final String pauseJobsUri() {
        return getQuartzApiUri(Admin.Api.Quartz.PAUSE_JOBS);
    }

    public static final String listJobsUri() {
        return getQuartzApiUri(Admin.Api.Quartz.LIST_JOBS);
    }

    public static final String createOrUpdateTriggerUri() {
        return getQuartzApiUri(Admin.Api.Quartz.CREATE_UPDATE_TRIGGER);
    }

    public static final String showTriggerUri() {
        return getQuartzApiUri(Admin.Api.Quartz.SHOW_TRIGGER);
    }

    public static final String deleteTriggerUri() {
        return getQuartzApiUri(Admin.Api.Quartz.DELETE_TRIGGER);
    }

    public static final String deleteTriggersUri() {
        return getQuartzApiUri(Admin.Api.Quartz.DELETE_TRIGGERS);
    }

    public static final String resumeTriggerUri() {
        return getQuartzApiUri(Admin.Api.Quartz.RESUME_TRIGGER);
    }

    public static final String resumeTriggersUri() {
        return getQuartzApiUri(Admin.Api.Quartz.RESUME_TRIGGERS);
    }

    public static final String pauseTriggerUri() {
        return getQuartzApiUri(Admin.Api.Quartz.PAUSE_TRIGGER);
    }

    public static final String pauseTriggersUri() {
        return getQuartzApiUri(Admin.Api.Quartz.PAUSE_TRIGGERS);
    }

    public static final String listTriggersUri() {
        return getQuartzApiUri(Admin.Api.Quartz.LIST_TRIGGERS);
    }

    public static final String listQuartzDetailsUri() {
        return getQuartzApiUri(Admin.Api.Quartz.LIST_QUARTZ_DETAILS);
    }

    public static final String jobKeyAutoCompleteUri() {
        return getQuartzApiUri(Admin.Api.Quartz.JOB_KEYS_AUTOCOMPLETE);
    }


    public static String currentLoggedInUserUri() {
        return format("%s%s%s", Admin.Api.BASE_URI, Admin.Api.User.BASE_URI, Admin.Api.User.CURRENT_LOGGED_IN_USER);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String dashboardWebUri() {
        return format("%s%s%s", Web.BASE_URI, Web.User.BASE_URI, Web.User.USER_DASHBOARD);
    }

    public static String getJobFragmentWebUri() {
        return format("%s%s%s%s%s%s%s", Web.BASE_URI, Quartz.BASE_URI, Quartz.CREATE_JOB_FRAGMENT, QUESTION_MARK, FRAGMENT, EQUAL_TO, FRAGMENTS_JOB.get(2));
    }

    public static String getJobFragmentWebUri(String fragment) {
        return format("%s%s%s%s%s%s%s", Web.BASE_URI, Quartz.BASE_URI, Quartz.CREATE_JOB_FRAGMENT, QUESTION_MARK, FRAGMENT, EQUAL_TO, fragment);
    }

    public static String getTriggerFragmentWebUri() {
        return format("%s%s%s%s%s%s%s", Web.BASE_URI, Quartz.BASE_URI, Quartz.CREATE_TRIGGER_FRAGMENT, QUESTION_MARK, FRAGMENT, EQUAL_TO, FRAGMENTS_TRIGGER.get(2));
    }

    public static String getTriggerFragmentWebUri(String fragment) {
        return format("%s%s%s%s%s%s%s", Web.BASE_URI, Quartz.BASE_URI, Quartz.CREATE_TRIGGER_FRAGMENT, QUESTION_MARK, FRAGMENT, EQUAL_TO, fragment);
    }

    public static String createJobWebUri() {
        return format("%s%s%s", Web.BASE_URI, Quartz.BASE_URI, Quartz.CREATE_JOB);
    }

    public static String createSimpleTriggerWebUri() {
        return format("%s%s%s%s%s%s%s", Web.BASE_URI, Quartz.BASE_URI, Quartz.CREATE_TRIGGER, QUESTION_MARK, TYPE, EQUAL_TO, SIMPLE);
    }

    public static String createCronTriggerWebUri() {
        return format("%s%s%s%s%s%s%s", Web.BASE_URI, Quartz.BASE_URI, Quartz.CREATE_TRIGGER, QUESTION_MARK, TYPE, EQUAL_TO, CRON);
    }

    public static String listJobsWebUri() {
        return format("%s%s%s", Web.BASE_URI, Quartz.BASE_URI, Quartz.LIST_JOBS);
    }
}
