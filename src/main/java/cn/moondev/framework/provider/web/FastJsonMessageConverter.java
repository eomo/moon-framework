package cn.moondev.framework.provider.web;

import cn.moondev.framework.provider.io.IOUtils;
import cn.moondev.framework.provider.security.XssUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Arrays;

public class FastJsonMessageConverter extends FastJsonHttpMessageConverter {

    public static final FastJsonMessageConverter INSTANCE = new FastJsonMessageConverter();

    private FastJsonMessageConverter() {
        this.setCharset(Charset.forName("UTF-8"));
        this.setSupportedMediaTypes(
                Arrays.asList(
                        MediaType.APPLICATION_JSON,
                        MediaType.TEXT_HTML,
                        MediaType.TEXT_PLAIN,
                        MediaType.APPLICATION_OCTET_STREAM));
        this.setFeatures(
                new SerializerFeature[]{
                        SerializerFeature.SkipTransientField,
                        SerializerFeature.QuoteFieldNames,
                        SerializerFeature.WriteDateUseDateFormat,
                        SerializerFeature.BrowserSecure
                });
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        try {
            String requestBody = IOUtils.toString(inputMessage.getBody(), Charset.forName("UTF-8"));
            requestBody = XssUtils.xssEncode(requestBody);
            return JSON.parseObject(requestBody, type, this.getFastJsonConfig().getFeatures());
        } catch (JSONException ex) {
            throw new HttpMessageNotReadableException("JSON parse error: " + ex.getMessage(), ex);
        } catch (IOException ex) {
            throw new HttpMessageNotReadableException("I/O error while reading input message", ex);
        }
    }
}
