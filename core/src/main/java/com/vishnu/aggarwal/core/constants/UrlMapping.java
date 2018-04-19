package com.vishnu.aggarwal.core.constants;

/*
Created by vishnu on 30/3/18 9:35 AM
*/

/**
 * The type Url config.
 */
public class UrlMapping {
    /**
     * The type Admin.
     */
    public static class Admin {
        /**
         * The type Web.
         */
        public static class Web {
            /**
             * The type Quartz.
             */
            public static class Quartz {
                /**
                 * The constant BASE_URI.
                 */
                public static final String BASE_URI = "/web/quartz";
                /**
                 * The constant CREATE_JOB.
                 */
                public static final String CREATE_JOB = "/job/create";
                /**
                 * The constant CREATE_TRIGGER.
                 */
                public static final String CREATE_TRIGGER = "/trigger/create";
            }

            /**
             * The type User.
             */
            public static class User {
                /**
                 * The constant BASE_URI.
                 */
                public static final String BASE_URI = "/web";
                /**
                 * The constant USER_DASHBOARD.
                 */
                public static final String USER_DASHBOARD = "/user/dashboard";
                /**
                 * The constant USER_LOGIN_1.
                 */
                public static final String USER_LOGIN_1 = "";
                /**
                 * The constant USER_LOGIN_2.
                 */
                public static final String USER_LOGIN_2 = "/";

                /**
                 * The constant FORGOT_PASSWORD.
                 */
                public static final String FORGOT_PASSWORD = "/forgotPassword";
            }
        }

        /**
         * The type Api.
         */
        public static class Api {
            /**
             * The type Quartz.
             */
            public static class Quartz {
                /**
                 * The constant BASE_URI.
                 */
                public static final String BASE_URI = "/api/quartz";
                /**
                 * The constant CREATE_JOB.
                 */
                public static final String CREATE_JOB = "/job";
                /**
                 * The constant UPDATE_JOB.
                 */
                public static final String UPDATE_JOB = "/job";
                /**
                 * The constant CREATE_TRIGGER.
                 */
                public static final String CREATE_TRIGGER = "/trigger";
                /**
                 * The constant UPDATE_TRIGGER.
                 */
                public static final String UPDATE_TRIGGER = "/trigger";
                /**
                 * The constant FETCH_JOB_BY_GROUP_NAME.
                 */
                public static final String FETCH_JOB_BY_GROUP_NAME = "/job/{groupName}";
                /**
                 * The constant FETCH_TRIGGER_BY_JOB_KEY_GROUP_NAME.
                 */
                public static final String FETCH_TRIGGER_BY_JOB_KEY_GROUP_NAME = "/trigger/{jobKeyName}/{groupName}";
                /**
                 * The constant FETCH_QUARTZ_DETAILS_GROUP_NAME.
                 */
                public static final String FETCH_QUARTZ_DETAILS_GROUP_NAME = "/details/{groupName}";
                /**
                 * The constant RESUME_JOBS.
                 */
                public static final String RESUME_JOBS = "/resume/jobs";
                /**
                 * The constant RESUME_TRIGGERS.
                 */
                public static final String RESUME_TRIGGERS = "/resume/triggers";
                /**
                 * The constant PAUSE_JOBS.
                 */
                public static final String PAUSE_JOBS = "/pause/jobs";
                /**
                 * The constant PAUSE_TRIGGERS.
                 */
                public static final String PAUSE_TRIGGERS = "/pause/triggers";
                /**
                 * The constant DELETE_JOBS.
                 */
                public static final String DELETE_JOBS = "/delete/jobs";
                /**
                 * The constant DELETE_TRIGGERS.
                 */
                public static final String DELETE_TRIGGERS = "/delete/triggers";
            }

            /**
             * The type User.
             */
            public static class User {
                /**
                 * The constant BASE_URI.
                 */
                public static final String BASE_URI = "/api/user";
                /**
                 * The constant LOGIN.
                 */
                public static final String LOGIN = "/login";
                public static final String LOGOUT = "/logout";
            }

            public static class Validation {
                public static final String BASE_URI = "/api/validation";
                public static final String UNIQUE_JOB_KEY_PER_GROUP = "/uniqueJobKey";
                public static final String UNIQUE_TRIGGER_KEY_PER_GROUP = "/uniqueTriggerKey";
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
            public static final String CREATE_JOB = "/job";
            /**
             * The constant UPDATE_JOB.
             */
            public static final String UPDATE_JOB = "/job";
            /**
             * The constant CREATE_TRIGGER.
             */
            public static final String CREATE_TRIGGER = "/trigger";
            /**
             * The constant UPDATE_TRIGGER.
             */
            public static final String UPDATE_TRIGGER = "/trigger";
            /**
             * The constant FETCH_JOB_BY_GROUP_NAME.
             */
            public static final String FETCH_JOB_BY_GROUP_NAME = "/job/{groupName}";
            /**
             * The constant FETCH_TRIGGER_BY_JOB_KEY_GROUP_NAME.
             */
            public static final String FETCH_TRIGGER_BY_JOB_KEY_GROUP_NAME = "/trigger/{jobKeyName}/{groupName}";
            /**
             * The constant FETCH_QUARTZ_DETAILS_GROUP_NAME.
             */
            public static final String FETCH_QUARTZ_DETAILS_GROUP_NAME = "/details/{groupName}";
            /**
             * The constant RESUME_JOBS.
             */
            public static final String RESUME_JOBS = "/resume/jobs";
            /**
             * The constant RESUME_TRIGGERS.
             */
            public static final String RESUME_TRIGGERS = "/resume/triggers";
            /**
             * The constant PAUSE_JOBS.
             */
            public static final String PAUSE_JOBS = "/pause/jobs";
            /**
             * The constant PAUSE_TRIGGERS.
             */
            public static final String PAUSE_TRIGGERS = "/pause/triggers";
            /**
             * The constant DELETE_JOBS.
             */
            public static final String DELETE_JOBS = "/delete/jobs";
            /**
             * The constant DELETE_TRIGGERS.
             */
            public static final String DELETE_TRIGGERS = "/delete/triggers";
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
            public static final String CURRENT_LOGGED_IN_USER = "/currentLoggedInUser";
        }

        public static class Validation {
            public static final String BASE_URI = "/validation";
            public static final String UNIQUE_JOB_KEY_PER_GROUP = "/uniqueJobKey";
            public static final String UNIQUE_TRIGGER_KEY_PER_GROUP = "/uniqueTriggerKey";
        }
    }
}
