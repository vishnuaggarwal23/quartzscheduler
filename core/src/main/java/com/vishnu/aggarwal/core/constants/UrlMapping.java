package com.vishnu.aggarwal.core.constants;

/*
Created by vishnu on 30/3/18 9:35 AM
*/

/**
 * The type Url config.
 */
public class UrlMapping {

    public static final String ALL_URL_PREFIX = "/**";
    public static final String ALL_URL_SUFFIX = "**/";

    /**
     * The type Admin.
     */
    public static class Admin {
        /**
         * The type Web.
         */
        public static class Web {

            /**
             * The constant BASE_URI.
             */
            public static final String BASE_URI = "/web";

            /**
             * The type Quartz.
             */
            public static class Quartz {
                /**
                 * The constant BASE_URI.
                 */
                public static final String BASE_URI = "/quartz";
                /**
                 * The constant CREATE_JOB.
                 */
                public static final String CREATE_JOB = "/job/create";
                /**
                 * The constant CREATE_TRIGGER.
                 */
                public static final String CREATE_TRIGGER = "/trigger/create";

                public static final String VIEW_JOB = "/job/view";

                public static final String LIST_JOBS = "/job/list";
            }

            /**
             * The type User.
             */
            public static class User {
                /**
                 * The constant BASE_URI.
                 */
                public static final String BASE_URI = "/user";
                /**
                 * The constant USER_DASHBOARD.
                 */
                public static final String USER_DASHBOARD = "/dashboard";
                /**
                 * The constant USER_LOGIN_1.
                 */
                public static final String USER_LOGIN_1 = "/login";
                /**
                 * The constant USER_LOGIN_2.
                 */
                public static final String USER_LOGIN_2 = "/";

                /**
                 * The constant FORGOT_PASSWORD.
                 */
                public static final String FORGOT_PASSWORD = "/forgotPassword";

                public static final String LOGOUT = "/logout";
            }
        }

        /**
         * The type Api.
         */
        public static class Api {
            /**
             * The type Quartz.
             */

            public static final String BASE_URI = "/api";

