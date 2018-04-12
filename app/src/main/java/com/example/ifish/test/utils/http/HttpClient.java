package com.example.ifish.test.utils.http;

/**
 * Created by zfx on 2018/4/11.
 */

import android.util.Log;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * http客户端，模拟各种http请求
 * 单例模式
 */

public class HttpClient {

    private static final String TAG = "HTTP_CLIENT";

    private static HttpClient instance = new HttpClient();

    private OkHttpClient httpClient;
    private ObjectMapper mapper;

    private HttpClient() {
        httpClient = new OkHttpClient();
        mapper = new ObjectMapper();
    }

    public static HttpClient getInstance() {
        return instance;
    }

    /**
     * 同步get请求
     * @param url     链接地址
     * @param headers 请求头
     * @param params  请求参数
     * @return
     */
    public String get(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        Request.Builder builder = new Request.Builder().get();
        // 添加请求头
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        // 拼接参数至url中
        if (params != null && params.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder(url);
            stringBuilder.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuilder.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            url = stringBuilder.toString();
        }
        Request request = builder.url(url).build();

        Response response = httpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String res = response.body().string();
            Log.i(TAG, "GET:"+res);
            return res;
        } else {
            Log.e(TAG, "GET请求错误码："+response.code());
            return null;
        }
    }

    /**
     * 同步get请求
     *
     * @param url 链接
     * @return
     */
    public String get(String url) throws IOException {
        return get(url, null, null);
    }

    /**
     * 同步get请求
     *
     * @param url    请求链接
     * @param params 请求参数
     * @return
     */
    public String get(String url, Map<String, String> params) throws IOException {
        return get(url, null, params);
    }

    /**
     * 同步post请求
     *
     * @param url     请求链接地址
     * @param headers 请求头
     * @param params  请求参数
     * @return
     */
    public String post(String url, Map<String, String> headers, Map<String, Object> params) throws IOException {
        MediaType APPLICATION_JSON = MediaType.parse("application/json");
        String postBody = mapper.writeValueAsString(params);
        Log.i(TAG, "post请求参数:" + postBody);
        Request.Builder builder = new Request.Builder().url(url).post(RequestBody.create(APPLICATION_JSON, postBody));
        // 添加头部信息
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.build();

        Response response = httpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String res = response.body().string();
            Log.i(TAG, "POST:"+res);
            return res;
        } else {
            Log.i(TAG, "请求错误码：" + response.code());
            return null;
        }
    }

    /**
     * 同步post请求
     *
     * @param url    请求链接地址
     * @param params 请求参数
     * @return
     * @throws JsonProcessingException
     */
    public String post(String url, Map<String, Object> params) throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return post(url, headers, params);
    }
}