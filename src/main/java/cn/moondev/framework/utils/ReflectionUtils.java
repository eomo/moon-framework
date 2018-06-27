package cn.moondev.framework.utils;

import java.lang.reflect.Field;

public class ReflectionUtils {

    /**
     *
     * @param field
     * @param instance
     * @param value
     */
    public static <T> void setField(Field field, T instance, Object value) throws IllegalAccessException {
        if (field.getType().equals(String.class)) {
            field.set(instance, value);
        }
    }
}
