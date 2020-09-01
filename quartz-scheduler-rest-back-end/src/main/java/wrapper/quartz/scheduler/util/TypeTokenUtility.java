package wrapper.quartz.scheduler.util;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * The type Type token utility.
 */
public final class TypeTokenUtility {
    /**
     * Map of string and map of string and object type.
     *
     * @return the type
     */
    public static Type mapOfStringAndMapOfStringAndObject() {
        return new TypeToken<Map<String, Map<String, Object>>>() {
        }.getType();
    }

    /**
     * Map of string and map of string and string type.
     *
     * @return the type
     */
    public static Type mapOfStringAndMapOfStringAndString() {
        return new TypeToken<Map<String, Map<String, String>>>() {
        }.getType();
    }
}
