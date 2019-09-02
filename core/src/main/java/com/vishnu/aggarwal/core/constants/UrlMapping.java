package com.vishnu.aggarwal.core.constants;

/*
Created by vishnu on 30/3/18 9:35 AM
*/

import lombok.NoArgsConstructor;

import static com.vishnu.aggarwal.core.constants.Action.*;
import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UrlMapping {
    @NoArgsConstructor(access = PRIVATE)
    public static class Admin {
        @NoArgsConstructor(access = PRIVATE)
        public static class Web {
            public static final String BASE_URI = BACKSLASH + WEB;

            @NoArgsConstructor(access = PRIVATE)
            public static class Quartz {
                public static final String BASE_URI = BACKSLASH + QUARTZ;
                public static final String CREATE_JOB = BACKSLASH + JOB + BACKSLASH + create;
                public static final String CREATE_JOB_FRAGMENT = BACKSLASH + FRAGMENT + BACKSLASH + JOB + BACKSLASH + create;
                public static final String CREATE_TRIGGER = BACKSLASH + TRIGGER + BACKSLASH + create;
                public static final String CREATE_TRIGGER_FRAGMENT = BACKSLASH + FRAGMENT + BACKSLASH + TRIGGER + BACKSLASH + create;
                public static final String VIEW_JOB = BACKSLASH + JOB + BACKSLASH + view;
                public static final String LIST_JOBS = BACKSLASH + JOB + BACKSLASH + list;
            }

            @NoArgsConstructor(access = PRIVATE)
            public static class User {
                public static final String BASE_URI = BACKSLASH + USER;
                public static final String USER_DASHBOARD = BACKSLASH + DASHBOARD;
                public static final String USER_LOGIN_1 = BACKSLASH + LOGIN;
                public static final String USER_LOGIN_2 = BACKSLASH;
                public static final String FORGOT_PASSWORD = BACKSLASH + ApplicationConstants.FORGOT_PASSWORD;
                public static final String LOGOUT = BACKSLASH + ApplicationConstants.LOGOUT;
            }
        }

        @NoArgsConstructor(access = PRIVATE)
        public static class Api {
            public static final String BASE_URI = BACKSLASH + API;

            @NoArgsConstructor(access = PRIVATE)
            public static class Quartz {
                public static final String BASE_URI = Rest.Quartz.BASE_URI;
                public static final String CREATE_UPDATE_JOB = Rest.Quartz.CREATE_UPDATE_JOB;
                public static final String SHOW_JOB = Rest.Quartz.SHOW_JOB;
                public static final String DELETE_JOB = Rest.Quartz.DELETE_JOB;
                public static final String DELETE_JOBS = Rest.Quartz.DELETE_JOBS;
                public static final String RESUME_JOB = Rest.Quartz.RESUME_JOB;
                public static final String RESUME_JOBS = Rest.Quartz.RESUME_JOBS;
                public static final String PAUSE_JOB = Rest.Quartz.PAUSE_JOB;
                public static final String PAUSE_JOBS = Rest.Quartz.PAUSE_JOBS;
                public static final String LIST_JOBS = Rest.Quartz.LIST_JOBS;
                public static final String CREATE_UPDATE_TRIGGER = Rest.Quartz.CREATE_UPDATE_TRIGGER;
                public static final String SHOW_TRIGGER = Rest.Quartz.SHOW_TRIGGER;
                public static final String DELETE_TRIGGER = Rest.Quartz.DELETE_TRIGGER;
                public static final String DELETE_TRIGGERS = Rest.Quartz.DELETE_TRIGGERS;
                public static final String RESUME_TRIGGER = Rest.Quartz.RESUME_TRIGGER;
                public static final String RESUME_TRIGGERS = Rest.Quartz.RESUME_TRIGGERS;
                public static final String PAUSE_TRIGGER = Rest.Quartz.PAUSE_TRIGGER;
                public static final String PAUSE_TRIGGERS = Rest.Quartz.PAUSE_TRIGGERS;
                public static final String LIST_TRIGGERS = Rest.Quartz.LIST_TRIGGERS;
                public static final String LIST_QUARTZ_DETAILS = Rest.Quartz.LIST_QUARTZ_DETAILS;
                public static final String JOB_KEYS_AUTOCOMPLETE = Rest.Quartz.JOB_KEYS_AUTOCOMPLETE;
            }

            @NoArgsConstructor(access = PRIVATE)
            public static class User {
                public static final String BASE_URI = Rest.User.BASE_URI;
                public static final String AUTHENTICATE = Rest.User.AUTHENTICATE;
                public static final String LOGIN = Rest.User.LOGIN;
                public static final String LOGOUT = Rest.User.LOGOUT;
                public static final String CURRENT_LOGGED_IN_USER = Rest.User.CURRENT_LOGGED_IN_USER;
            }

            @NoArgsConstructor(access = PRIVATE)
            public static class Validation {
                public static final String BASE_URI = Rest.Validation.BASE_URI;
                public static final String UNIQUE_JOB_KEY_PER_GROUP = Rest.Validation.UNIQUE_JOB_KEY_PER_GROUP;
                public static final String UNIQUE_TRIGGER_KEY_PER_GROUP = Rest.Validation.UNIQUE_TRIGGER_KEY_PER_GROUP;
            }

            @NoArgsConstructor(access = PRIVATE)
            public static class Dashboard {
                public static final String BASE_URI = Rest.Dashboard.BASE_URI;
                public static final String COUNTER = Rest.Dashboard.COUNTER;
            }
        }
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Rest {
        @NoArgsConstructor(access = PRIVATE)
        public static class Quartz {
            public static final String BASE_URI = BACKSLASH + QUARTZ;
            public static final String CREATE_UPDATE_JOB = BACKSLASH + JOB;
            public static final String SHOW_JOB = BACKSLASH + JOB;
            public static final String DELETE_JOB = BACKSLASH + JOB;
            public static final String DELETE_JOBS = BACKSLASH + JOB + BACKSLASH + ALL;
            public static final String RESUME_JOB = BACKSLASH + JOB + BACKSLASH + resume;
            public static final String RESUME_JOBS = BACKSLASH + JOB + BACKSLASH + ALL + BACKSLASH + resume;
            public static final String PAUSE_JOB = BACKSLASH + JOB + BACKSLASH + pause;
            public static final String PAUSE_JOBS = BACKSLASH + JOB + BACKSLASH + ALL + BACKSLASH + pause;
            public static final String LIST_JOBS = BACKSLASH + JOB + BACKSLASH + ALL + BACKSLASH + list;
            public static final String CREATE_UPDATE_TRIGGER = BACKSLASH + TRIGGER;
            public static final String SHOW_TRIGGER = BACKSLASH + TRIGGER;
            public static final String DELETE_TRIGGER = BACKSLASH + TRIGGER;
            public static final String DELETE_TRIGGERS = BACKSLASH + TRIGGER + BACKSLASH + ALL;
            public static final String RESUME_TRIGGER = BACKSLASH + TRIGGER + BACKSLASH + resume;
            public static final String RESUME_TRIGGERS = BACKSLASH + TRIGGER + BACKSLASH + ALL + BACKSLASH + resume;
            public static final String PAUSE_TRIGGER = BACKSLASH + TRIGGER + BACKSLASH + pause;
            public static final String PAUSE_TRIGGERS = BACKSLASH + TRIGGER + BACKSLASH + ALL + BACKSLASH + pause;
            public static final String LIST_TRIGGERS = BACKSLASH + TRIGGER + BACKSLASH + ALL + BACKSLASH + list;
            public static final String LIST_QUARTZ_DETAILS = BACKSLASH + DETAILS + BACKSLASH + ALL;
            public static final String JOB_KEYS_AUTOCOMPLETE = BACKSLASH + AUTO_COMPLETE + JOB_KEY;
        }

        @NoArgsConstructor(access = PRIVATE)
        public static class User {
            public static final String BASE_URI = BACKSLASH + USER;
            public static final String AUTHENTICATE = BACKSLASH + ApplicationConstants.AUTHENTICATE;
            public static final String LOGIN = BACKSLASH + ApplicationConstants.LOGIN;
            public static final String LOGOUT = BACKSLASH + ApplicationConstants.LOGOUT;
            public static final String CURRENT_LOGGED_IN_USER = BACKSLASH + CURRENT;
        }

        @NoArgsConstructor(access = PRIVATE)
        public static class Validation {
            public static final String BASE_URI = BACKSLASH + VALIDATION;
            public static final String UNIQUE_JOB_KEY_PER_GROUP = BACKSLASH + JOB_KEY + UNIQUE;
            public static final String UNIQUE_TRIGGER_KEY_PER_GROUP = BACKSLASH + TRIGGER_KEY + UNIQUE;
        }

        @NoArgsConstructor(access = PRIVATE)
        public static class Dashboard {
            public static final String BASE_URI = BACKSLASH + DASHBOARD;
            public static final String COUNTER = BACKSLASH + ApplicationConstants.COUNTER;
        }
    }
}