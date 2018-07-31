package cn.moondev.framework.utils;

import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Joiner;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class JsonUtils {

    public static String parseJsonStringArray(JSONArray stringArray) {
        if (CollectionUtils.isEmpty(stringArray)) {
            return "";
        }
        List<String> list = stringArray.stream().map(item -> item.toString()).collect(Collectors.toList());
        return Joiner.on(',').join(list);
    }
}
