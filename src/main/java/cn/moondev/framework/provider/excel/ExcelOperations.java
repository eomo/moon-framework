package cn.moondev.framework.provider.excel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

public class ExcelOperations {

    public static Logger LOG = LoggerFactory.getLogger(ExcelOperations.class);

    public static void main(String[] args) {
//        String path = "/Users/Moon/Documents/test.xls";
//        List<User> users = ExcelUtils.importExcel(path,User.class);

        Field[] fields = User.class.getFields();
        for (Field field : fields) {
            System.out.println(field.getType());
        }
        System.out.println(String.class);
        System.out.println(Integer.TYPE);
//        System.out.println(users);
    }
}
