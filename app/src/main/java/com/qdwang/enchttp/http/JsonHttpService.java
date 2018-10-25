package com.qdwang.enchttp.http;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * author: create by qdwang
 * date: 2018/10/24 18:09
 * described：
 */
public class JsonHttpService implements IHttpService {

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
    public void setHttpCallBack(IHttpListener listener) {
        this.httpListener = listener;
    }

    /**
     * 实现真正的网络操作
     */
    @Override
    public void request() {
        httpURLConnection();
    }

    private void httpURLConnection(){
        URL url;
        try {
            url = new URL(this.url);
            urlConnection =(HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);//连接的超时时间
            urlConnection.setUseCaches(false);//不使用缓
            urlConnection.setInstanceFollowRedirects(true);//是成员函数，仅作用于当前函数，设置
            urlConnection.setReadTimeout(5000);//响应的超时时间
            urlConnection.setDoInput(true);//设置这个连接是否可以写入数据
            urlConnection.setDoOutput(true);//设置这个连接是否可以输出数据
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.connect();
            //------------------使用自字节流发送数据----------------------------------
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            if (requestData != null){
                bos.write(requestData);
            }
            //把这个字节数组的数据写入到缓存区
            bos.flush();
            outputStream.close();
            bos.close();
            //----------------------------字符流写入数据-------------------------------
            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream in = urlConnection.getInputStream();
                httpListener.onSuccess(in);
            }
        }catch (Exception e){
            e.printStackTrace();
            httpListener.onFailure();
        }finally {
            //使用完关闭tcp连接，释放资源
            urlConnection.disconnect();
        }
    }
}
