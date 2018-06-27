package cn.moondev.framework.provider.excel;

import cn.moondev.framework.utils.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class CellValueUtils {

    public static Object getValue(Field field, Object value) {
        Class<?> fieldType = field.getType();
        if (String.class.equals(fieldType)) {
            if (value instanceof Double) {
                return StringUtils.doubleToString(value);
            }
            return String.valueOf(value);
        } else if (Integer.class.equals(fieldType) || Integer.TYPE.equals(fieldType)) {
            try {
                return Integer.valueOf(String.valueOf(value));
            } catch (Exception e) {
                return Double.valueOf(String.valueOf(value)).intValue();
            }
        } else if (Long.class.equals(fieldType) || Long.TYPE.equals(fieldType)) {
            // 如果Excel中的值包含小数，这里尝试转换，如果转换失败的话，则不再处理
            try {
                return Long.valueOf(String.valueOf(value));
            } catch (Exception e) {
                return Double.valueOf(String.valueOf(value)).longValue();
            }
        } else if (Short.class.equals(fieldType) || Short.TYPE.equals(fieldType)) {
            // 如果Excel中的值包含小数，这里尝试转换，如果转换失败的话，则不再处理
            try {
                return Short.valueOf(String.valueOf(value));
            } catch (Exception e) {
                return Double.valueOf(String.valueOf(value)).shortValue();
            }
        } else if (Float.class.equals(fieldType) || Float.TYPE.equals(fieldType)) {
            return Float.valueOf(String.valueOf(value));
        } else if (Double.class.equals(fieldType) || Double.TYPE.equals(fieldType)) {
            return Double.valueOf(String.valueOf(value));
        } else if (Boolean.class.equals(fieldType) || Boolean.TYPE.equals(fieldType)) {
            return Boolean.valueOf(String.valueOf(value));
        } else if (BigDecimal.class.equals(fieldType)) {
            return new BigDecimal(String.valueOf(value));
        }
        return value;
    }
}
