package com.vishnu.aggarwal.core.util;

/*
Created by vishnu on 1/9/18 12:14 AM
*/

import com.google.gson.reflect.TypeToken;
import com.vishnu.aggarwal.core.co.JobDetailsCO;
import com.vishnu.aggarwal.core.co.QuartzDetailsCO;
import com.vishnu.aggarwal.core.co.TriggerDetailsCO;
import com.vishnu.aggarwal.core.dto.DashboardDTO;
import com.vishnu.aggarwal.core.dto.ErrorResponseDTO;
import com.vishnu.aggarwal.core.dto.UserAuthenticationDTO;
import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.core.vo.DataTableVO;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * The type Type token utils.
 */
public class TypeTokenUtils {
    /**
     * Gets hash map of string and error response dto.
     *
     * @return the hash map of string and error response dto
     */
    public static Type getHashMapOfStringAndErrorResponseDTO() {
        return new TypeToken<HashMap<String, ErrorResponseDTO>>() {
        }.getType();
    }

    /**
     * Gets hash map of string and user authentication dto.
     *
     * @return the hash map of string and user authentication dto
     */
    public static Type getHashMapOfStringAndUserAuthenticationDTO() {
        return new TypeToken<HashMap<String, UserAuthenticationDTO>>() {
        }.getType();
    }

    /**
     * Gets hash map of string and user dto.
     *
     * @return the hash map of string and user dto
     */
    public static Type getHashMapOfStringAndUserDTO() {
        return new TypeToken<HashMap<String, UserDTO>>() {
        }.getType();
    }

    /**
     * Gets hash map of string and boolean.
     *
     * @return the hash map of string and boolean
     */
    public static Type getHashMapOfStringAndBoolean() {
        return new TypeToken<HashMap<String, Boolean>>() {
        }.getType();
    }

    /**
     * Gets hash map of string and job details co.
     *
     * @return the hash map of string and job details co
     */
    public static Type getHashMapOfStringAndJobDetailsCO() {
        return new TypeToken<HashMap<String, JobDetailsCO>>() {
        }.getType();
    }

    /**
     * Gets hash map of string and trigger details co.
     *
     * @return the hash map of string and trigger details co
     */
    public static Type getHashMapOfStringAndTriggerDetailsCO() {
        return new TypeToken<HashMap<String, TriggerDetailsCO>>() {
        }.getType();
    }

    public static Type getHashMapOfStringAndDashboardDTO() {
        return new TypeToken<HashMap<String, DashboardDTO>>() {
        }.getType();
    }

    /**
     * Gets hash map of string and string.
     *
     * @return the hash map of string and string
     */
    public static Type getHashMapOfStringAndString() {
        return new TypeToken<HashMap<String, String>>() {
        }.getType();
    }

    /**
     * Gets data table vo of job details co.
     *
     * @return the data table vo of job details co
     */
    public static Type getDataTableVOOfJobDetailsCO() {
        return new TypeToken<DataTableVO<JobDetailsCO>>() {
        }.getType();
    }

    /**
     * Gets data table vo of trigger details co.
     *
     * @return the data table vo of trigger details co
     */
    public static Type getDataTableVOOfTriggerDetailsCO() {
        return new TypeToken<DataTableVO<TriggerDetailsCO>>() {
        }.getType();
    }

    /**
     * Gets data table vo of quartz details co.
     *
     * @return the data table vo of quartz details co
     */
    public static Type getDataTableVOOfQuartzDetailsCO() {
        return new TypeToken<DataTableVO<QuartzDetailsCO>>() {
        }.getType();
    }
}
