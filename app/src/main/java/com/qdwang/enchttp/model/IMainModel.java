package com.qdwang.enchttp.model;

/**
 * author: create by qdwang
 * date: 2018/11/7 20:21
 * described：
 */
public interface IMainModel {

    void getData(IMainModelListener listener);

    interface IMainModelListener{

        void complete(String data);
    }
}
