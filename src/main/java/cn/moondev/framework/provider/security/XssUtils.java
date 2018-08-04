package cn.moondev.framework.provider.security;

import java.util.regex.Pattern;

public class XssUtils {

    /**
     * 防止XSS注入
     */
    public static String xssEncode(String src) {
        if (null == src || src.isEmpty()) {
            return src;
        }
        return escapeCharacter(stripXSSAndSql(src));
    }

    /**
     * 对部分特殊字符进行转义(半角->全角)
     */
    public static String escapeCharacter(String src) {
        StringBuilder sb = new StringBuilder(src.length() + 16);
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            switch (c) {
                case '>':
                    sb.append("＞");// 转义大于号
                    break;
                case '<':
                    sb.append("＜");// 转义小于号
                    break;
                case '\'':
                    sb.append("＇");// 转义单引号
                    break;
                case '\"':
                    sb.append("＂");// 转义双引号
                    break;
                case '&':
                    sb.append("＆");// 转义&
                    break;
                case '#':
                    sb.append("＃");// 转义#
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 去掉字符串中的脚本内容
     */
    public static String stripXSSAndSql(String src) {
        // Avoid anything between script tags
        Pattern scriptPattern = Pattern.compile("<[\r\n| | ]*script[\r\n| | ]*>(.*?)</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);
        src = scriptPattern.matcher(src).replaceAll("");
        // Avoid anything in a src="http://www.yihaomen.com/article/java/..." type of e-xpression
        scriptPattern = Pattern.compile("src[\r\n| | ]*=[\r\n| | ]*[\\\"|\\\'](.*?)[\\\"|\\\']", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        src = scriptPattern.matcher(src).replaceAll("");
        // Remove any lonesome </script> tag
        scriptPattern = Pattern.compile("</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);
        src = scriptPattern.matcher(src).replaceAll("");
        // Remove any lonesome <script ...> tag
        scriptPattern = Pattern.compile("<[\r\n| | ]*script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        src = scriptPattern.matcher(src).replaceAll("");
        // Avoid eval(...) expressions
        scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        src = scriptPattern.matcher(src).replaceAll("");
        // Avoid e-xpression(...) expressions
        scriptPattern = Pattern.compile("e-xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        src = scriptPattern.matcher(src).replaceAll("");
        // Avoid javascript:... expressions
        scriptPattern = Pattern.compile("javascript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
        src = scriptPattern.matcher(src).replaceAll("");
        // Avoid vbscript:... expressions
        scriptPattern = Pattern.compile("vbscript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
        src = scriptPattern.matcher(src).replaceAll("");
        // Avoid onload= expressions
        scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        src = scriptPattern.matcher(src).replaceAll("");
        return src;
    }
}
