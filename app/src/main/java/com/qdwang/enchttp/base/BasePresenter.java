package com.qdwang.enchttp.base;

/**
 * author: create by qdwang
 * date: 2018/11/6 15:34
 * described：
 */
public abstract class BasePresenter<V extends BaseView> implements IBasePresenter<V> {

    protected V iBaseView;

    @Override
    public void onAttachedView(V iBaseView) {
        this.iBaseView = iBaseView;
    }

    @Override
    public void onDetachedView() {
        iBaseView = null;
    }
}
