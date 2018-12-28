package com.qdwang.mylibrary.downfile.download;

import android.support.annotation.NonNull;

import java.security.SignedObject;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * author: create by qdwang
 * date: 2018/11/13 15:11
 * described：自定义的线程池
 */
public class ThreadPool {

    //cpu核心数
    private int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //可同时下载任务数（核心线程数）
    private int CORE_POOL_SIZE = 3;
    //缓存队列的大小（最大线程数）
    private int MAX_POOL_SIZE = 20;
    //非核心线程闲置的超时时间（秒）如果超时则会被回收
    private long KEEP_ALIVE = 10L;
    private ThreadPoolExecutor THREAD_POOL_EXCUTOR;

    private ThreadPool(){
    }

    private ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger count = new AtomicInteger();
        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "download_task#"+count.getAndIncrement());
        }
    };

    public static ThreadPool getInstance(){
        return SingletonHolder.threadPool;
    }

    private static class SingletonHolder{
        private final static ThreadPool threadPool = new ThreadPool();
    }

    public void setCorePoolSize(int corePoolSize) {
        if(corePoolSize == 0){
            return;
        }
        this.CORE_POOL_SIZE=corePoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        if(maxPoolSize == 0){
            return;
        }
        this.MAX_POOL_SIZE=maxPoolSize;
    }

    public int getCorePoolSize() {
        return CORE_POOL_SIZE;
    }

    public int getMaxPoolSize() {
        return MAX_POOL_SIZE;
    }

    public ThreadPoolExecutor getThreadPoolExcutor() {
        if(THREAD_POOL_EXCUTOR == null){
            THREAD_POOL_EXCUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
        }
        return THREAD_POOL_EXCUTOR;
    }
}
