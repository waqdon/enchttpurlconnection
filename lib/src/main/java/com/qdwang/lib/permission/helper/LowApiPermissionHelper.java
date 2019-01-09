package com.qdwang.lib.permission.helper;

import android.app.Activity;

/**
 * author: create by qdwang
 * date: 2018/10/26 09:51
 * described：
 */
public class LowApiPermissionHelper extends PermissionHelper {

    public LowApiPermissionHelper(Activity activity) {
        super(activity);
    }

    @Override
    public void requestPermission(String rationale, int positiveButton, int negativeButton, int requestCode, String... permissions) {
        throw new IllegalStateException("低于android6.0版本无需运行权限请求");
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(String deniedPerm) {
        return false;
    }
}
