package com.qdwang.enchttp.http;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * author: create by qdwang
 * date: 2018/10/24 18:30
 * described：
 */
public class JsonHttpListener<M> implements IHttpListener{

    private IDataCallBack<M> dataCallBack;
    private Class<M> responseClass;
    private Gson gson = new Gson();

    Handler handler = new Handler(Looper.getMainLooper());

    public JsonHttpListener(Class<M> responseClass, IDataCallBack dataCallBack){
        this.responseClass = responseClass;
        this.dataCallBack = dataCallBack;
    }

    @Override
    public void onSuccess(InputStream inputStream) {
        String content = getContent(inputStream);
        final M response = gson.fromJson(content, responseClass);
        //切换主线程
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(dataCallBack != null){
                    dataCallBack.onSuccess(response);
                }
            }
        });
    }

    @Override
    public void onFailure() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(dataCallBack != null){
                    dataCallBack.onFalure();
                }
            }
        });
    }

    /**
     * 把输入流转成字符串
     * @param inputStream
     * @return
     */
    private String getContent(InputStream inputStream) {
        String content = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null){
                    sb.append(line+"\n");
                }
            }catch (Exception e){
                System.out.print("ERROR = " + e.toString());
            }finally {
                try {
                    inputStream.close();
                }catch (Exception e){
                    System.out.print("ERROR = " + e.toString());
                }
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return content;
    }
}
