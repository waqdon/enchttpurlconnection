package com.qdwang.enchttp.http;

/**
 * author: create by qdwang
 * date: 2018/10/24 17:50
 * described：设置url
 *            设置请求参数
 *            执行请求
 */
public interface IHttpService {

    void setUrl(String url);

    void setRequest(byte[] request);

    void request();

    void setHttpCallBack(IHttpListener listener);
}
