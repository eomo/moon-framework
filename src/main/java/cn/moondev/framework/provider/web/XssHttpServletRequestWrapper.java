package cn.moondev.framework.provider.web;

import com.google.common.base.Strings;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        String header = super.getHeader(name);
        return Strings.isNullOrEmpty(header) ? header : HtmlUtils.htmlEscape(header);
    }

    @Override
    public String getParameter(String name) {
        String param = super.getParameter(name);
        return Strings.isNullOrEmpty(param) ? param : HtmlUtils.htmlEscape(param);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapseValues = new String[length];
            for (int i = 0; i < length; i++) {
                escapseValues[i] = HtmlUtils.htmlEscape(values[i]);
            }
            return escapseValues;
        }
        return super.getParameterValues(name);
    }

}
