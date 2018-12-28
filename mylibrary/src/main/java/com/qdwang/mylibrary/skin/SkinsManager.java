package com.qdwang.mylibrary.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * author: create by qdwang
 * date: 2018/11/15 11:49
 * described：换肤管理类
 */
public class SkinsManager {
    private static class SkinManagerSingle{
        private final static SkinsManager outSingle = new SkinsManager();
    }

    public static SkinsManager getInstance(){
        return SkinManagerSingle.outSingle;
    }

    private Context context;
    private String skinPackageName;
    private Resources skinResource;
    private SkinFactory skinFactory;

    private SkinsManager(){}

    /**
     * 使用application的上下文防止内存泄漏
     * @param context
     */
    public void init(Context context){
        this.context = context.getApplicationContext();
        skinFactory = new SkinFactory();
        LayoutInflater.from(context).setFactory(skinFactory);
    }

    public SkinFactory getSkinFactory() {
        return skinFactory;
    }

    /**
     * 加载主题包
     * @param apkPath
     */
    public void loadSkin(String apkPath){
        try {
            if(TextUtils.isEmpty(apkPath)){
                skinResource = null;
                skinPackageName = null;
                return;
            }
            File file = new File(apkPath);
            if(!file.exists()){
                skinResource = null;
                skinPackageName = null;
                return;
            }
            AssetManager assetManager=AssetManager.class.newInstance();
            Method method = AssetManager.class.getMethod("addAssetPath", new Class[]{String.class});
            method.invoke(assetManager, apkPath);
            skinPackageName = context.getPackageManager().getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES).packageName;
            skinResource = new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (NoSuchMethodException e){
            e.printStackTrace();
        }catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public int getColor(int id){
        int color = context.getResources().getColor(id);
        if(skinResource != null){
            String resName = skinResource.getResourceEntryName(id);
            int resId = skinResource.getIdentifier(resName, "color", skinPackageName);
            if(resId > 0){
                return skinResource.getColor(resId);
            }
        }
        return color;
    }

    public Drawable getDrawable(int id){
        Drawable drawable = context.getResources().getDrawable(id);
        if(skinResource != null){
            String resName = skinResource.getResourceEntryName(id);
            int resId = skinResource.getIdentifier(resName, "drawable", skinPackageName);
            if(resId>0){
                return skinResource.getDrawable(id);
            }
        }
        return drawable;
    }


}
