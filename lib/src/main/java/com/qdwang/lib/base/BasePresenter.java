package com.qdwang.lib.base;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * author: create by qdwang
 * date: 2018/11/6 15:34
 * described：
 */
public abstract class BasePresenter<V extends IBaseView> {

    protected Context context;
    private WeakReference<V> vWeakReference;
    protected V iBaseView;

    public void onAttachedView(V iBaseView) {
        vWeakReference = new WeakReference<>(iBaseView);
        this.iBaseView = vWeakReference.get();
    }

    public void onDetachedView() {
        iBaseView = null;
    }

    protected void onCreate(){}
    protected void onResume(){}
    protected void onPause(){}
    protected void onDestory(){}

}
