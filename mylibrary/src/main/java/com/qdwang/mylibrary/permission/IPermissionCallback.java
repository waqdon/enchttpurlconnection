package com.qdwang.mylibrary.permission;

import java.util.List;

/**
 * author: create by qdwang
 * date: 2018/10/26 10:14
 * described：权限请求回调结果
 */
public interface IPermissionCallback {
    /**
     * 授予权限通过返回
     * @param requestCode 授权请求吗标识
     * @param granted 请求的权限组
     */
    void onPermissionGranted(int requestCode, List<String> granted);

    /**
     * 拒绝权限的返回
     * @param requestCode 授权请求吗标识
     * @param denied 请求授权组
     */
    void onPermissionDeined(int requestCode, List<String> denied);
}
