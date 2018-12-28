package com.qdwang.mylibrary.downfile;

import java.io.File;
import java.io.IOException;

import okhttp3.Response;

/**
 * author: create by qdwang
 * date: 2018/11/12 18:01
 * described：
 */
public class DownloadUtils {

    /**
     * 百分比
     * @param currentLength
     * @param totalFileLength
     * @return
     */
    public static float getPerentage(int currentLength, int totalFileLength) {
        if(currentLength > totalFileLength){
            return 0;
        }
        return ((int)(currentLength*1000.0/totalFileLength))*1.0f / 100;
    }

    public static boolean isFileExists(File file) {
        if(file.exists()){
            return true;
        }
        return false;
    }

    /**
     * 服务器文件是否已经更改
     * @param response
     * @return
     */
    public static boolean isNotServerFileChanged(Response response){
        return response.code() == 206;
    }

    /**
     * 文件最后修改时间
     * @param response
     * @return
     */
    public static String getLastModify(Response response){
        return response.headers().get("Last-Modified");
    }

    public static void deleteFile(File saveFile, File tempFile) {
        if(saveFile!=null && saveFile.exists()){
            saveFile.delete();
        }
        if(tempFile != null && tempFile.exists()){
            tempFile.delete();
        }
    }

    public static boolean deleteFile(String path, String name) {
        File file = new File(path, name);
        if(file.exists() && file.isFile()){
            if(file.delete()){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    public static File createFile(String path, String name) {
        File file = null;
        try {
            file = new File(path, name);
            if(!file.exists()){
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static boolean isSuportRange(Response response) {
        if(response.headers().get("Accept-Ranges").equals("bytes")){
            return true;
        }
        return false;
    }
}
