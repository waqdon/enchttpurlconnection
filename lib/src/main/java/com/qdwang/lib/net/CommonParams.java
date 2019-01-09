package com.qdwang.lib.net;

import android.content.Context;
import com.google.gson.Gson;
import com.qdwang.lib.utils.AppUtils;
import com.qdwang.lib.utils.DeviceUtils;
import com.qdwang.lib.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * author: create by qdwang
 * date: 2018/9/6 13:41
 * described：公共参数封装类
 */
public class CommonParams {
    private Context context;

    private static CommonParams instances;

    private CommonParams() {
    }

    public static synchronized CommonParams getInstances() {
        if (instances == null) {
            instances = new CommonParams();
        }
        return instances;
    }

    /**
     * 在调用接口前初始化上下文，供请求接口获取公共参数
     *
     * @param context
     */
    public void initCommonParams(Context context) {
        this.context = context;
    }

    /**
     * 获取请求接口的封装的公共参数
     *
     * @return
     */
    public String getCommonParams() {
        Map<String, String> map = new HashMap<>();
        map.put("token", SharedPreferencesUtils.getInstance(context).shareReadString("token", ""));
        map.put("version", AppUtils.getInstances().getVersionName(context));
        map.put("deviceCode", DeviceUtils.getInstances().getUniqueId(context));
        map.put("sourceType", "1");
        map.put("os", DeviceUtils.getOsVersion());
        map.put("channel", "");
        return new Gson().toJson(map);
    }
}
