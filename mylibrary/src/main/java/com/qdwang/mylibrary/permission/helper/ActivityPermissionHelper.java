package com.qdwang.mylibrary.permission.helper;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;

/**
 * author: create by qdwang
 * date: 2018/10/26 09:51
 * described：
 */
public class ActivityPermissionHelper extends PermissionHelper {

    public ActivityPermissionHelper(Activity activity) {
        super(activity);
    }

    @Override
    public void requestPermission(String rationale, int positiveButton, int negativeButton, int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(getHost(), permissions, requestCode);
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(String deniedPerm) {
        return ActivityCompat.shouldShowRequestPermissionRationale(getHost(), deniedPerm);
    }
}
