package com.qdwang.mylibrary.skin;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * author: create by qdwang
 * date: 2018/11/15 10:18
 * described：换肤
 */
public class SkinFactory implements LayoutInflater.Factory{

    private Activity activity;
    private static final String[] VIEW_PREFIX = {"android.view.", "android.widget.", "android.webkit."};
    private List<SkinItem> skinItems = new ArrayList<>();

    public void setFactory(Activity activity){
        this.activity = activity;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = createView(name, context, attrs);
        Log.e("qdwang=========", ",  name = " + name + ", view = " + view);
        if(view != null){
            parseSkinAttr(view, context, attrs);
        }
        return view;
    }

    private View createView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if(name.contains(".")){
            //自定义的view
            view = getView(name, context, attrs);
        }else {
            //系统自带的view
            if ("View".equals(name)){
                view = getView(VIEW_PREFIX[0]+name, context, attrs);
            }else if ("WebView".equals(name)){
                view = getView(VIEW_PREFIX[2] + name, context, attrs);
            }else {
                view = getView(VIEW_PREFIX[1] + name, context, attrs);
            }
//            for (int i=0; i < VIEW_PREFIX.length; i++) {
//                String trueName = VIEW_PREFIX[i]+name;
//                Log.e("qdwang=========", ",  name = " + name + ", trueName = " + trueName);
//                view = getView(trueName, context, attrs);
//            }
        }
        return view;
    }

    /**
     * 反射得到xml布局中的view
     * @param name 类的全名
     * @param context
     * @param attrs
     * @return
     */
    private View getView(String name, Context context, AttributeSet attrs) {
        View view = null;
        try {
            Class<?> clazz=context.getClassLoader().loadClass(name);
            try {
                Constructor<?> constructor = clazz.getConstructor(new Class[]{Context.class, AttributeSet.class});
                try {
                    view = (View) constructor.newInstance(context, attrs);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 收集需要换肤的view以及对应的资源
     * @param view 需要换肤的view
     * @param context 上下文
     * @param attrs 需要换肤的资源属性
     */
    private void parseSkinAttr(View view, Context context, AttributeSet attrs){
        List<SkinAttr> skinAttrs = new ArrayList<>();
        for (int i=0; i < attrs.getAttributeCount(); i++) {
            String attributeName = attrs.getAttributeName(i);
            String attributeValue = attrs.getAttributeValue(i);
            if(attributeValue.startsWith("@")){
                int id = Integer.valueOf(attributeValue.substring(1));
                String resourceTypeName = context.getResources().getResourceTypeName(id);
                String resourceEntryName = context.getResources().getResourceEntryName(id);
                SkinAttr skinAttr = new SkinAttr(id, attributeName, resourceTypeName, resourceEntryName);
                skinAttrs.add(skinAttr);
            }
        }
        if(!skinAttrs.isEmpty()){
            SkinItem skinItem = new SkinItem(view, skinAttrs);
            skinItems.add(skinItem);
        }
    }

    public void apply(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if(activity!=null){
                int resId = SkinUtils.getStatusBarColorResId(activity);
                activity.getWindow().setStatusBarColor(SkinsManager.getInstance().getColor(resId));
            }
        }
        for (SkinItem skinItem : skinItems) {
            skinItem.apply();
        }
    }

}
