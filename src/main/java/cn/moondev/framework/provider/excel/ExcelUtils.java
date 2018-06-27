package cn.moondev.framework.provider.excel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExcelUtils<T> {

    public static <T> List<T> importExcel(String path, Class<T> clazz) {
        File file = new File(path);
        try (Workbook workbook = WorkbookFactory.create(file)) {
            return importSheet(workbook, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static <T> List<T> importSheet(Workbook workbook, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        List<T> list = Lists.newArrayList();
        String sheetName = getSheetName(clazz);
        List<ExcelField> fields = getExcelField(clazz);
        // Excel Sheet
        Sheet sheet = workbook.getSheet(sheetName);
        if (Objects.isNull(sheet)) {
            return Lists.newArrayList();
        }
        Iterator<Row> iterator = sheet.rowIterator();
        int index = 0;
        Map<Integer, ExcelField> indexFieldMap = null;
        Cell cell = null;
        while (iterator.hasNext()) {
            Row row = iterator.next();
            // 第一行为标题
            if (index == 0) {
                indexFieldMap = getColumnIndex(fields, row);
            } else {
                T object = clazz.newInstance();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    cell = cellIterator.next();
                    cell.setCellType(CellType.STRING);
                    String cellValue = cell.getStringCellValue();

                    ExcelField excelField = indexFieldMap.get(cell.getColumnIndex());
                    if (Objects.nonNull(excelField)) {
                        Field field = excelField.field;
                        field.setAccessible(true);
                        field.set(object,cellValue);
                        ReflectionUtils.setField(field,object,cellValue);
                    }
                }
                list.add(object);
            }
            index++;
        }
        return list;

    }

    /**
     * 根据Excel的第一行Title对List<ExcelField>进行转换
     *
     * @param excelFields Excel Field Info
     * @param firstRow    Excel的第一行，第一行必须是Title行，否则导入会出现未知错误
     * @return Map.key = 该字段的列索引  Map.value = Field Info
     */
    private static Map<Integer, ExcelField> getColumnIndex(List<ExcelField> excelFields, Row firstRow) {
        Map<String, ExcelField> fieldMap = excelFields.stream().collect(Collectors.toMap(f -> f.columnName, f -> f));
        Iterator<Cell> iterator = firstRow.cellIterator();
        Map<Integer, ExcelField> indexFieldMap = Maps.newHashMap();
        ExcelField field = null;
        while (iterator.hasNext()) {
            Cell cell = iterator.next();
            cell.setCellType(CellType.STRING);
            String cellValue = cell.getStringCellValue();
            field = fieldMap.get(cellValue);
            if (Objects.nonNull(field)) {
                field.columnIndex = cell.getColumnIndex();
                indexFieldMap.put(field.columnIndex, field);
            }
        }
        return indexFieldMap;
    }

    /**
     * 获取Excel Sheet名称
     *
     * @param clazz Excel行对应一个Java Class
     * @return
     */
    private static <T> String getSheetName(Class<T> clazz) {
        ExcelSheet sheetAnnotation = clazz.getAnnotation(ExcelSheet.class);
        return Optional.of(sheetAnnotation.name()).orElse(clazz.getSimpleName());
    }

    /**
     * 获取Excel Clazz对应的属性
     *
     * @param clazz Excel行对应一个Java Class˚
     * @return
     */
    private static <T> List<ExcelField> getExcelField(Class<T> clazz) {
        List<ExcelField> excelFields = Lists.newArrayList();
        Field[] fields = clazz.getDeclaredFields();
        if (null == fields || fields.length == 0) {
            return excelFields;
        }
        ExcelColumn column = null;
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            column = field.getAnnotation(ExcelColumn.class);
            if (Objects.isNull(column)) {
                continue;
            }
            excelFields.add(new ExcelField(field, column));
        }
        return excelFields;
    }
}
