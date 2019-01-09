package com.qdwang.lib.net.parser;

import com.google.gson.Gson;

import java.util.Map;

/**
 * author: create by qdwang
 * date: 2018/9/4 16:14
 * described：
 */
public interface IParser<T> {

    Gson getGson();

    String mapToJson(Map<String, String> map);

    <T> T fromJson(String json, Class<T> classOfT);
}
