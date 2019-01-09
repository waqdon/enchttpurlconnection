package com.qdwang.lib.utils.autolayout;

/**
 * author: create by qdwang
 * date: 2018/11/14 11:31
 * described：
 */
public class AutoLayout {
    private static final AutoLayout ourInstance = new AutoLayout();
    private final AutoData autoData = new AutoData();

    public static AutoLayout getIntances() {
        return ourInstance;
    }

    private AutoLayout() {
    }


    public AutoLayout width(int width){
        autoData.setWidthNum(width);
        autoData.setWidth(true);
        return this;
    }

    public AutoLayout height(int height){
        autoData.setHeightNum(height);
        autoData.setHeight(true);
        return this;
    }

    public AutoLayout ignore(){
        autoData.setIgnore(true);
        return this;
    }

    public AutoLayout multiple(int multiple){
        autoData.setMultiple(multiple);
        return this;
    }

    public AutoData getAutoData(){
        return autoData;
    }
}
