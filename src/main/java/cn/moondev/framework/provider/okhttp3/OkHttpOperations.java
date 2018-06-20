package cn.moondev.framework.provider.okhttp3;

import cn.moondev.framework.model.Format;
import cn.moondev.framework.provider.okhttp3.exception.MethodNotSupportedException;
import cn.moondev.framework.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpOperations {

    private static final Logger LOG = LoggerFactory.getLogger(OkHttpOperations.class);

    /**
     * OkHttpClient应全局唯一
     */
    private OkHttpClient client;

    public OkHttpOperations(long connectionTimeout) {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(connectionTimeout, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 同步请求
     *
     * @param okHttpRequest
     * @param handler       需要实现convert接口
     * @return
     */
    public <T> List<T> syncRequest(OkHttpRequest okHttpRequest, ResponseHandler<T> handler) {
        Request request = buildRequest(okHttpRequest);
        try {
            Response response = client.newCall(request).execute();
            return handler.convert(response.body().string());
        } catch (IOException e) {
            LOG.error("执行Http同步请求时出现异常，{}", okHttpRequest.toString(), e);
            return null;
        }
    }

    /**
     * 同步请求，直接返回数据
     *
     * @param okHttpRequest
     * @return
     */
    public String directSyncRequest(OkHttpRequest okHttpRequest) {
        Request request = buildRequest(okHttpRequest);
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            LOG.error("执行Http同步请求时出现异常，{}", okHttpRequest.toString(), e);
            return null;
        }
    }

    /**
     * 异步请求
     *
     * @param okHttpRequest
     * @param handler       需要实现handle接口
     * @param <T>
     */
    public <T> void asyncRequest(OkHttpRequest okHttpRequest, ResponseHandler<T> handler) {
        Request request = buildRequest(okHttpRequest);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 暂时只打印错误日志，可根据业务需求将失败的请求放置到重试队列中
                LOG.error("执行Http异步请求时出现异常，{}", okHttpRequest.toString(), e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handler.handle(response.body().string());
            }
        });
    }

    /**
     * 创建OkHttp Request
     *
     * @param okHttpRequest
     * @return
     */
    public Request buildRequest(OkHttpRequest okHttpRequest) {
        Request.Builder builder = new Request.Builder();
        if (!CollectionUtils.isEmpty(okHttpRequest.requestHeader)) {
            for (Map.Entry<String, String> entry : okHttpRequest.requestHeader.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if ("GET".equalsIgnoreCase(okHttpRequest.method)) {
            return buildGetRequest(okHttpRequest, builder);
        } else if ("POST".equalsIgnoreCase(okHttpRequest.method)) {
            return buildPostRequest(okHttpRequest, builder);
        }
        throw new MethodNotSupportedException();
    }

    private Request buildGetRequest(OkHttpRequest okHttpRequest, Request.Builder builder) {
        String queryString = StringUtils.createLinkString(okHttpRequest.requestParams, false, false);
        String url = String.format("%s?%s", okHttpRequest.domain, queryString);
        return builder.url(url).build();
    }

    private Request buildPostRequest(OkHttpRequest okHttpRequest, Request.Builder builder) {
        RequestBody requestBody = RequestBody.create(null, new byte[]{});
        if (!CollectionUtils.isEmpty(okHttpRequest.requestParams)) {
            if (Format.PLAIN == okHttpRequest.requestFormat) {
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                for (Map.Entry<String, Object> entry : okHttpRequest.requestParams.entrySet()) {
                    formBodyBuilder.add(entry.getKey(), StringUtils.null2Empty(entry.getValue()));
                }
                requestBody = formBodyBuilder.build();
            } else if (Format.JSON == okHttpRequest.requestFormat) {
                requestBody = RequestBody.create(MediaType.parse("application/json"),
                        JSONObject.toJSONBytes(okHttpRequest.requestParams));
            }
        }
        return builder.url(okHttpRequest.domain).post(requestBody).build();
    }
}
