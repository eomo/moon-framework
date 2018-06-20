package cn.moondev.framework.provider.okhttp3;

import java.util.List;

public interface ResponseHandler<T> {

    /**
     * 将Http请求响应的数据转换成对应的业务对象
     *
     * @param content
     * @return
     */
    List<T> convert(String content);


    /**
     * 将Http请求响应数据进行业务处理
     *
     * @param content
     */
    void handle(String content);
}
