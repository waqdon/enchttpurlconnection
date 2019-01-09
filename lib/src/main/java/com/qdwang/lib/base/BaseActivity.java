package com.qdwang.lib.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qdwang.lib.R;
import com.qdwang.lib.permission.IPermissionCallback;
import com.qdwang.lib.permission.PermissionManager;
import com.qdwang.lib.permission.dialog.DialogAppSettings;
import com.qdwang.lib.utils.StatusBarUtil;

import java.util.List;

/**
 * author: create by qdwang
 * date: 2018/11/1 16:42
 * described：基础activity，处理一些页面的通用方法和相同业务
 */
public abstract class BaseActivity<V extends IBaseView, P extends BasePresenter<V>> extends AppCompatActivity implements IBaseView, IPermissionCallback {

    protected P presenter;
    protected Toolbar toolbar;
    protected TextView tvTitle, tvMenu;
    private RelativeLayout contentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setLightMode(this);
        initParams();
        contentView = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        init(contentView);
        createView(savedInstanceState);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addContentView(contentView, params);
        initPresenter();
        if(presenter !=null){
            presenter.onAttachedView((V) this);
            presenter.onCreate();
        }
    }

    private void init(View contentView) {
        toolbar = contentView.findViewById(R.id.toolbar);
        tvTitle = contentView.findViewById(R.id.tv_title);
        tvMenu = contentView.findViewById(R.id.tv_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    protected abstract void initParams();
    protected abstract void createView(Bundle savedInstanceState);
    protected abstract void initPresenter();

    @Override
    protected void onResume() {
        super.onResume();
        if(presenter != null){
            presenter.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(presenter != null){
            presenter.onPause();
        }
    }

    @Override
    public void onPermissionGranted(int requestCode, List<String> granted) {

    }

    @Override
    public void onPermissionDeined(String rationale, int requestCode, List<String> denied) {
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
        PermissionManager.onRequestPermissionResult("", requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(presenter != null){
            presenter.onDetachedView();
            presenter.onDestory();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
