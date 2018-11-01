package com.qdwang.enchttp;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.qdwang.mylibrary.permission.IPermissionCallback;
import com.qdwang.mylibrary.permission.PermissionManager;
import com.qdwang.mylibrary.permission.dialog.DialogAppSettings;

import java.util.List;

/**
 * author: create by qdwang
 * date: 2018/11/1 16:42
 * described：
 */
public class BaseActivity extends AppCompatActivity implements IPermissionCallback {

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
}
