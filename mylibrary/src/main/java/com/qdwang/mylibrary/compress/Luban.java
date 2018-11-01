package com.qdwang.mylibrary.compress;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * author: create by qdwang
 * date: 2018/11/1 14:02
 * described：
 */
public class Luban implements Handler.Callback {

    private static final String TAG = "Luban";
    private static final String DEFAULT_DISK_CACHE_DIR = "luban_disk_cache";
    private static final int MSG_COMPRESS_START = 0; //压缩开始
    private static final int MSG_COMPRESS_SUCCESS = 1;//压缩成功
    private static final int MSG_COMPRESS_ERROR = 2; //压缩失败

    private Context context;
    private String mTargetDir;             //压缩后存储的路劲
    private boolean focusAlpha;            //是否忽略（argb）中a的压缩,忽略图片背景为黑色
    private int mLeastCompressSize;  //大于此数就压缩，否则不压缩
    private List<InputStreamProvider> inputStreamProviders;
    private CompressListener compressListener;
//    private OnRenameListener renameListener;
    private String rename;
    private Handler handler;

    private Luban(Builder builder){
        context = builder.context;
        mTargetDir = builder.mTargetDir;
        focusAlpha = builder.focusAlpha;
        mLeastCompressSize = builder.mLeastCompressSize;
        inputStreamProviders = builder.inputStreamProviders;
        compressListener = builder.compressListener;
        rename = builder.rename;
        handler = new Handler(Looper.getMainLooper(), this);
    }

    public static Builder builder(Context context){
        return new Builder(context);
    }