            /**
             * The type Quartz.
             */
            public static class Quartz {
                /**
                 * The constant BASE_URI.
                 */
                public static final String BASE_URI = Rest.Quartz.BASE_URI;
                /**
                 * The constant CREATE_JOB.
                 */
                public static final String CREATE_API_JOB = Rest.Quartz.CREATE_API_JOB;
                /**
                 * The constant CREATE_JOB_SCHEDULED_SIMPLE.
                 */
                public static final String CREATE_API_JOB_SCHEDULED_SIMPLE = Rest.Quartz.CREATE_API_JOB_SCHEDULED_SIMPLE;
                /**
                 * The constant CREATE_JOB_SCHEDULED_CRON.
                 */
                public static final String CREATE_API_JOB_SCHEDULED_CRON = Rest.Quartz.CREATE_API_JOB_SCHEDULED_CRON;
                /**
                 * The constant UPDATE_JOB.
                 */
                public static final String UPDATE_API_JOB = Rest.Quartz.UPDATE_API_JOB;
                /**
                 * The constant CREATE_TRIGGER.
                 */
                public static final String CREATE_SIMPLE_TRIGGER = Rest.Quartz.CREATE_SIMPLE_TRIGGER;
                /**
                 * The constant CREATE_CRON_TRIGGER.
                 */
                public static final String CREATE_CRON_TRIGGER = Rest.Quartz.CREATE_CRON_TRIGGER;
                /**
                 * The constant UPDATE_TRIGGER.
                 */
                public static final String UPDATE_SIMPLE_TRIGGER = Rest.Quartz.UPDATE_SIMPLE_TRIGGER;
                /**
                 * The constant UPDATE_CRON_TRIGGER.
                 */
                public static final String UPDATE_CRON_TRIGGER = Rest.Quartz.UPDATE_CRON_TRIGGER;
                /**
                 * The constant FETCH_JOB_BY_GROUP_NAME.
                 */
                public static final String FETCH_JOBS_OF_CURRENT_USER_GROUP = Rest.Quartz.FETCH_JOBS_OF_CURRENT_USER_GROUP;
                /**
                 * The constant FETCH_TRIGGER_BY_JOB_KEY_GROUP_NAME.
                 */
                public static final String FETCH_TRIGGERS_BY_JOB_KEY_AND_CURRENT_USER_GROUP = Rest.Quartz.FETCH_TRIGGERS_BY_JOB_KEY_AND_CURRENT_USER_GROUP;
                /**
                 * The constant FETCH_QUARTZ_DETAILS_GROUP_NAME.
                 */
                public static final String FETCH_QUARTZ_DETAILS_BY_CURRENT_USER_GROUP = Rest.Quartz.FETCH_QUARTZ_DETAILS_BY_CURRENT_USER_GROUP;
                /**
                 * The constant RESUME_JOBS.
                 */
                public static final String RESUME_JOBS = Rest.Quartz.RESUME_JOBS;
                /**
                 * The constant RESUME_TRIGGERS.
                 */
                public static final String RESUME_TRIGGERS = Rest.Quartz.RESUME_TRIGGERS;
                /**
                 * The constant PAUSE_JOBS.
                 */
                public static final String PAUSE_JOBS = Rest.Quartz.PAUSE_JOBS;
                /**
                 * The constant PAUSE_TRIGGERS.
                 */
                public static final String PAUSE_TRIGGERS = Rest.Quartz.PAUSE_TRIGGERS;
                /**
                 * The constant DELETE_JOBS.
                 */
                public static final String DELETE_JOBS = Rest.Quartz.DELETE_JOBS;
                /**
                 * The constant DELETE_TRIGGERS.
                 */
                public static final String DELETE_TRIGGERS = Rest.Quartz.DELETE_TRIGGERS;

                public static final String JOB_KEYS_AUTOCOMPLETE = Rest.Quartz.JOB_KEYS_AUTOCOMPLETE;
            }

            /**
             * The type User.
             */
            public static class User {
                /**
                 * The constant BASE_URI.
                 */
                public static final String BASE_URI = Rest.User.BASE_URI;
                /**
                 * The constant AUTHENTICATE.
                 */
                public static final String AUTHENTICATE = Rest.User.AUTHENTICATE;
                /**
                 * The constant LOGIN.
                 */
                public static final String LOGIN = Rest.User.LOGIN;
                /**
                 * The constant LOGOUT.
                 */
                public static final String LOGOUT = Rest.User.LOGOUT;
                /**
                 * The constant CURRENT_LOGGED_IN_USER.
                 */
                public static final String CURRENT_LOGGED_IN_USER = Rest.User.CURRENT_LOGGED_IN_USER;
            }

            /**
             * The type Validation.
             */
            public static class Validation {
                /**
                 * The constant BASE_URI.
                 */
                public static final String BASE_URI = Rest.Validation.BASE_URI;
                /**
                 * The constant UNIQUE_JOB_KEY_PER_GROUP.
                 */
                public static final String UNIQUE_JOB_KEY_PER_GROUP = Rest.Validation.UNIQUE_JOB_KEY_PER_GROUP;
                /**
                 * The constant UNIQUE_TRIGGER_KEY_PER_GROUP.
                 */
                public static final String UNIQUE_TRIGGER_KEY_PER_GROUP = Rest.Validation.UNIQUE_TRIGGER_KEY_PER_GROUP;
            }
        }
    }

