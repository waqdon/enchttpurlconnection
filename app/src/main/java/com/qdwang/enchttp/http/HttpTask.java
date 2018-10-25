package com.qdwang.enchttp.http;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

/**
 * author: create by qdwang
 * date: 2018/10/24 17:57
 * described：
 */
public class HttpTask<T> implements Runnable {

    private IHttpService httpService;
    private IHttpListener httpListener;
    private Gson gson = new Gson();

    /**
     *
     * @param requestInfo
     * @param url
     * @param httpService
     * @param httpListener
     * @param <T>
     */
    protected <T> HttpTask(T requestInfo, String url, IHttpService httpService, IHttpListener httpListener){
        this.httpListener = httpListener;
        this.httpService = httpService;
        this.httpService.setUrl(url);
        this.httpService.setHttpCallBack(this.httpListener);
        if(requestInfo != null){
            //把参数转成json请求也可以转成别的请求格式
            String requestData = gson.toJson(requestInfo);
            try {
                this.httpService.setRequest(requestData.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        httpService.request();
    }
}
