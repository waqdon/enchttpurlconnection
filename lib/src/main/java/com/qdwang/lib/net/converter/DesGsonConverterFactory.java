package com.qdwang.lib.net.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.qdwang.lib.net.parser.GsonParser;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * author: create by qdwang
 * date: 2018/9/4 17:15
 * described：重写retrofit需要的解析工厂然后添加加密和解密
 */
public class DesGsonConverterFactory extends Converter.Factory{

    public static DesGsonConverterFactory create() {
        return create(GsonParser.getInstance().getGson());
    }

    public static DesGsonConverterFactory create(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new DesGsonConverterFactory(gson);
    }

    private final Gson gson;

    private DesGsonConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new DesGsonRequestBodyConverter<>(gson, adapter);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new DesGsonResponseBodyConverter<>(gson, adapter);
    }
}
