package com.qdwang.mylibrary.compress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * author: create by qdwang
 * date: 2018/11/1 11:11
 * described：位图压缩引擎
 */
public class BitmapEngine {

    private InputStreamProvider srcImg;    //原图输入流
    private File tagImg;                   //压缩后的目标文件
    private int srcWidth;                  //原图宽
    private int srcHeight;                 //原图高
    private boolean focusAlpha;            //是否忽略处理（argb）透明度，true不忽略压缩慢，false忽略背景为黑色

    public BitmapEngine(InputStreamProvider srcImg, File tagImg, boolean focusAlpha) throws Exception {
        this.srcImg=srcImg;
        this.tagImg=tagImg;
        this.focusAlpha=focusAlpha;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeStream(srcImg.open(), null, options);
        srcWidth = options.outWidth;
        srcHeight = options.outHeight;
    }

    /**
     * 鲁班压缩的核心算法
     * @return 返回一个 options.inSampleSize 所需要的压缩值
     */
    private int computeSize() {
        srcWidth = srcWidth % 2 == 1 ? srcWidth + 1 : srcWidth;
        srcHeight = srcHeight % 2 == 1 ? srcHeight + 1 : srcHeight;

        int longSide = Math.max(srcWidth, srcHeight);
        int shortSide = Math.min(srcWidth, srcHeight);

        float scale = ((float) shortSide / longSide);
        if (scale <= 1 && scale > 0.5625) {
            if (longSide < 1664) {
                return 1;
            } else if (longSide < 4990) {
                return 2;
            } else if (longSide > 4990 && longSide < 10240) {
                return 4;
            } else {
                return longSide / 1280 == 0 ? 1 : longSide / 1280;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            return longSide / 1280 == 0 ? 1 : longSide / 1280;
        } else {
            return (int) Math.ceil(longSide / (1280.0 / scale));
        }
    }

    /**
     * 压缩图片
     * @return
     */
    public File compress()throws Exception{
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = computeSize();
        Bitmap bitmap = BitmapFactory.decodeStream(srcImg.open(), null, options);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (BitmapCheckUtils.SINGLE.isJPG(srcImg.open())){
            bitmap = rotatingImage(bitmap, BitmapCheckUtils.SINGLE.getOrientation(srcImg.open()));
        }
        bitmap.compress(focusAlpha?Bitmap.CompressFormat.PNG: Bitmap.CompressFormat.JPEG, 60, outputStream);
        bitmap.recycle();
        FileOutputStream fileOutputStream = new FileOutputStream(tagImg);
        fileOutputStream.write(outputStream.toByteArray());
        fileOutputStream.flush();
        fileOutputStream.close();
        outputStream.close();
        return tagImg;
    }

    private Bitmap rotatingImage(Bitmap bitmap, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
