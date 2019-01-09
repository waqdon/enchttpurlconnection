package com.qdwang.lib.net.interceptor;

import com.qdwang.lib.net.RxHttp;
import com.qdwang.lib.net.des.EncipherProxy;
import com.qdwang.lib.utils.LogUtils;

import okhttp3.*;
import okio.Buffer;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author qdwang$
 * @date 2018/8/31$
 * @describe
 */
public class LogInterceptor implements Interceptor {

    private final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        //这个chain里面包含了request和response，所以你要什么都可以从这里拿
        Request request = chain.request();
        long t1 = System.nanoTime();//请求发起的时间
        String method = request.method();
        if ("POST".equals(method)) {
            String body = null;
            if (request.body() instanceof RequestBody) {
                RequestBody requestBody = request.body();
                if (requestBody != null) {
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);
                    Charset charset = UTF8;
                    MediaType contentType = requestBody.contentType();
                    if (contentType != null) {
                        charset = contentType.charset(UTF8);
                    }
                    body = buffer.readString(charset);
                }
                LogUtils.e(RxHttp.TAG, String.format("发送请求 %s on %nRequestParams:{%s}", request.url(), body));
            }
        } else {
            LogUtils.e(RxHttp.TAG, String.format("发送请求 %s on %s%n%s", request.url(), chain.connection(), request.headers()));
        }
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();//收到响应的时间
        //这里不能直接使用response.body().string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
        //个新的response给应用层处理
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        try {
            LogUtils.e(RxHttp.TAG,
                    String.format("接收响应: [%s] %n返回json:【%s】 %.1fms %n%s",
                            response.request().url(),
                            EncipherProxy.decrypt(responseBody.string()),
                            (t2 - t1) / 1e6d,
                            response.headers()
                    ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
