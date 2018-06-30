package cn.moondev.framework.provider.excel.annotation;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {

    /**
     * 列名称
     */
    String name() default "";

    /**
     * 列宽
     */
    int width() default 0;

    /**
     * 水平对齐方式
     */
    HorizontalAlignment align() default HorizontalAlignment.LEFT;

    /**
     * 时间格式化，日期类型时生效
     */
    String dateformat() default "yyyy-MM-dd";

    /**
     * 格式转换，格式A_b
     */
    String[] replace() default {};
}
