package com.qdwang.enchttp.http;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * author: create by qdwang
 * date: 2018/11/13 17:13
 * described：
 */
public class FileHttpService implements IHttpService {

    String url;
    byte[] requestData;
    IHttpListener httpListener;
    private HttpURLConnection urlConnection = null;

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setRequest(byte[] request) {
        this.requestData = request;
    }

    @Override
    public void request() {

    }

    @Override
    public void setHttpCallBack(IHttpListener listener) {
        this.httpListener = listener;
    }


}
