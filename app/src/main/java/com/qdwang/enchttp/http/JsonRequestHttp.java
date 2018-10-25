package com.qdwang.enchttp.http;

/**
 * author: create by qdwang
 * date: 2018/10/24 18:49
 * described：
 */
public class JsonRequestHttp {

    /**
     *
     * @param requestInfo
     * @param url
     * @param response
     * @param dataCallBack
     * @param <T>
     * @param <M>
     */
    public static<T, M> void sendJsonRequest(T requestInfo, String url, Class<M> response, IDataCallBack<M> dataCallBack){
       IHttpListener httpListener = new JsonHttpListener<M>(response, dataCallBack);
       IHttpService httpService = new JsonHttpService();
       HttpTask<T> httpTask = new HttpTask<T>(requestInfo, url, httpService, httpListener);
       ThreadPoolManager.getInstance().execute(httpTask);
    }
}
