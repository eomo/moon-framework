package cn.moondev.framework.utils;

import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class StringUtils {

    /**
     * 将Map转换成&=连接的字符串
     *
     * @param map  参数Map
     * @param sort 是否需要排序
     * @return
     */
    public static String createLinkString(Map<String, Object> map, boolean sort, boolean ignoreNull) {
        if (!CollectionUtils.isEmpty(map)) {
            Set<String> keys = map.keySet();
            if (sort) {
                keys.stream().sorted((a, b) -> a.compareToIgnoreCase(b)).collect(Collectors.toList());
            }
            StringBuilder sb = new StringBuilder();
            Object value = null;
            for (String key : keys) {
                value = map.get(key);
                if (ignoreNull && Objects.isNull(value)) {
                    continue;
                }
                sb.append(key).append("=").append(null2Empty(value)).append("&");
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 对象转字符串
     *
     * @param obj
     * @return
     */
    public static String null2Empty(Object obj) {
        return Objects.isNull(obj) ? "" : obj.toString();
    }
}
