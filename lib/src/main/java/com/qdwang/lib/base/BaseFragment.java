package com.qdwang.lib.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * @author qdwang
 * @date 2019/1/6
 * @describe
 */
public abstract class BaseFragment<V extends IBaseView, P extends BasePresenter<V>> extends Fragment implements IBaseView{

    protected P presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initParams();
        View contentView = createView(inflater, container, savedInstanceState);
        initPresenter();
        if(presenter != null){
            presenter.onAttachedView((V) this);
            presenter.onCreate();
        }
        return contentView;
    }

    protected abstract void initParams();
    protected abstract View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
    protected abstract void initPresenter();

    @Override
    public void onResume() {
        super.onResume();
        if(presenter != null){
            presenter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(presenter != null){
            presenter.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(presenter != null){
            presenter.onDetachedView();
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
