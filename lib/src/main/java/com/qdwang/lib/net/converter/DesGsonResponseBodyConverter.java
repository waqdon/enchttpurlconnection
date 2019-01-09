package com.qdwang.lib.net.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.qdwang.lib.net.des.EncipherProxy;

import okhttp3.ResponseBody;
import retrofit2.Converter;

import java.io.Reader;
import java.io.StringReader;

/**
 * author: create by qdwang
 * date: 2018/9/4 17:24
 * described：
 */
public class DesGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final TypeAdapter<T> adapter;
    DesGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value){
        //TODO 处理出参解密
        try {
            String response = EncipherProxy.decrypt(value.string());
            Reader reader = new StringReader(response);
            JsonReader jsonReader = gson.newJsonReader(reader);
            return adapter.read(jsonReader);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            value.close();
        }
        return null;
    }
}
