package com.qdwang.mylibrary.permission.helper;

import android.app.Activity;
import android.os.Build;

import java.util.List;

/**
 * author: create by qdwang
 * date: 2018/10/26 09:45
 * described：抽象辅助类
 */
public abstract class PermissionHelper {

    private Activity activity;

    public PermissionHelper(Activity activity) {
        this.activity = activity;
    }

    public Activity getHost() {
        return activity;
    }

    public static PermissionHelper newInstance(Activity activity) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return new LowApiPermissionHelper(activity);
        }
        return new ActivityPermissionHelper(activity);
    }

    /**
     * 用户申请权限
     * @param rationale 如果用户第一次拒绝请求，解释为何需要这组权限的说明内容
     * @param positiveButton 确定按钮
     * @param negativeButton 取消按钮
     * @param requestCode 请求标识码
     * @param permissions 需要授权的权限组
     */
    public abstract void requestPermission(String rationale, int positiveButton, int negativeButton, int requestCode, String... permissions);

    /**
     * 第一次打开app时是false
     * 上次弹出权限点击了拒绝，没有勾选“不在询问”返回true
     * 上次弹出权限点击了拒绝，并且勾选“不在询问” 返回false
     * @param deniedPerm 被拒绝的权限
     * @return 点击拒绝没有勾选“不在询问”true， 并且勾选“不在询问” false
     */
    public abstract boolean shouldShowRequestPermissionRationale(String deniedPerm);

    /**
     * 检查被拒绝的权限组中是否有点击了“不在询问”
     * @param deniedPerms
     * @return
     */
    public boolean somePermissionPermanentlyDenied(List<String> deniedPerms) {
        //拒绝的权限组遍历
        for (String deniedPerm: deniedPerms) {
            if(!shouldShowRequestPermissionRationale(deniedPerm)){
                return true;
            }
        }
        return false;
    }
}
