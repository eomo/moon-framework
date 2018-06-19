package cn.moondev.framework.provider.okhttp3;

import cn.moondev.framework.model.Format;
import com.google.common.collect.Maps;

import java.util.Map;

public class OkHttpRequest {

    /**
     * HTTP Request domain
     */
    public String domain;

    /**
     * HTTP Request method: GET or POST
     */
    public String method;

    /**
     * HTTP Request Header
     */
    public Map<String, String> requestHeader = Maps.newHashMap();

    /**
     * HTTP Request params
     */
    public Map<String, Object> requestParams = Maps.newHashMap();

    /**
     * HTTP Request data format
     */
    public Format requestFormat = Format.PLAIN;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OkHttpRequest{");
        sb.append("domain='").append(domain).append('\'');
        sb.append(", method='").append(method).append('\'');
        sb.append(", requestHeader=").append(requestHeader);
        sb.append(", requestParams=").append(requestParams);
        sb.append(", requestFormat=").append(requestFormat);
        sb.append('}');
        return sb.toString();
    }
}
