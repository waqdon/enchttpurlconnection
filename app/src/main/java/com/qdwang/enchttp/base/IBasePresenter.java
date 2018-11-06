package com.qdwang.enchttp.base;

/**
 * author: create by qdwang
 * date: 2018/11/6 14:12
 * described：
 */
public interface IBasePresenter<V extends BaseView> {

    /**
     * 绑定view
     * @param v
     */
    void onAttachedView(V v);

    /**
     * 解除绑定
     */
    void onDetachedView();
}
