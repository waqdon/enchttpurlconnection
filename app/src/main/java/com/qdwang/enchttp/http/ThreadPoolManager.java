package com.qdwang.enchttp.http;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * author: create by qdwang
 * date: 2018/10/23 15:31
 * described：单例
 */
public class ThreadPoolManager {
    private static final ThreadPoolManager ourInstance = new ThreadPoolManager();
    private ThreadPoolExecutor threadPoolExecutor;

    public static ThreadPoolManager getInstance() {
        return ourInstance;
    }

    private ThreadPoolManager() {
        threadPoolExecutor = new ThreadPoolExecutor(5, 20, 15, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                try {
                    queue.put(r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadPoolExecutor.execute(runnable);
    }

    private LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

    public void execute(Runnable runnable){
        try {
            if(runnable !=null){
                queue.put(runnable);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true){
                try {
                    Runnable taskRunnable = queue.take();
                    if (taskRunnable != null){
                        threadPoolExecutor.execute(taskRunnable);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
