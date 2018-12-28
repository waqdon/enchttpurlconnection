package com.qdwang.mylibrary.skin;

import android.app.Activity;

/**
 * author: create by qdwang
 * date: 2018/11/15 13:50
 * described：
 */
public class SkinUtils {

    public static int getStatusBarColorResId(Activity activity){
        int resId = activity.getResources().getIdentifier("colorPrimaryDark", "color", activity.getPackageName());
        return resId;
    }
}
