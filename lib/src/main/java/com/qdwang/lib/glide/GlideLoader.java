package com.qdwang.lib.glide;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.*;
import com.bumptech.glide.request.RequestOptions;
import com.qdwang.lib.glide.transformation.BlurTransformation;

/**
 * author: create by qdwang
 * date: 2018/9/14 10:46
 * described：二次封装glide
 */
public class GlideLoader implements IGlideLoader{

    private static GlideLoader glideLoader = new GlideLoader();
    private static RequestManager requestManager;
    private static Context mContext;

    public static GlideLoader with(Context context){
        mContext = context;
        requestManager = Glide.with(context);
        return glideLoader;
    }

    public static GlideLoader with(Activity activity){
        mContext = activity;
        requestManager = Glide.with(activity);
        return glideLoader;
    }

    public static GlideLoader with(FragmentActivity activity){
        mContext = activity;
        requestManager = Glide.with(activity);
        return glideLoader;
    }

    public static GlideLoader with(Fragment fragment){
        mContext = fragment.getContext();
        requestManager = Glide.with(fragment);
        return glideLoader;
    }

    public static GlideLoader with(android.app.Fragment fragment){
        mContext = fragment.getActivity();
        requestManager = Glide.with(fragment);
        return glideLoader;
    }

    public static GlideLoader with(View view){
        mContext = view.getContext();
        requestManager = Glide.with(view);
        return glideLoader;
    }

    @Override
    public void loadCircleCrop(ImageView imageView, String url, int resDrawable) {
        requestManager.asBitmap().load(url).apply(RequestOptions.placeholderOf(resDrawable).bitmapTransform(new CircleCrop())).into(imageView);
    }

    @Override
    public void loadFitCenter(ImageView imageView, String url, int resDrawable) {
        requestManager.asBitmap().load(url).apply(RequestOptions.placeholderOf(resDrawable).bitmapTransform(new FitCenter())).into(imageView);
    }

    @Override
    public void loadCenterCrop(ImageView imageView, String url, int resDrawable) {
        requestManager.asBitmap().load(url).apply(RequestOptions.placeholderOf(resDrawable).bitmapTransform(new CenterCrop())).into(imageView);
    }

    @Override
    public void loadCenterInside(ImageView imageView, String url, int resDrawable){
        requestManager.asBitmap().load(url).apply(RequestOptions.placeholderOf(resDrawable).bitmapTransform(new CenterInside())).into(imageView);
    }

    @Override
    public void loadRoundedCorners(ImageView imageView, int roundingRadius, String url, int resDrawable) {
        requestManager.asBitmap().load(url).apply(RequestOptions.placeholderOf(resDrawable).bitmapTransform(new RoundedCorners(roundingRadius))).into(imageView);
    }

    @Override
    public void loadBlurTransformation(ImageView imageView, String url, int resDrawable) {
        requestManager.asBitmap().load(url).apply(RequestOptions.placeholderOf(resDrawable).bitmapTransform(new BlurTransformation(mContext))).into(imageView);
    }

    @Override
    public void loadBlurTransformation(ImageView imageView, String url, int radius, int resDrawable) {
        requestManager.asBitmap().load(url).apply(RequestOptions.placeholderOf(resDrawable).bitmapTransform(new BlurTransformation(mContext, radius))).into(imageView);
    }

    @Override
    public void loadBlurTransformation(ImageView imageView, String url, int radius, int sampling, int resDrawable) {
        requestManager.asBitmap().load(url).apply(RequestOptions.placeholderOf(resDrawable).bitmapTransform(new BlurTransformation(mContext, radius, sampling))).into(imageView);
    }
}
