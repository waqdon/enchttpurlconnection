package com.qdwang.lib.glide;

import android.widget.ImageView;

/**
 * author: create by qdwang
 * date: 2018/9/14 11:16
 * described：
 */
public interface IGlideLoader {

    /**
     * 加载圆形图片
     * @param imageView
     * @param url
     */
    void loadCircleCrop(ImageView imageView, String url, int resDrawable);

    void loadFitCenter(ImageView imageView, String url, int resDrawable);

    void loadCenterCrop(ImageView imageView, String url, int resDrawable);

    void loadCenterInside(ImageView imageView, String url, int resDrawable);

    /**
     * 加载圆角图片
     * @param imageView
     * @param roundingRadius 角度
     * @param url
     */
    void loadRoundedCorners(ImageView imageView, int roundingRadius, String url, int resDrawable);

    /**
     * 加载高斯模糊图片
     * @param imageView
     * @param url
     */
    void loadBlurTransformation(ImageView imageView, String url, int resDrawable);

    void loadBlurTransformation(ImageView imageView, String url, int radius, int resDrawable);

    void loadBlurTransformation(ImageView imageView, String url, int radius, int sampling, int resDrawable);
}
