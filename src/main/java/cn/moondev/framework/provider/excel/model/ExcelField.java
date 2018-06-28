package cn.moondev.framework.provider.excel.model;

import cn.moondev.framework.provider.excel.annotation.ExcelColumn;

import java.lang.reflect.Field;

/**
 * Excel clazz 属性信息
 */
public class ExcelField {

    public Field field;

    public ExcelColumn column;

    public String columnName;

    public String fieldName;

    /**
     * 行的索引
     */
    public int columnIndex;

    public ExcelField() {

    }

    public ExcelField(Field field, ExcelColumn column) {
        this.field = field;
        this.column = column;
        this.columnName = column.name();
        this.fieldName = field.getName();
    }
}
