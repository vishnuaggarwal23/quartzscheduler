package wrapper.quartz.scheduler.util;

import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Date;

/**
 * The type Date utility.
 */
public final class DateUtility {
    /**
     * Clear time date.
     *
     * @param date the date
     * @return the date
     * @throws IllegalArgumentException the illegal argument exception
     */
    public static Date clearTime(Date date) throws IllegalArgumentException {
        Assert.notNull(date, "Date cannot be null");
        return getStartDate(date);
    }

    /**
     * Gets start date.
     *
     * @param date the date
     * @return the start date
     * @throws IllegalArgumentException the illegal argument exception
     */
    public static Date getStartDate(Date date) throws IllegalArgumentException {
        Assert.notNull(date, "Date cannot be null");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * Gets end date.
     *
     * @param date the date
     * @return the end date
     * @throws IllegalArgumentException the illegal argument exception
     */
    public static Date getEndDate(Date date) throws IllegalArgumentException {
        Assert.notNull(date, "Date cannot be null");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }
}
