package wrapper.quartz.scheduler.util;

import org.slf4j.Logger;

/**
 * The type Logger utility.
 */
public final class LoggerUtility {
    /**
     * Error.
     *
     * @param logger  the logger
     * @param message the message
     */
    public static void error(Logger logger, String message) {
        error(logger, message, null);
    }

    /**
     * Error.
     *
     * @param logger    the logger
     * @param throwable the throwable
     */
    public static void error(Logger logger, Throwable throwable) {
        error(logger, throwable.getMessage(), throwable);
    }

    /**
     * Error.
     *
     * @param logger    the logger
     * @param message   the message
     * @param throwable the throwable
     */
    public static void error(Logger logger, String message, Throwable throwable) {
        if (logger.isErrorEnabled()) {
            logger.error(message, throwable);
        }
    }

    /**
     * Debug.
     *
     * @param logger  the logger
     * @param message the message
     */
    public static void debug(Logger logger, String message) {
        debug(logger, message, null);
    }

    /**
     * Debug.
     *
     * @param logger    the logger
     * @param throwable the throwable
     */
    public static void debug(Logger logger, Throwable throwable) {
        error(logger, throwable.getMessage(), throwable);
    }

    /**
     * Debug.
     *
     * @param logger    the logger
     * @param message   the message
     * @param throwable the throwable
     */
    public static void debug(Logger logger, String message, Throwable throwable) {
        if (logger.isErrorEnabled()) {
            logger.debug(message, throwable);
        }
    }

    /**
     * Warn.
     *
     * @param logger  the logger
     * @param message the message
     */
    public static void warn(Logger logger, String message) {
        warn(logger, message, null);
    }

    /**
     * Warn.
     *
     * @param logger    the logger
     * @param throwable the throwable
     */
    public static void warn(Logger logger, Throwable throwable) {
        error(logger, throwable.getMessage(), throwable);
    }

    /**
     * Warn.
     *
     * @param logger    the logger
     * @param message   the message
     * @param throwable the throwable
     */
    public static void warn(Logger logger, String message, Throwable throwable) {
        if (logger.isErrorEnabled()) {
            logger.warn(message, throwable);
        }
    }

    /**
     * Info.
     *
     * @param logger  the logger
     * @param message the message
     */
    public static void info(Logger logger, String message) {
        info(logger, message, null);
    }

    /**
     * Info.
     *
     * @param logger    the logger
     * @param throwable the throwable
     */
    public static void info(Logger logger, Throwable throwable) {
        error(logger, throwable.getMessage(), throwable);
    }

    /**
     * Info.
     *
     * @param logger    the logger
     * @param message   the message
     * @param throwable the throwable
     */
    public static void info(Logger logger, String message, Throwable throwable) {
        if (logger.isErrorEnabled()) {
            logger.info(message, throwable);
        }
    }

    /**
     * Trace.
     *
     * @param logger  the logger
     * @param message the message
     */
    public static void trace(Logger logger, String message) {
        trace(logger, message, null);
    }

    /**
     * Trace.
     *
     * @param logger    the logger
     * @param throwable the throwable
     */
    public static void trace(Logger logger, Throwable throwable) {
        error(logger, throwable.getMessage(), throwable);
    }

    /**
     * Trace.
     *
     * @param logger    the logger
     * @param message   the message
     * @param throwable the throwable
     */
    public static void trace(Logger logger, String message, Throwable throwable) {
        if (logger.isErrorEnabled()) {
            logger.trace(message, throwable);
        }
    }
}