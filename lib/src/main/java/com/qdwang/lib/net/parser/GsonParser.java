package com.qdwang.lib.net.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

/**
 * author: create by qdwang
 * date: 2018/9/4 16:13
 * described：gson解析器
 */
public class GsonParser<T> implements IParser<T> {

    private  static IParser parser;
    private GsonParser(){}
    public static IParser getInstance(){
        if(parser==null){
            synchronized (GsonParser.class){
                if(parser==null){
                    parser=new GsonParser();
                }
            }
        }
        return parser;
    }

    private static Gson gson;

    static {
        gson = new GsonBuilder()
                .setPrettyPrinting() // 格式化json字符串
                .disableHtmlEscaping()
                .create();
    }

    @Override
    public Gson getGson() {
        return gson;
    }

    @Override
    public String mapToJson(Map<String, String> map) {
        return gson.toJson(map);
    }

    @Override
    public <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }
}
