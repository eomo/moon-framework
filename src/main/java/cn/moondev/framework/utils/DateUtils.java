package cn.moondev.framework.utils;

import com.google.common.base.Strings;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class DateUtils {

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
