package com.qdwang.enchttp.http;

import java.io.InputStream;

/**
 * author: create by qdwang
 * date: 2018/10/24 17:51
 * described：处理结果
 *             回调给调用层
 */
public interface IHttpListener {

    void onSuccess(InputStream inputStream);

    void onFailure();
}
