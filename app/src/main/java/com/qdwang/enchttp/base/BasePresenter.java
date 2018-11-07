package com.qdwang.enchttp.base;

import java.lang.ref.WeakReference;

/**
 * author: create by qdwang
 * date: 2018/11/6 15:34
 * described：
 */
public abstract class BasePresenter<V extends BaseView> implements IBasePresenter<V> {

    private WeakReference<V> vWeakReference;
    protected V iBaseView;

    @Override
    public void onAttachedView(V iBaseView) {
        vWeakReference = new WeakReference<>(iBaseView);
        this.iBaseView = vWeakReference.get();
    }

    @Override
    public void onDetachedView() {
        iBaseView = null;
    }
}
