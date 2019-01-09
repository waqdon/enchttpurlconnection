package com.qdwang.lib.glide;

import android.content.Context;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;


/**
 * author: create by qdwang
 * date: 2018/9/19 14:44
 * described：关于glide缓存的一些配置
 */
public class GseAppGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
//        //设置磁盘缓存大小
//        int size = 100 * 1024 * 1024;
//        String imageCacheDir = context.getExternalCacheDir() + "/image/";
//        //设置磁盘缓存
//        builder.setDiskCache(new DiskLruCacheFactory(imageCacheDir, size));

        //设置图片池
        int bitmapPoolSizeBytes = 1024 * 1024 * 30; // 30mb
        builder.setBitmapPool(new LruBitmapPool(bitmapPoolSizeBytes));
        //设置内存缓存大小
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        builder.setMemoryCache(new LruResourceCache(maxMemory / 8));
        //设置磁盘缓存大小
        int diskCacheSizeBytes = 1024 * 1024 * 200;  //100 MB
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));
    }
}