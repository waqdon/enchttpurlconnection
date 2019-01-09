package com.qdwang.lib.net.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.qdwang.lib.net.parser.GsonParser;
import com.qdwang.lib.utils.LogUtils;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class StringConverterFactory extends Converter.Factory {
    public static StringConverterFactory create() {
        return create(GsonParser.getInstance().getGson());
    }

    public static StringConverterFactory create(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new StringConverterFactory(gson);
    }

    private final Gson gson;

    private StringConverterFactory(Gson gson) {
        this.gson = gson;
    }
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new DesGsonResponseBodyConverter<>(gson, adapter);
    }

    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        LogUtils.e("RxHttp", "StringConverterFactory stringConverter ");
        return new StringRequestBodyConverter();
    }
}
