package cn.moondev.framework.provider.okhttp3;

public interface ResponseHandler<T> {

    /**
     * 将Http请求响应的数据转换成对应的业务对象
     *
     * @param bytes
     * @return
     */
    T convert(byte[] bytes);


    /**
     * 将Http请求响应数据进行业务处理
     *
     * @param bytes
     */
    void handle(byte[] bytes);
}
