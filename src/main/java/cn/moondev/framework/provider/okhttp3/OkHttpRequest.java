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
    public String method = "GET";

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

    public OkHttpRequest(){
        requestHeader.put("Accept","*/*");
        requestHeader.put("Accept-Language","zh-CN,zh;q=0.9");
        requestHeader.put("Connection","keep-alive");
        requestHeader.put("DNT","1");
        requestHeader.put("Host","data.eastmoney.com");
        requestHeader.put("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36");
    }

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
