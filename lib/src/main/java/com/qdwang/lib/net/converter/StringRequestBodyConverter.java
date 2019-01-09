package com.qdwang.lib.net.converter;

import com.qdwang.lib.net.des.EncipherProxy;
import com.qdwang.lib.utils.LogUtils;

import okhttp3.MediaType;
import retrofit2.Converter;

import java.lang.annotation.Annotation;
import java.nio.charset.Charset;
import java.util.HashMap;

public class StringRequestBodyConverter implements Converter<String, String> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private Annotation[] annotations;
    private HashMap<String, Annotation> map = new HashMap<>();
    public StringRequestBodyConverter() {
    }

    @Override
    public String convert(String value){

        LogUtils.e("RxHttp", "StringRequestBodyConverter params = " + value);
        if(value.contains("gusuier/api/")){
            return value;
        }
        try {
            String jsonParams = EncipherProxy.encrypt(value);
            return jsonParams;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