    private void compress(final Context context){
        if (inputStreamProviders == null || inputStreamProviders.size() == 0 && compressListener != null) {
            compressListener.onError(new NullPointerException("image file cannot be null"));
        }
        Iterator<InputStreamProvider> iterator = inputStreamProviders.iterator();
        while (iterator.hasNext()){
            final InputStreamProvider inputStreamProvider = iterator.next();
            AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        handler.sendMessage(handler.obtainMessage(MSG_COMPRESS_START));
                        File file = compress(context, inputStreamProvider);
                        handler.sendMessage(handler.obtainMessage(MSG_COMPRESS_SUCCESS, file));
                    } catch (Exception e) {
                        handler.sendMessage(handler.obtainMessage(MSG_COMPRESS_ERROR, e));
                        e.printStackTrace();
                    }
                }
            });
            iterator.remove();
        }
    }

    /**
     * 压缩图片（方法中被注释的是github中鲁班的逻辑代码）
     * @param context 上下文
     * @param path 被压缩图片输入流
     * @return 压缩后的文件
     * @throws Exception
     */
    private File compress(Context context, InputStreamProvider path) throws Exception {
        File result;
        File outFile = getImageCacheFile(context, BitmapCheckUtils.SINGLE.extSuffix(path));
//        if (renameListener != null) {
//            String filename = renameListener.rename(path.getPath());
//            outFile = getImageCustomFile(context, filename);
//        }
        if(rename != null){
            outFile = getImageCustomFile(context, rename);
        }
//        if (mCompressionPredicate != null) {
//            if (mCompressionPredicate.apply(path.getPath())
//                    && BitmapCheckUtils.SINGLE.needCompress(mLeastCompressSize, path.getPath())) {
//                result = new BitmapEngine(path, outFile, focusAlpha).compress();
//            } else {
//                result = new File(path.getPath());
//            }
//        } else {
            result = BitmapCheckUtils.SINGLE.needCompress(mLeastCompressSize, path.getPath()) ?
                    new BitmapEngine(path, outFile, focusAlpha).compress() :
                    new File(path.getPath());
//        }
        return result;
    }

    /**
     * 创建需要缓存的压缩后的文件
     * @param context
     * @param suffix
     * @return
     */
    private File getImageCacheFile(Context context, String suffix) {
        if (TextUtils.isEmpty(mTargetDir)) {
            mTargetDir = getImageCacheDir(context).getAbsolutePath();
        }
        String cacheBuilder = mTargetDir + "/" +
                System.currentTimeMillis() +
                (int) (Math.random() * 1000) +
                (TextUtils.isEmpty(suffix) ? ".jpg" : suffix);
        return new File(cacheBuilder);
    }

    private File getImageCacheDir(Context context) {
        return getImageCacheDir(context, DEFAULT_DISK_CACHE_DIR);
    }

    private File getImageCacheDir(Context context, String cacheName) {
        //SDCard/Android/data/你的应用包名/cache/目录（临时缓存目录）
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir != null) {
            File result = new File(cacheDir, cacheName);
            if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
                // File wasn't able to create a directory, or the result exists but not a directory
                return null;
            }
            return result;
        }
        if (Log.isLoggable(TAG, Log.ERROR)) {
            Log.e(TAG, "default disk cache dir is null");
        }
        return null;
    }

    private File getImageCustomFile(Context context, String filename) {
        if (TextUtils.isEmpty(mTargetDir)) {
            mTargetDir = getImageCacheDir(context).getAbsolutePath();
        }
        String cacheBuilder = mTargetDir + "/" + filename;
        return new File(cacheBuilder);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (compressListener == null) {
            return false;
        }
        switch (msg.what){
            case MSG_COMPRESS_START:
                compressListener.onStart();
                break;
            case MSG_COMPRESS_SUCCESS:
                compressListener.onSuccess((File) msg.obj);
                break;
            case MSG_COMPRESS_ERROR:
                compressListener.onError((Exception) msg.obj);
                break;
        }
        return false;
    }

    /**
     * 构建者内部类
     */
    public static class Builder{

        private Context context;
        private String mTargetDir;             //压缩后存储的路劲
        private boolean focusAlpha;            //是否忽略（argb）中a的压缩,忽略图片背景为黑色
        private int mLeastCompressSize = 100;  //大于100kb// 就压缩，否则不压缩
        private List<InputStreamProvider> inputStreamProviders;
        private CompressListener compressListener;
//        private OnRenameListener renameListener;
        private String rename;

        public Builder(Context context){
            this.context = context;
            inputStreamProviders = new ArrayList<>();
        }

        /**
         * 设置压缩后文件的缓存目录
         * @param mTargetDir
         * @return
         */
        public Builder setmTargetDir(String mTargetDir) {
            this.mTargetDir=mTargetDir;
            return this;
        }

        /**
         * 设置是否支持透明度压缩
         * @param focusAlpha <p> true - to keep alpha channel, the compress speed will be slow. </p>
         *                    <p> false - don't keep alpha channel, it might have a black background.</p>
         * @return
         */
        public Builder setFocusAlpha(boolean focusAlpha) {
            this.focusAlpha=focusAlpha;
            return this;
        }

        /**
         *  设置个数值，高于该数值就压缩，否则不压缩
         * @param mLeastCompressSize 默认100
         * @return
         */
        public Builder setmLeastCompressSize(int mLeastCompressSize) {
            this.mLeastCompressSize=mLeastCompressSize;
            return this;
        }

        /**
         * 设置压缩监听
         * @param compressListener
         * @return
         */
        public Builder setCompressListener(CompressListener compressListener) {
            this.compressListener=compressListener;
            return this;
        }

//        public Builder setRenameListener(OnRenameListener renameListener) {
//            this.renameListener=renameListener;
//            return this;
//        }

        /**
         * 重命名压缩文件
         * @param rename
         * @return
         */
        public Builder setRename(String rename){
            this.rename = rename;
            return this;
        }

        /**
         * 把图片文件或者图片路径或者图片的URi传进来转化为输入流
         * @param list
         * @param <T>
         * @return
         */
        public <T> Builder load(List<T> list) {
            for (T src : list) {
                if (src instanceof String) {
                    load((String) src);
                } else if (src instanceof File) {
                    load((File) src);
                } else if (src instanceof Uri) {
                    load((Uri) src);
                } else {
                    throw new IllegalArgumentException("Incoming data type exception, it must be String, File, Uri or Bitmap");
                }
            }
            return this;
        }

        /**
         * 图片的uri
         * @param uri
         */
        public Builder load(final Uri uri) {
            inputStreamProviders.add(new InputStreamProvider() {
                @Override
                public InputStream open() throws Exception {
                    return context.getContentResolver().openInputStream(uri);
                }

                @Override
                public String getPath() {
                    return uri.getPath();
                }
            });
            return this;
        }

        /**
         * 图片文件
         * @param file
         */
        public Builder load(final File file) {
            inputStreamProviders.add(new InputStreamProvider() {
                @Override
                public InputStream open() throws Exception {
                    return new FileInputStream(file);
                }

                @Override
                public String getPath() {
                    return file.getAbsolutePath();
                }
            });
            return this;
        }

        /**
         * 图片的绝对路径
         * @param path
         */
        public Builder load(final String path) {
            inputStreamProviders.add(new InputStreamProvider() {
                @Override
                public InputStream open() throws Exception {
                    return new FileInputStream(path);
                }

                @Override
                public String getPath() {
                    return path;
                }
            });
            return this;
        }

        /**
         * 去执行压缩方法
         */
        public void build(){
            new Luban(this).compress(context);
        }
    }
}
