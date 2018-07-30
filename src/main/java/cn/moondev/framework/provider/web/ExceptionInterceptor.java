package cn.moondev.framework.provider.web;

import cn.moondev.framework.model.AppException;
import cn.moondev.framework.model.ResponseDTO;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;

public class ExceptionInterceptor implements HandlerInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex != null) {
            processException(response, ex);
        }
    }

    public static void processException(HttpServletResponse response, Throwable ex) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        byte[] error = JSONObject.toJSONBytes(errorResponseDTO(ex));
        response.setContentLength(error.length);
        try (OutputStream os = response.getOutputStream()) {
            os.write(error);
        }
    }

    @NotNull
    private static ResponseDTO errorResponseDTO(Throwable ex) {
        LOG.info("request error:", ex);
        if (ex instanceof AppException) {
            AppException exception = (AppException) ex;
            return ResponseDTO.failed(exception.mcode, exception.message);
        }
        return ResponseDTO.failed("-1");
    }
}