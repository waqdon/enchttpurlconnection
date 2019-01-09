package com.qdwang.lib.net.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import com.qdwang.lib.net.RequestBodyParams;
import com.qdwang.lib.net.des.EncipherProxy;
import com.qdwang.lib.utils.LogUtils;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * author: create by qdwang
 * date: 2018/9/4 17:25
 * described：
 */
public class DesGsonRequestBodyConverter<T> implements Converter<T, RequestBody> {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    DesGsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public RequestBody convert(T value) {
        //TODO 处理入参加密
        RequestBodyParams params = (RequestBodyParams) value;
        String json = params.jsonParams;
        LogUtils.e("RxHttp", "params = " + json);
        try {
            params.jsonParams = EncipherProxy.encrypt(json);
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            JsonWriter jsonWriter = gson.newJsonWriter(writer);
            adapter.write(jsonWriter, (T) params);
            jsonWriter.close();
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
