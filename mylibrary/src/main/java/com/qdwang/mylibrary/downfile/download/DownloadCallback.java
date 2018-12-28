package com.qdwang.mylibrary.downfile.download;

import java.io.File;

/**
 * author: create by qdwang
 * date: 2018/11/12 17:49
 * described：
 */
public interface DownloadCallback {

    /**
     * 开始下载
     * @param currentSize
     * @param totalSize
     * @param progress
     */
    void onStart(long currentSize, long totalSize, float progress);

    /**
     * 正在下载
     * @param currentSize
     * @param totalSize
     * @param progress
     */
    void onProgress(long currentSize, long totalSize, float progress);

    /**
     * 暂停下载
     */
    void onPause();

    /**
     * 取消下载
     */
    void onCancel();

    /**
     * 下载完成
     * @param file
     */
    void onFinish(File file);

    /**
     * 线程等待下载
     */
    void onWait();

    /**
     * 下载错误
     * @param error
     */
    void onError(String error);
}
