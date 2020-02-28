package com.vishnu.aggarwal.quartz.core.util;

/*
Created by vishnu on 24/4/18 12:19 PM
*/

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;

/**
 * The type Date utils.
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    /**
     * Clear time date.
     *
     * @param date the date
     * @return the date
     */
    public static Date clearTime(Date date) {
        return getStart(date);
    }

    /**
     * Get start date.
     *
     * @param date the date
     * @return the date
     */
    public static Date getStart(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * Gets end.
     *
     * @param date the date
     * @return the end
     */
    public static Date getEnd(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = getInstance();
        c.setTime(date);
        c.set(HOUR_OF_DAY, 23);
        c.set(MINUTE, 59);
        c.set(SECOND, 59);
        c.set(MILLISECOND, 999);
        return c.getTime();
    }
}
