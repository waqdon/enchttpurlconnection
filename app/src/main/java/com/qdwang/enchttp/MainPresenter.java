package com.qdwang.enchttp;

import com.qdwang.enchttp.base.BasePresenter;
import com.qdwang.enchttp.model.IMainModel;
import com.qdwang.enchttp.model.MainModel;

/**
 * author: create by qdwang
 * date: 2018/11/6 15:32
 * described：
 */
public class MainPresenter extends BasePresenter<MainView> implements IMainPresenter {

    private IMainModel iMainModel = new MainModel();

    @Override
    public void getData() {
        if(iBaseView!= null){
            iBaseView.showToast("123");
        }
    }

    public void fetch(){
        if(iBaseView != null){
            if(iMainModel != null){
                iMainModel.getData(new IMainModel.IMainModelListener() {
                    @Override
                    public void complete(String data) {

                    }
                });
            }
        }
    }
}
