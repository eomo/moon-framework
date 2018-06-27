package cn.moondev.framework.provider.excel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ExcelOperations {

    public static Logger LOG = LoggerFactory.getLogger(ExcelOperations.class);

    public static void main(String[] args) {
        String path = "/Users/Moon/Documents/test.xls";
        List<User> users = ExcelUtils.importExcel(path,User.class);
        System.out.println(users);
    }
}
