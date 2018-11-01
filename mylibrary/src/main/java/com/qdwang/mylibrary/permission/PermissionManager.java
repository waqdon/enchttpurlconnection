package com.qdwang.mylibrary.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;


import com.qdwang.mylibrary.permission.annotation.IPermission;
import com.qdwang.mylibrary.permission.helper.PermissionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * author: create by qdwang
 * date: 2018/10/26 09:41
 * described：权限框架请求业务管理类
 */
public class PermissionManager {

    /**
     * 检查所有请求的权限是否被授予
     * @param activity
     * @param permissions 请求的权限组
     */
    public static boolean hasPermission(Activity activity, String... permissions) {
        if(activity == null){
            throw  new IllegalArgumentException("参数中的activity不能为null");
        }
        /***
         * 小于android6.0不需要动态请求权限
         */
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }
        for (String permission: permissions) {
            /**
             * 循环中发现有任一权限被拒绝就直接返回false
             */
            if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     * 申请权限
     * @param activity 当前activity
     * @param rationale 如果用户第一次拒绝请求权限，解释为什么需要这组权限的说明内容
     * @param requestCode 请求标识码
     * @param permissions 请求的一组权限
     */
    public static void requestPermissions(Activity activity, String rationale, int requestCode, String... permissions){
        /**
         * 发起请求之前先检查权限状态
         */
        if(hasPermission(activity, permissions)){
            notifyHasPermission(activity, requestCode, permissions);
            return;
        }
        /**
         * 做权限申请
         */
        PermissionHelper helper = PermissionHelper.newInstance(activity);
        helper.requestPermission(rationale, android.R.string.ok, android.R.string.cancel, requestCode, permissions);
    }

    /**
     * 如果权限都已经被授权了，则进入onRequestPermissionResult方法回调
     * @param activity 当前activity
     * @param requestCode 请求标识码
     * @param permissions  授权通过的权限组
     */
    private static void notifyHasPermission(Activity activity, int requestCode, String[] permissions) {
        /**
         * 二次检查，授权通过的权限组告知处理权限结果方法
         */
        int[] grantResults = new int[permissions.length];
        for (int i = 0; i < grantResults.length; i++) {
            grantResults[i] = PackageManager.PERMISSION_GRANTED;
        }
        onRequestPermissionResult(requestCode, permissions, grantResults, activity);
    }

    /**
     * 处理权限请求结果方法
     * 如果授予或者拒绝任何权限，将通过permissionCallback回调接受结果
     * 执行带有@IPermission注解方法
     * @param requestCode 回调授权标识码
     * @param permissions 回调权限组
     * @param grantResults  回调授权结果
     * @param activity  拥有实现permissionCallback接口或者有@IPermission注解对象的activity
     */
    public static void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults, Activity activity) {
        /**
         * 定义两个集合接受授权和拒绝
         */
        List<String> granted = new ArrayList<>();//通过
        List<String> denied = new ArrayList<>();//拒绝
        for (int i = 0; i < permissions.length; i++) {
            //遍历请求结果，分类加入集合
            String permission = permissions[i];
            if(grantResults[i] ==PackageManager.PERMISSION_GRANTED){
                granted.add(permission);
            }else {
                denied.add(permission);
            }
            /**
             * 回调授权通过的结果
             */
            if(!granted.isEmpty()){
                if(activity instanceof IPermissionCallback){
                    ((IPermissionCallback)activity).onPermissionGranted(requestCode, granted);
                }
            }
            /**
             * 回调授权拒绝的结果
             */
            if(!denied.isEmpty()){
                if(activity instanceof IPermissionCallback){
                    ((IPermissionCallback)activity).onPermissionDeined(requestCode, denied);
                }
            }

            /**
             * 如果授权全部通过，才能执行注解的方法，哪怕一下权限拒绝都不行
             */
            if(!granted.isEmpty() && denied.isEmpty()){
                reflectAnnotationMethod(activity, requestCode);
            }
        }
    }

    /**
     *  找到指定的activity，有IPermission注解方法，并且请求方法正确的方法才执行
     * @param activity
     * @param requestCode
     */
    private static void reflectAnnotationMethod(Activity activity, int requestCode) {
        //获取类
        Class<? extends Activity> clazz = activity.getClass();
        //获取类的所有的方法
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method: methods) {
            //如果方法是IPermission注解
            if(method.isAnnotationPresent(IPermission.class)){
                //获取注解
                IPermission iPermission = method.getAnnotation(IPermission.class);
                //如果注解的值==请求标识码（二次匹配）
                if(iPermission.value() == requestCode){
                    //方法必须返回void(三次匹配)
                    if(method.getParameterTypes().length > 0){
                        throw new RuntimeException("不能执行未知方法，方法必须返回void");
                    }
                    if(method.isAccessible()){
                        //如果方法是私有的，可以设置访问
                        method.setAccessible(true);
                    }
                    try {
                        method.invoke(activity);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 检查被拒绝的权限组中是否有点击了“不在询问”
     * @param activity
     * @param permissions
     * @return 如果有任一“不在询问”的权限返回true， 反之false
     */
    public static boolean somePermissionPermanentlyDeined(Activity activity, List<String> permissions) {
        return PermissionHelper.newInstance(activity).somePermissionPermanentlyDenied(permissions);
    }
}
