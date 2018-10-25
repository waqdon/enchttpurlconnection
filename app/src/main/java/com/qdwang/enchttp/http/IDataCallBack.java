package com.qdwang.enchttp.http;

/**
 * author: create by qdwang
 * date: 2018/10/24 18:07
 * described：回调调用层接口
 */
public interface IDataCallBack<M> {

    void onSuccess(M m);

    void onFalure();
}
