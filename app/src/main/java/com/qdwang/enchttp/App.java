package com.qdwang.enchttp;

import android.app.Application;

import com.qdwang.mylibrary.skin.SkinFactory;
import com.qdwang.mylibrary.skin.SkinsManager;

/**
 * author: create by qdwang
 * date: 2018/11/15 16:15
 * described：
 */
public class App extends Application {

    private static App instance;
    public SkinFactory skinFactory;

    public static App getApp(){
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        skinFactory = new SkinFactory();
//        LayoutInflater.from(this).setFactory(skinFactory);
        super.onCreate();
        SkinsManager.getInstance().init(this);
    }
}
