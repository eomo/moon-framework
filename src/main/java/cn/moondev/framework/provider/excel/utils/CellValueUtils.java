package cn.moondev.framework.provider.excel.utils;

import cn.moondev.framework.provider.excel.model.ExcelField;
import com.google.common.base.Strings;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 * 处理表格值的工具类
 */
public class CellValueUtils {

    /**
     * 获取表格的值
     */
    public static <T> Object getValue(ExcelField field, Cell cell) {
        Object cellValue = getCellValue(cell, field.field.getType(), field);
        if (Objects.nonNull(cellValue)) {
            cellValue = replaceValue(field.column.replace(), cellValue);
            return getTypeValue(field.field, cellValue);
        }
        return null;
    }

    /**
     * 将从表格中获取到的值根据JavaBean的属性类型进行转换
     *
     * @param field JavaBean实体中对象的属性
     * @param value 从表格中获取到的具体值
     * @return
     */
    public static Object getTypeValue(Field field, Object value) {
        // 未获取到表格的值，直接返回null
        if (Objects.isNull(value)) {
            return null;
        }
        Class<?> fieldType = field.getType();
        // 如果是日期类型，直接返回，因为前面已经对日期进行过转换
        if (isDateClazz(fieldType)) {
            return value;
        }
        // 字符串类型
        if (String.class.equals(fieldType)) {
            if (value instanceof Double) {
                // 处理科学计数法
                return doubleToString(value);
            }
            return String.valueOf(value);
        }
        // 整型
        if (Integer.class.equals(fieldType) || Integer.TYPE.equals(fieldType)) {
            return parseInteger(value);
        }
        // 长整型
        if (Long.class.equals(fieldType) || Long.TYPE.equals(fieldType)) {
            return parseLong(value);
        }
        // Short
        if (Short.class.equals(fieldType) || Short.TYPE.equals(fieldType)) {
            return parseShort(value);
        }
        // Float
        if (Float.class.equals(fieldType) || Float.TYPE.equals(fieldType)) {
            return Float.valueOf(String.valueOf(value));
        }
        // Double
        if (Double.class.equals(fieldType) || Double.TYPE.equals(fieldType)) {
            return Double.valueOf(String.valueOf(value));
        }
        // 布尔值
        if (Boolean.class.equals(fieldType) || Boolean.TYPE.equals(fieldType)) {
            return Boolean.valueOf(String.valueOf(value));
        }
        // BigDecimal
        if (BigDecimal.class.equals(fieldType)) {
            return new BigDecimal(String.valueOf(value));
        }
        return value;
    }


    /**
     * 处理 @ExcelColumn 的 replace 属性
     */
    private static Object replaceValue(String[] replace, Object result) {
        if (replace != null && replace.length > 0) {
            String temp = String.valueOf(result);
            String[] tempArr;
            for (int i = 0; i < replace.length; i++) {
                tempArr = replace[i].split("_");
                if (temp.equals(tempArr[0])) {
                    return tempArr[1];
                }
            }
        }
        return result;
    }

    /**
     * 获取单元格内的值
     */
    private static Object getCellValue(Cell cell, Class<?> clazz, ExcelField excelField) {
        CellType cellType = cell.getCellTypeEnum();
        // 表格内容为布尔值
        if (CellType.BOOLEAN == cellType) {
            return cell.getBooleanCellValue();
        }
        // 表格的内容为通过公式计算的结果
        if (CellType.FORMULA == cellType) {
            return getFormulaCellValue(cell);
        }
        // 表格内容为数字
        if (CellType.NUMERIC == cellType) {
            return getNumbericCellValue(cell, clazz, excelField);
        }
        // 表格内容为字符串
        if (CellType.STRING == cellType) {
            return cell.getStringCellValue();
        }
        // 其他CellType类型
        return null;
    }

    /**
     * 表格日期转换
     */
    private static Object convertCellDate(Date cellDate, Class<?> clazz) {
        if (java.sql.Date.class.equals(clazz)) {
            return new java.sql.Date(cellDate.getTime());
        }
        if (java.sql.Time.class.equals(clazz)) {
            return new Time(cellDate.getTime());
        }
        if (java.sql.Timestamp.class.equals(clazz)) {
            return new Timestamp(cellDate.getTime());
        }
        if (LocalDate.class.equals(clazz)) {
            return convertToLocalDate(cellDate);
        }
        if (LocalDateTime.class.equals(clazz)) {
            return convertToLocalDateTime(cellDate);
        }
        return cellDate;
    }

    /**
     * 获取数字类型的表格值
     */
    private static Object getNumbericCellValue(Cell cell, Class<?> clazz, ExcelField field) {
        // 如果表格内容为日期
        if (DateUtil.isCellDateFormatted(cell)) {
            // 对应的JavaBean属性也是日期类型
            if (isDateClazz(clazz)) {
                Date cellDate = DateUtil.getJavaDate(cell.getNumericCellValue());
                return convertCellDate(cellDate, clazz);
            }
            // 对应的JavaBean属性是字符串类型，则按照dateFormat配置进行格式化
            else if (String.class.equals(clazz)) {
                // 如果没有配置 dateFormat属性，则直接按照字符串返回
                if (Strings.isNullOrEmpty(field.column.dateformat())) {
                    cell.setCellType(CellType.STRING);
                    return cell.getStringCellValue();
                }
                Date cellDate = DateUtil.getJavaDate(cell.getNumericCellValue());
                return format(cellDate, field.column.dateformat());
            }
        }
        // 其他情况直接返回数字
        return cell.getNumericCellValue();
    }

    /**
     * 获取公式类型的表格值
     */
    private static Object getFormulaCellValue(Cell cell) {
        try {
            return cell.getStringCellValue();
        } catch (IllegalStateException e) {
            return cell.getNumericCellValue();
        }
    }

    /**
     * 是否是日期类型
     */
    private static boolean isDateClazz(Class<?> clazz) {
        return clazz == Date.class || clazz == java.sql.Date.class ||
                clazz == Time.class || clazz == Timestamp.class ||
                clazz == LocalDate.class || clazz == LocalDateTime.class;
    }

    /**
     * 数值转换
     */
    private static Object parseInteger(Object value) {
        // 如果Excel中的值包含小数，这里尝试转换，如果转换失败的话，则不再处理
        try {
            return Integer.valueOf(String.valueOf(value));
        } catch (Exception e) {
            return Double.valueOf(String.valueOf(value)).intValue();
        }
    }

    private static Object parseLong(Object value) {
        try {
            return Long.valueOf(String.valueOf(value));
        } catch (Exception e) {
            return Double.valueOf(String.valueOf(value)).longValue();
        }
    }

    private static Object parseShort(Object value) {
        try {
            return Short.valueOf(String.valueOf(value));
        } catch (Exception e) {
            return Double.valueOf(String.valueOf(value)).shortValue();
        }
    }

    /**
     * 科学计数法转换
     */
    private static String doubleToString(Object object) {
        String value = object.toString();
        if (value.contains("E")) {
            BigDecimal bigDecimal = new BigDecimal(value);
            return bigDecimal.toPlainString();
        }
        return value;
    }

    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static String format(Date date, String format) {
        if (!Strings.isNullOrEmpty(format) || Objects.nonNull(date)) {
            return new SimpleDateFormat(format).format(date);
        }
        return null;
    }
}
