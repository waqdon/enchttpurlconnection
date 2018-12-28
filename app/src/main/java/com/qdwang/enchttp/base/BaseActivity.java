package com.qdwang.enchttp.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.qdwang.enchttp.App;
import com.qdwang.mylibrary.permission.IPermissionCallback;
import com.qdwang.mylibrary.permission.PermissionManager;
import com.qdwang.mylibrary.permission.dialog.DialogAppSettings;
import com.qdwang.mylibrary.skin.SkinFactory;

import java.util.List;

/**
 * author: create by qdwang
 * date: 2018/11/1 16:42
 * described：
 */
public abstract class BaseActivity<V extends BaseView, P extends IBasePresenter<V>> extends AppCompatActivity implements BaseView, IPermissionCallback {

    protected P presenter;
    protected SkinFactory skinFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        skinFactory = App.getApp().skinFactory;
//        skinFactory = new SkinFactory();
        LayoutInflater.from(this).setFactory(skinFactory);
        skinFactory.setFactory(this);

        super.onCreate(savedInstanceState);
        initPresenter();
        presenter.onAttachedView((V) this);
    }

    protected abstract void initPresenter();

    @Override
    public void onPermissionGranted(int requestCode, List<String> granted) {

    }

    @Override
    public void onPermissionDeined(int requestCode, List<String> denied) {
        if(PermissionManager.somePermissionPermanentlyDeined(this, denied)){
            new DialogAppSettings.Builder(this).setRationale("请求权限").setListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setRequestCode(requestCode).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.onRequestPermissionResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetachedView();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
