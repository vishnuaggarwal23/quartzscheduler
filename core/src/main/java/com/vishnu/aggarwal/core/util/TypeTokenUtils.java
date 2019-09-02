package com.vishnu.aggarwal.core.util;

/*
Created by vishnu on 1/9/18 12:14 AM
*/

import com.google.gson.reflect.TypeToken;
import com.vishnu.aggarwal.core.co.JobDetailsCO;
import com.vishnu.aggarwal.core.co.QuartzDetailsCO;
import com.vishnu.aggarwal.core.co.TriggerDetailsCO;
import com.vishnu.aggarwal.core.dto.*;
import com.vishnu.aggarwal.core.vo.DataTableVO;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
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

    public static Type getHashMapOfStringAndDashboardDTO() {
        return new TypeToken<HashMap<String, DashboardDTO>>() {
        }.getType();
    }

    public static Type getHashMapOfStringAndString() {
        return new TypeToken<HashMap<String, String>>() {
        }.getType();
    }

    public static Type getHashMapOfStringAndObject() {
        return new TypeToken<HashMap<String, Object>>() {
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

    public static Type getHashMapOfStringAndQuartzDetailsCO() {
        return new TypeToken<HashMap<String, QuartzDetailsCO>>() {
        }.getType();
    }

    public static Type getHashMapOfStringAndListKeyGroupDescriptionDTO() {
        return new TypeToken<HashMap<String, List<KeyGroupDescriptionDTO>>>() {
        }.getType();
    }
}
