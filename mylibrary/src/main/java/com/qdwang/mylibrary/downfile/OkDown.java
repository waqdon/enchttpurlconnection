package com.qdwang.mylibrary.downfile;

import android.content.Context;

/**
 * author: create by qdwang
 * date: 2018/11/12 15:18
 * described：下载统一入口类
 */
public class OkDown {

    private Context context;
    private String url; //下载链接
    private String path; //下载路径
    private String name; //文件名
    private String childTaskCount; //单个任务采用几个线程下载

    private void build(OkDownBuilder okDownBuilder) {
        this.context = okDownBuilder.context;
        this.url = okDownBuilder.url;
        this.path = okDownBuilder.path;
        this.name = okDownBuilder.name;
        this.childTaskCount = okDownBuilder.childTaskCount;
    }



    public static class OkDownBuilder{

        private Context context;
        private String url; //下载链接
        private String path; //下载路径
        private String name; //文件名
        private String childTaskCount; //单个任务采用几个线程下载

        public OkDownBuilder(Context context) {
            this.context = context.getApplicationContext();
        }

        public OkDownBuilder setUrl(String url) {
            this.url=url;
            return this;
        }

        public OkDownBuilder setPath(String path) {
            this.path=path;
            return this;
        }

        public OkDownBuilder setName(String name) {
            this.name=name;
            return this;
        }

        public OkDownBuilder setChildTaskCount(String childTaskCount) {
            this.childTaskCount=childTaskCount;
            return this;
        }
    }
}