    /**
     * The type Rest.
     */
    public static class Rest {
        /**
         * The type Quartz.
         */
        public static class Quartz {
            /**
             * The constant BASE_URI.
             */
            public static final String BASE_URI = "/quartz";
            /**
             * The constant CREATE_JOB.
             */
            public static final String CREATE_API_JOB = "/api/job/create";
            /**
             * The constant CREATE_JOB_SCHEDULED_SIMPLE.
             */
            public static final String CREATE_API_JOB_SCHEDULED_SIMPLE = "/api/simple/job/create";
            /**
             * The constant CREATE_JOB_SCHEDULED_CRON.
             */
            public static final String CREATE_API_JOB_SCHEDULED_CRON = "/api/cron/job/create";
            /**
             * The constant UPDATE_JOB.
             */
            public static final String UPDATE_API_JOB = "/api/job/update";
            /**
             * The constant CREATE_TRIGGER.
             */
            public static final String CREATE_SIMPLE_TRIGGER = "/simple/trigger/create";
            /**
             * The constant CREATE_CRON_TRIGGER.
             */
            public static final String CREATE_CRON_TRIGGER = "/cron/trigger/create";
            /**
             * The constant UPDATE_TRIGGER.
             */
            public static final String UPDATE_SIMPLE_TRIGGER = "/simple/trigger/update";
            /**
             * The constant UPDATE_CRON_TRIGGER.
             */
            public static final String UPDATE_CRON_TRIGGER = "/cron/trigger/update";
            /**
             * The constant FETCH_JOB_BY_GROUP_NAME.
             */
            public static final String FETCH_JOBS_OF_CURRENT_USER_GROUP = "/job/list";
            /**
             * The constant FETCH_TRIGGER_BY_JOB_KEY_GROUP_NAME.
             */
            public static final String FETCH_TRIGGERS_BY_JOB_KEY_AND_CURRENT_USER_GROUP = "/trigger/list";
            /**
             * The constant FETCH_QUARTZ_DETAILS_GROUP_NAME.
             */
            public static final String FETCH_QUARTZ_DETAILS_BY_CURRENT_USER_GROUP = "/details/list";
            /**
             * The constant RESUME_JOBS.
             */
            public static final String RESUME_JOBS = "/job/resume";
            /**
             * The constant RESUME_TRIGGERS.
             */
            public static final String RESUME_TRIGGERS = "/trigger/resume";
            /**
             * The constant PAUSE_JOBS.
             */
            public static final String PAUSE_JOBS = "/job/pause";
            /**
             * The constant PAUSE_TRIGGERS.
             */
            public static final String PAUSE_TRIGGERS = "/trigger/pause";
            /**
             * The constant DELETE_JOBS.
             */
            public static final String DELETE_JOBS = "/job/delete";
            /**
             * The constant DELETE_TRIGGERS.
             */
            public static final String DELETE_TRIGGERS = "/trigger/delete";
            public static final String JOB_KEYS_AUTOCOMPLETE = "/autocomplete/job/keys";
        }

        /**
         * The type User.
         */
        public static class User {
            /**
             * The constant BASE_URI.
             */
            public static final String BASE_URI = "/user";
            /**
             * The constant AUTHENTICATE.
             */
            public static final String AUTHENTICATE = "/authenticate";
            /**
             * The constant LOGIN.
             */
            public static final String LOGIN = "/login";
            /**
             * The constant LOGOUT.
             */
            public static final String LOGOUT = "/logout";
            /**
             * The constant CURRENT_LOGGED_IN_USER.
             */
            public static final String CURRENT_LOGGED_IN_USER = "/current";
        }

        /**
         * The type Validation.
         */
        public static class Validation {
            /**
             * The constant BASE_URI.
             */
            public static final String BASE_URI = "/validation";
            /**
             * The constant UNIQUE_JOB_KEY_PER_GROUP.
             */
            public static final String UNIQUE_JOB_KEY_PER_GROUP = "/jobKey/unique";
            /**
             * The constant UNIQUE_TRIGGER_KEY_PER_GROUP.
             */
            public static final String UNIQUE_TRIGGER_KEY_PER_GROUP = "/triggerKey/unique";
        }

        public static class Dashboard {
            public static final String BASE_URI = "/dashboard";
            public static final String COUNTER = "/counter";
        }
    }
}
