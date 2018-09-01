package com.vishnu.aggarwal.core.util;

/*
Created by vishnu on 1/9/18 12:14 AM
*/

import com.google.gson.reflect.TypeToken;
import com.vishnu.aggarwal.core.co.JobDetailsCO;
import com.vishnu.aggarwal.core.co.QuartzDetailsCO;
import com.vishnu.aggarwal.core.co.TriggerDetailsCO;
import com.vishnu.aggarwal.core.dto.ErrorResponseDTO;
import com.vishnu.aggarwal.core.dto.UserAuthenticationDTO;
import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.core.vo.DataTableVO;

import java.lang.reflect.Type;
import java.util.HashMap;

public class TypeTokenUtils {
    public static Type getHashMapOfStringAndErrorResponseDTO() {
        return new TypeToken<HashMap<String, ErrorResponseDTO>>() {
        }.getType();
    }

    public static Type getHashMapOfStringAndUserAuthenticationDTO() {
        return new TypeToken<HashMap<String, UserAuthenticationDTO>>() {
        }.getType();
    }

    public static Type getHashMapOfStringAndUserDTO() {
        return new TypeToken<HashMap<String, UserDTO>>() {
        }.getType();
    }

    public static Type getHashMapOfStringAndBoolean() {
        return new TypeToken<HashMap<String, Boolean>>() {
        }.getType();
    }

    public static Type getHashMapOfStringAndJobDetailsCO() {
        return new TypeToken<HashMap<String, JobDetailsCO>>() {
        }.getType();
    }

    public static Type getHashMapOfStringAndTriggerDetailsCO() {
        return new TypeToken<HashMap<String, TriggerDetailsCO>>() {
        }.getType();
    }

    public static Type getHashMapOfStringAndString() {
        return new TypeToken<HashMap<String, String>>() {
        }.getType();
    }

    public static Type getDataTableVOOfJobDetailsCO() {
        return new TypeToken<DataTableVO<JobDetailsCO>>() {
        }.getType();
    }

    public static Type getDataTableVOOfTriggerDetailsCO() {
        return new TypeToken<DataTableVO<TriggerDetailsCO>>() {
        }.getType();
    }

    public static Type getDataTableVOOfQuartzDetailsCO() {
        return new TypeToken<DataTableVO<QuartzDetailsCO>>() {
        }.getType();
    }
}
