package com.qdwang.mylibrary.downfile.net;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author: create by qdwang
 * date: 2018/11/12 15:25
 * described：
 */
public class OkHttpManager {

    private  OkHttpClient.Builder builder;
    public OkHttpManager() {
        builder=new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
    }

    public static OkHttpManager getInstances(){
        return OkHttpHolder.okHttpManager;
    }

    private static class OkHttpHolder{
        private final static OkHttpManager okHttpManager = new OkHttpManager();
    }

    /**
     * 异步请求
     * @param url
     * @param start //文件从哪个字节开始下载
     * @param end  // 文件从哪个字节结束下载
     * @param callback
     * @return
     */
    public Call initRequest(String url, long start, long end, Callback callback){
        Request request=new Request.Builder().url(url).header("Range", "bytes="+start+"="+end).build();
        Call call = builder.build().newCall(request);
        call.enqueue(callback);
        return call;
    }

    /**
     * 同步请求
     * @param url
     * @return
     * @throws IOException
     */
    public Response initRequest(String url) throws IOException {
        Request request=new Request.Builder().url(url).header("Range", "bytes=0-").build();
        Response response = builder.build().newCall(request).execute();
        return response;
    }

    public Response initRequest(String url, String lastModify) {

        return null;
    }
}
