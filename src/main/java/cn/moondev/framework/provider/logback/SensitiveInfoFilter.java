package cn.moondev.framework.provider.logback;

import ch.qos.logback.access.AccessConstants;
import ch.qos.logback.access.servlet.TeeFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Access日志中对敏感信息进行过滤
 */
public class SensitiveInfoFilter extends TeeFilter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        super.doFilter(request, response, filterChain);

        byte[] inputBuffer = (byte[]) request.getAttribute(AccessConstants.LB_INPUT_BUFFER);
        if (inputBuffer != null) {
            String requestBody = new String(inputBuffer);
            requestBody = requestBody.replaceAll("\"password\":\"[^\"]*\"", "\"password\":\"***\"");
            requestBody = requestBody.replaceAll("\"idNum\":\"[^\"]*\"", "\"idNum\":\"***\"");
            request.setAttribute(AccessConstants.LB_INPUT_BUFFER, requestBody.getBytes(UTF_8));
        }
    }
}
