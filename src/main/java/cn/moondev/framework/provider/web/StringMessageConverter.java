package cn.moondev.framework.provider.web;

import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.Charset;

public class StringMessageConverter extends StringHttpMessageConverter {

    public static final StringMessageConverter INSTANCE = new StringMessageConverter();

    public StringMessageConverter() {
        super(Charset.forName("UTF-8"));
        super.setWriteAcceptCharset(false);
    }
}
