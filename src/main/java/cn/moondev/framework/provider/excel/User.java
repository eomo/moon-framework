package cn.moondev.framework.provider.excel;

@ExcelSheet(name = "用户")
public class User {

    @ExcelColumn(name = "姓名")
    public String name;
    @ExcelColumn(name = "年龄")
    public int age;
    @ExcelColumn(name = "年级")
    public int grade;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("name='").append(name).append('\'');
        sb.append(", age='").append(age).append('\'');
        sb.append(", grade='").append(grade).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
