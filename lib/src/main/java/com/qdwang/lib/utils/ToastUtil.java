package com.qdwang.lib.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * author: create by qdwang
 * date: 2018/9/5 10:26
 * described：
 */
public class ToastUtil {

    public static void showToast(Context context, int strRes){
        Toast.makeText(context, context.getString(strRes), Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String string){
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
}
