package cn.moondev.framework.provider.excel.utils;

import cn.moondev.framework.provider.excel.annotation.ExcelColumn;
import cn.moondev.framework.provider.excel.annotation.ExcelSheet;
import cn.moondev.framework.provider.excel.model.ExcelField;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Excel导入工具类
 */
public class ImportExcelUtils {

    private static Logger LOG = LoggerFactory.getLogger(ImportExcelUtils.class);

    /**
     * 导入Excel
     *
     * @param path  Excel文件路径
     * @param clazz 对应的JavaBean实体类
     * @return
     */
    public static <T> List<T> doImport(String path, Class<T> clazz) {
        File file = new File(path);
        try (Workbook workbook = WorkbookFactory.create(file)) {
            // 获取指定的Sheet
            Sheet sheet = getSheet(workbook, clazz);
            if (Objects.isNull(sheet)) {
                return Lists.newArrayList();
            }
            // 获取对应JavaBean的属性信息
            List<ExcelField> fields = getExcelField(clazz);
            // 遍历Excel的每一行并组装对应的T对象
            return newInstanceList(sheet, clazz, fields);
        } catch (Exception e) {
            LOG.error("导入Excel文件出现异常, path={}", path, e);
            return null;
        }
    }

    /**
     * 遍历Sheet的每一行，然后组装对应的T实例
     */
    private static <T> List<T> newInstanceList(Sheet sheet, Class<T> clazz, List<ExcelField> fields)
            throws IllegalAccessException, InstantiationException {
        List<T> list = Lists.newArrayList();
        Iterator<Row> iterator = sheet.rowIterator();
        Map<Integer, ExcelField> indexFieldMap = null;
        boolean firstRow = true;
        Cell cell = null;
        while (iterator.hasNext()) {
            Row row = iterator.next();
            if (firstRow) {
                indexFieldMap = getColumnIndex(fields, row);
                firstRow = false;
                continue;
            }
            T object = clazz.newInstance();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                cell = cellIterator.next();
                ExcelField excelField = indexFieldMap.get(cell.getColumnIndex());
                if (Objects.nonNull(excelField)) {
                    Field field = excelField.field;
                    field.setAccessible(true);
                    field.set(object, CellValueUtils.getValue(excelField, cell));
                }
            }
            list.add(object);
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
     * 获取指定名称的Sheet
     *
     * @param clazz Excel行对应一个Java Class
     * @return
     */
    private static Sheet getSheet(Workbook workbook, Class<?> clazz) {
        ExcelSheet sheetAnnotation = clazz.getAnnotation(ExcelSheet.class);
        // 如果Excel的Sheet名称为空，那么直接获取第一个Sheet
        if (Objects.isNull(sheetAnnotation) || Strings.isNullOrEmpty(sheetAnnotation.name())) {
            return workbook.getSheetAt(0);
        }
        return workbook.getSheet(sheetAnnotation.name());
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
