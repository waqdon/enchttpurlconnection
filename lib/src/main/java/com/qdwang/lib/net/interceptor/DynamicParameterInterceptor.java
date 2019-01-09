package com.qdwang.lib.net.interceptor;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author qdwang$
 * @date 2018/8/31$
 * @describe
 */
public class DynamicParameterInterceptor implements Interceptor {

    private HashMap<String, String> map;

    public DynamicParameterInterceptor(HashMap<String, String> map) {
        this.map = map;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //get请求后面追加共同的参数
        HttpUrl.Builder bulider = request.url().newBuilder();
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            bulider.addQueryParameter((String) entry.getKey(), (String) entry.getValue());
        }
        request = request.newBuilder().url(bulider.build()).build();
        return chain.proceed(request);
    }
}
