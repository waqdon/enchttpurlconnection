package com.qdwang.lib.net;

/**
 * author: create by qdwang
 * date: 2018/9/4 14:13
 * described：出参统一部分的的数据
 */
public class ResponseInfo<T> {

    public int rspCode;
    public String showMsg;
    public String rspMsg;
    public T data;
}
