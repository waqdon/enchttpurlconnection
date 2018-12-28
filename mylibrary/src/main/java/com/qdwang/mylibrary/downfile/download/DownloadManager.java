package com.qdwang.mylibrary.downfile.download;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * author: create by qdwang
 * date: 2018/11/13 14:51
 * described：
 */
public class DownloadManager {

    private Context context;
    private Map<String, DownloadProgressHandler> progressHandlerMap = new HashMap<>();
    private Map<String, DownloadCallback> downloadCallbackMap = new HashMap<>();
    private Map<String, DownloadData> downloadDataMap = new HashMap<>();
    private Map<String, FileTask> fileTaskMap = new HashMap<>();

    private volatile static DownloadManager instances;

    private DownloadData downloadData;

    private DownloadManager(Context context) {
        this.context = context;
    }

    public static DownloadManager getInstances(Context context){
        if(instances == null){
            synchronized (DownloadManager.class){
                if(instances == null){
                    instances = new DownloadManager(context);
                }
            }
        }
        return instances;
    }

    public synchronized void init(String url, String path, String name, int childTaskCount){
        downloadData = new DownloadData(url, path, name, childTaskCount);
        downloadData.url = url;
        downloadData.path = path;
        downloadData.name = name;
        downloadData.childTaskCount = childTaskCount;
    }

    public DownloadManager start(DownloadCallback downloadCallback){
        excute(downloadData, downloadCallback);
        return instances;
    }

    public DownloadManager start(DownloadData downloadData, DownloadCallback downloadCallback){
        excute(downloadData, downloadCallback);
        return instances;
    }

    public DownloadManager start(String url, DownloadCallback downloadCallback){
        excute(downloadDataMap.get(url), downloadCallback);
        return instances;
    }

    private synchronized void excute(DownloadData downloadData, DownloadCallback downloadCallback) {
        if(progressHandlerMap.get(downloadData.url) != null){
            return;
        }
        if(downloadData.childTaskCount == 0){
            downloadData.childTaskCount = 1;
        }
        DownloadProgressHandler progressHandler = new DownloadProgressHandler(context, downloadData);
        FileTask fileTask=new FileTask(context, downloadData, progressHandler.getHandler());
        progressHandler.setFlieTask(fileTask);
        downloadDataMap.put(downloadData.url, downloadData);
        downloadCallbackMap.put(downloadData.url, downloadCallback);
        fileTaskMap.put(downloadData.url, fileTask);
        progressHandlerMap.put(downloadData.url, progressHandler);
        ThreadPool.getInstance().getThreadPoolExcutor().execute(fileTask);
        if(ThreadPool.getInstance().getThreadPoolExcutor().getActiveCount() == ThreadPool.getInstance().getCorePoolSize()){
            downloadCallback.onWait();
        }
    }
}
