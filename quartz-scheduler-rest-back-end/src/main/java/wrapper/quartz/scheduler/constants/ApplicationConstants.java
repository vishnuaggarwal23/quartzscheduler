package wrapper.quartz.scheduler.constants;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wrapper.quartz.scheduler.util.LoggerUtility;

/**
 * The interface Application constants.
 */

public interface ApplicationConstants {

    Logger log = LoggerFactory.getLogger(ApplicationConstants.class);

    /**
     * The constant UNDERSCORE.
     */
    String UNDERSCORE = "_";

    /**
     * The interface Security constants.
     */
    interface SecurityConstants {
        /**
         * The constant X_AUTH_TOKEN.
         */
        String X_AUTH_TOKEN = "X-AUTH-TOKEN";
        String ACCESS_TOKEN = "X-ACCESS-TOKEN";
        String REFRESH_TOKEN = "X-REFRESH-TOKEN";
    }

    /**
     * The interface Message constants.
     */
    interface MessageConstants {
        /**
         * The constant INFO.
         */
        String INFO = "info";
        /**
         * The constant ERROR.
         */
        String ERROR = "error";
        /**
         * The constant SUCCESS.
         */
        String SUCCESS = "success";
        /**
         * The constant WARNING.
         */
        String WARNING = "warning";
    }

    /**
     * The interface Quartz constants.
     */
    interface QuartzConstants {
        /**
         * The constant REQUEST_URL.
         */
        String REQUEST_URL = "requestURL";
        /**
         * The constant REQUEST_TYPE.
         */
        String REQUEST_TYPE = "requestType";
        /**
         * The constant REQUEST_HEADER.
         */
        String REQUEST_HEADER = "requestHeader";

        /**
         * Build request header string.
         *
         * @param headerKey the header key
         * @return the string
         */
        static String buildRequestHeader(String headerKey) {
            return REQUEST_HEADER + UNDERSCORE + headerKey;
        }

        /**
         * Gets header key from request header.
         *
         * @param requestHeader the request header
         * @return the header key from request header
         */
        static String getHeaderKeyFromRequestHeader(String requestHeader) {
            return requestHeader.split(UNDERSCORE)[1];
        }

        /**
         * Is valid request header boolean.
         *
         * @param requestHeader the request header
         * @return the boolean
         */
        static boolean isValidRequestHeader(String requestHeader) {
            boolean valid = false;
            if (StringUtils.isBlank(requestHeader)) {
                LoggerUtility.warn(log, "Request header is empty");
            } else {
                valid = requestHeader.startsWith(REQUEST_HEADER);
            }
            return valid;
        }
    }
}