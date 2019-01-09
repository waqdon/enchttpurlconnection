package com.qdwang.lib.utils.autolayout;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;

/**
 * author: create by qdwang
 * date: 2018/11/14 11:32
 * described：
 */
public enum AutoDensityUtil {

    INSTANCE;

    private float sNoncompatDensity = 0f;
    private float sNoncompatScaledDensity = 0f;

    /**
     * 该方法的适配方案由今日头条技术团队开源提供（此方法是以宽适配）
     * 默认以宽适配
     */
    public void setCustomDesnsity(Activity activity, AutoData autoData) {
        final Application application = activity.getApplication();
        DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
//        if (sNoncompatDensity == 0f) {
        sNoncompatDensity = appDisplayMetrics.density;
        sNoncompatScaledDensity = appDisplayMetrics.scaledDensity;
        application.registerComponentCallbacks(new ComponentCallbacks() {
            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                if (newConfig != null && newConfig.fontScale > 0) {
                    sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                }
            }

            @Override
            public void onLowMemory() {

            }
        });
        float targetDensity = getTargetDensity(application,appDisplayMetrics, autoData);
        float targetScaledDensity = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity);
        int targetDensityDpi =(int) (160 * targetScaledDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
//        }
    }

    /**
     * 获取缩放后的Density
     * @param displayMetrics
     * @return
     */
    private float getTargetDensity(Application application, DisplayMetrics displayMetrics, AutoData autoData) {
        float targetDensity;
        float standardDp;
        if (autoData.isWidth()) {
            standardDp = (autoData.getWidthNum() / autoData.getMultiple());
            targetDensity = displayMetrics.widthPixels / standardDp;
        } else {
            standardDp = (autoData.getHeightNum() / autoData.getMultiple());
            if (autoData.isIgnore()) {
                targetDensity = displayMetrics.heightPixels / standardDp;
            } else {
                int statusBarHeight = -1;
                //获取status_bar_height资源的ID
                int resourceId = application.getBaseContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    //根据资源ID获取响应的尺寸值
                    statusBarHeight = application.getBaseContext().getResources().getDimensionPixelSize(resourceId);
                }
                targetDensity = (displayMetrics.heightPixels - statusBarHeight) / (standardDp - statusBarHeight / autoData.getMultiple());
            }
        }
        return targetDensity;
    }

    public void init(Application app){
        /**
         * 做ui适配--默认以宽适配
         */
        AutoLayout.getIntances().width(750).multiple(2);
        app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                AutoData autoData = getAutoData(activity);
                if (autoData == null){
                    return;
                }
                if (autoData.getWidthNum() == 0 && autoData.getHeightNum() == 0){
                    new Exception("Please set design width or height");
                }
                if (autoData.getMultiple() == 0){
                    new Exception("Please set multiple, it is very important");
                }
                AutoDensityUtil.INSTANCE.setCustomDesnsity(activity, autoData);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    /**
     * 获取用户的设置数据
     * @param activity 用户自定义的数据会覆盖全局数据
     * @return autoData
     */
    private AutoData getAutoData(Activity activity) {
        AutoData autoData = AutoLayout.getIntances().getAutoData();
        if (activity instanceof IAutoLayout){
            IAutoLayout iAutoLayout = (IAutoLayout) activity;
            autoData = iAutoLayout.custom();
        }
        return autoData;
    }
}
