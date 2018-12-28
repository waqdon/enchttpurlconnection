package com.qdwang.mylibrary.downfile.download;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.qdwang.mylibrary.downfile.DownloadUtils;
import com.qdwang.mylibrary.downfile.Constans;
import com.qdwang.mylibrary.downfile.db.Db;

/**
 * author: create by qdwang
 * date: 2018/11/12 17:45
 * described：断点续传的状态
 */
public class DownloadProgressHandler {
    private String url;
    private String path;
    private String name;
    private int childTaskCount;
    private Context context;
    private DownloadCallback downloadCallback;
    private DownloadData downloadData;
    private int currentState =Constans.NONE;
    private FileTask flieTask;
    private boolean isSuportRange;
    private int currentLength = 0;
    private int totalFileLength;
    private int tempChildTaskCount = 0;
    private long lastProgressTime = 0;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int lastState = currentState;
            currentState = msg.what;
            downloadData.currentLength = currentLength;
            switch (currentState){
                case Constans.START:
                    //开始下载
                    Bundle bundle = msg.getData();

                    totalFileLength = bundle.getInt("totalFileLength");
                    currentLength = bundle.getInt("currentLength");
                    isSuportRange = bundle.getBoolean("isSuportRange");
                    String lastModify = bundle.getString("lastModify");
                    if(isSuportRange){
                        childTaskCount = 1;
                    }else if(currentLength == 0){
                        Db.getInstance(context).insertData(new DownloadData(url, path, name, currentLength, totalFileLength, childTaskCount, System.currentTimeMillis()));
                    }
                    if(downloadCallback != null){
                        downloadCallback.onStart(currentLength, totalFileLength, DownloadUtils.getPerentage(currentLength, totalFileLength));
                    }
                    break;
                case Constans.PROGRESS:
                    synchronized (this){
                        currentLength += msg.arg1;
                        downloadData.percentage = DownloadUtils.getPerentage(currentLength, totalFileLength);
                        if(downloadCallback != null || System.currentTimeMillis() - lastProgressTime >= 20 || currentLength == totalFileLength){
                            downloadCallback.onProgress(currentLength, totalFileLength, DownloadUtils.getPerentage(currentLength, totalFileLength));
                            lastProgressTime = System.currentTimeMillis();
                        }
                        if(currentLength == totalFileLength){
                            sendEmptyMessage(Constans.FINISH);
                        }
                    }
                    break;
//                case Constans.START:
//                    break;
//                case Constans.START:
//                    break;
//                case Constans.START:
//                    break;
//                case Constans.START:
//                    break;
//                case Constans.START:
//                    break;
//                case Constans.START:
//                    break;
                default:
                    break;
            }
        }
    };

    public DownloadProgressHandler(Context context, DownloadData downloadData) {
        this.context=context;
        this.downloadData=downloadData;
        this.url=downloadData.url;
        this.path=downloadData.path;
        this.name=downloadData.name;
        this.childTaskCount=downloadData.childTaskCount;
        DownloadData data = Db.getInstance(context).getData(url);
        this.downloadData = data == null?this.downloadData:data;
    }

    public Handler getHandler() {
        return handler;
    }

    public int getCurrentState() {
        return currentState;
    }

    public DownloadData getDownloadData() {
        return downloadData;
    }

    public void setFlieTask(FileTask flieTask) {
        this.flieTask=flieTask;
    }
}
