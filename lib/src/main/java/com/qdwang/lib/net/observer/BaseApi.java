package com.qdwang.lib.net.observer;

import com.google.gson.Gson;
import com.qdwang.lib.net.CommonParams;
import com.qdwang.lib.net.RequestBodyParams;
import com.qdwang.lib.net.parser.GsonParser;
import io.reactivex.Observable;

import java.util.HashMap;
import java.util.Map;


/**
 * author: create by qdwang
 * date: 2018/9/4 15:15
 * described：
 */
public class BaseApi {

    /**
     * 处理入参在这里，公参也统一在该方法中处理
     * @param map 业务入参
     * @return
     */
    public static RequestBodyParams toReBody(Map<String, String> map) {
        RequestBodyParams params;
        Map<String, String> mm = new HashMap<>();
        Map<String, String> allMap = new HashMap<>();
        String commonParams = CommonParams.getInstances().getCommonParams();
        mm.put("commonParams", commonParams);
        if (map != null && !map.isEmpty()) {
            String dataParame = new Gson().toJson(map);
            mm.put("dataParams", dataParame);
        }
        allMap.put("jsonParams", GsonParser.getInstance().mapToJson(mm));
        String json = GsonParser.getInstance().mapToJson(allMap);
        params =(RequestBodyParams) GsonParser.getInstance().fromJson(json, RequestBodyParams.class);
        return params;
    }

    public static String toReString(Map<String, String> map) {
        Map<String, String> mm = new HashMap<>();
        String commonParams = CommonParams.getInstances().getCommonParams();
        mm.put("commonParams", commonParams);
        if (map != null && !map.isEmpty()) {
            String dataParame = new Gson().toJson(map);
            mm.put("dataParams", dataParame);
        }
        String json = GsonParser.getInstance().mapToJson(mm);
        try {
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public Observable getObservable(Observable obsvable){
            return new ObserableBuilder(obsvable)
//                    .addApiException()
                    .build();
        }
    }
