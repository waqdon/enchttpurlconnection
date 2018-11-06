package com.qdwang.enchttp;

import com.qdwang.enchttp.base.BasePresenter;

/**
 * author: create by qdwang
 * date: 2018/11/6 15:32
 * described：
 */
public class MainPresenter extends BasePresenter<MainView> implements IMainPresenter {

    @Override
    public void getData() {
        iBaseView.showToast("123");
    }
}
