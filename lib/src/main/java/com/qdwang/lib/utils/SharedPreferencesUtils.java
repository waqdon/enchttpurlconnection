package com.qdwang.lib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Set;

/**
 * author: create by qdwang
 * date: 2018/9/6 13:43
 * described：封装的sp存取工具类
 */
public class SharedPreferencesUtils {
    private Context context;
    static SharedPreferencesUtils utils;

    public static SharedPreferencesUtils getInstance(Context context) {
        if (utils != null) {
            return utils;
        } else {
            utils = new SharedPreferencesUtils(context);
            return utils;
        }
    }

    public SharedPreferencesUtils(Context context) {
        this.context = context;
    }

    public String shareReadString(String key, String defaultVal) {
        SharedPreferences sharedata = this.context.getSharedPreferences("data", 0);
        return sharedata.getString(key, defaultVal);
    }

    public void shareWriteString(String key, String val) {
        SharedPreferences.Editor sharedata = this.context.getSharedPreferences("data", 0).edit();
        sharedata.putString(key, val);
        sharedata.commit();
    }

    public int shareReadInt(String key, int defaultVal) {
        SharedPreferences sharedata = this.context.getSharedPreferences("data", 0);
        return sharedata.getInt(key, defaultVal);
    }

    public void shareWriteInt(String key, int val) {
        SharedPreferences.Editor sharedata = this.context.getSharedPreferences("data", 0).edit();
        sharedata.putInt(key, val);
        sharedata.commit();
    }

    public boolean shareReadBoolean(String key, boolean defaultVal) {
        SharedPreferences sharedata = this.context.getSharedPreferences("data", 0);
        return sharedata.getBoolean(key, defaultVal);
    }

    public void shareWriteBoolean(String key, boolean val) {
        SharedPreferences.Editor sharedata = this.context.getSharedPreferences("data", 0).edit();
        sharedata.putBoolean(key, val);
        sharedata.commit();
    }

    public Set<String> shareReadStringSet(String key, Set<String> set) {
        SharedPreferences sharedata = this.context.getSharedPreferences("data", 0);
        return sharedata.getStringSet(key, set);
    }

    public void shareWriteStringSet(String key, Set<String> set) {
        SharedPreferences.Editor sharedata = this.context.getSharedPreferences("data", 0).edit();
        sharedata.putStringSet(key, set);
        sharedata.commit();
    }

    public long shareReadLong(String key, long defaultVal) {
        SharedPreferences sharedata = this.context.getSharedPreferences("data", 0);
        return sharedata.getLong(key, defaultVal);
    }

    public void shareWriteLong(String key, long val) {
        SharedPreferences.Editor sharedata = this.context.getSharedPreferences("data", 0).edit();
        sharedata.putLong(key, val);
        sharedata.commit();
    }
}
