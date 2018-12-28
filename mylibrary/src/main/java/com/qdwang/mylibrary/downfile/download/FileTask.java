package com.qdwang.mylibrary.downfile.download;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.qdwang.mylibrary.downfile.DownloadUtils;
import com.qdwang.mylibrary.downfile.Constans;
import com.qdwang.mylibrary.downfile.db.Db;
import com.qdwang.mylibrary.downfile.net.OkHttpManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * author: create by qdwang
 * date: 2018/11/12 17:44
 * described：
 */
public class FileTask implements Runnable {

    private Context context;
    private String url;
    private String path;
    private String name;
    private Handler handler;
    private int EACH_TEMP_SIZE = 16;
    private int TEMP_FILE_TOTAL_SIZE;
    private int childTaskCount;
    private ArrayList<Call> callList;
    private boolean IS_CANCEL;
    private boolean IS_DESTORY;
    private boolean IS_PAUSE;
    private int tempChildTaskCount;

    public FileTask(Context context, DownloadData downloadData, Handler handler) {
        this.context=context;
        this.url=downloadData.url;
        this.path=downloadData.path;
        this.name=downloadData.name;
        this.childTaskCount = downloadData.childTaskCount;
        this.handler=handler;
        TEMP_FILE_TOTAL_SIZE = EACH_TEMP_SIZE * childTaskCount;
    }

    @Override
    public void run() {
        try {
            File saveFile = new File(path, name);
            File tempFile = new File(path, name+".temp");
            DownloadData data =Db.getInstance(context).getData(url);
            if(DownloadUtils.isFileExists(saveFile) && DownloadUtils.isFileExists(tempFile) && data != null && data.status != Constans.PROGRESS){
                Response response=OkHttpManager.getInstances().initRequest(url, data.lastModify);
                if(response != null && response.isSuccessful() && DownloadUtils.isNotServerFileChanged(response)){
                    //服务端文件没有变化，准备下载
                    TEMP_FILE_TOTAL_SIZE = EACH_TEMP_SIZE * data.childTaskCount;
                    onStart(data.totalLength, data.currentLength, data.lastModify, true);
                }else {
                    //需要先进行断点下载前的准备
                    prepareRangeFile(response);
                }
                saveRangeFile();
            }else {
                //不支持断点续传
                Response response = OkHttpManager.getInstances().initRequest(url);
                if(response != null && response.isSuccessful()){
                    if(DownloadUtils.isSuportRange(response)){
                        saveCommonFile(response);
                        saveRangeFile();
                    }else {
                        saveCommonFile(response);
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 普通下载
     * @param response
     */
    private void saveCommonFile(Response response) {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            long fileLength = response.body().contentLength();
            onStart(fileLength, 0, "", false);
            DownloadUtils.deleteFile(path, name);
            File file = DownloadUtils.createFile(path, name);
            if(file == null){
                return;
            }
            inputStream = response.body().byteStream();
            fileOutputStream = new FileOutputStream(file);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1){
                if(IS_CANCEL){
                    handler.sendEmptyMessage(Constans.CANCEL);
                    break;
                }
                if(IS_DESTORY){
                    handler.sendEmptyMessage(Constans.FINISH);
                }
                fileOutputStream.write(buffer,0, len);
                onProgresds(len);
            }
            fileOutputStream.flush();
        }catch (IOException e){
            onError(e);
        }finally {
            try {
                inputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            response.close();
        }
    }

    /**
     * 断点下载前的准备
     *   首先是清除历史记录，创建新的目标文件和临时文件
     *   childTaskCount代表文件需要通过几个子任务去下载，
     *   这样就可以得到每个子任务需要下载的任务大小
     *   进而得到具体的断点信息并记录到临时文件中
     *  "rws"  打开以便读取和写入。相对于 "rw"，"rws" 还要求对“文件的内容”或“元数据”的每个更新都同步写入到基础存储设备。
     *
     * "rwd"  打开以便读取和写入，相对于 "rw"，"rwd" 还要求对“文件的内容”的每个更新都同步写入到基础存储设备
     * @param response
     */
    private void prepareRangeFile(Response response) {
        RandomAccessFile saveRandomAccessFile = null;
        RandomAccessFile tempRandomAccessFile = null;
        FileChannel tempChannel = null;
        File saveFile;
        File tempFile;
        try {
            saveFile=new File(path, name);
            tempFile=new File(path, name+".temp");
            //获取下载文件的总大小
            long fileLength = response.body().contentLength();
            onStart(fileLength, 0, DownloadUtils.getLastModify(response), true);
            Db.getInstance(context).deleteData(url);
            DownloadUtils.deleteFile(saveFile, tempFile);
            saveFile=new File(path, name);
            tempFile=new File(path, name+".temp");
            saveRandomAccessFile =new RandomAccessFile(saveFile, "rws");
            saveRandomAccessFile.setLength(fileLength);

            tempRandomAccessFile =new RandomAccessFile(tempFile, "rws");
            tempRandomAccessFile.setLength(TEMP_FILE_TOTAL_SIZE);
            tempChannel = tempRandomAccessFile.getChannel();
            MappedByteBuffer buffer = tempChannel.map(FileChannel.MapMode.READ_WRITE, 0, TEMP_FILE_TOTAL_SIZE);
            long start;
            long end;
            int eachSize = (int)(fileLength / childTaskCount);
            for (int i = 0; i < childTaskCount; i++){
                if(i == childTaskCount - 1){
                    start = i * childTaskCount;
                    end = fileLength - 1;
                }else {
                    start = i * eachSize;
                    end = (i + 1) * eachSize - 1;
                }
                buffer.putLong(start);
                buffer.putLong(end);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveRangeFile(){
        final File saveFile=new File(path, name);
        final File  tempFile=new File(path, name+".temp");
        final Ranges ranges=readDownloadRange(tempFile);
        //存储请求
        callList = new ArrayList<>();
        Db.getInstance(context).updateProgress(0, 0, Constans.PROGRESS, url);
        for (int i=0; i < childTaskCount; i++) {
            final int temp = i;
            Call call = OkHttpManager.getInstances().initRequest(url, ranges.start[i], ranges.end[i], new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    onError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    startSaveRangeFile(response, temp, ranges, saveFile, tempFile);
                }
            });
            callList.add(call);
        }
    }

    /**
     * 分段保存文件
     * @param response
     * @param temp
     * @param ranges
     * @param saveFile
     * @param tempFile
     */
    private void startSaveRangeFile(Response response, int temp, Ranges ranges, File saveFile, File tempFile) {
        RandomAccessFile saveRandomAccessFile = null;
        FileChannel saveChannel = null;
        InputStream inputStream = null;
        RandomAccessFile tempRandomAccessFile = null;
        FileChannel tempChannel = null;
        try {
            saveRandomAccessFile =new RandomAccessFile(saveFile, "rws");
            saveChannel = saveRandomAccessFile.getChannel();
            MappedByteBuffer saveBuffer =saveChannel.map(FileChannel.MapMode.READ_WRITE, ranges.start[temp], ranges.end[temp]-ranges.start[temp] +1);

            tempRandomAccessFile =new RandomAccessFile(tempFile, "rws");
            tempChannel = tempRandomAccessFile.getChannel();
            MappedByteBuffer tempBuffer = tempChannel.map(FileChannel.MapMode.READ_WRITE, 0, TEMP_FILE_TOTAL_SIZE);

            inputStream = response.body().byteStream();
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1){
                if(IS_CANCEL){
                    handler.sendEmptyMessage(Constans.CANCEL);
                    callList.get(temp).cancel();
                    break;
                }
                saveBuffer.put(buffer,0, len);
                tempBuffer.putLong(temp * EACH_TEMP_SIZE, tempBuffer.getLong(temp * EACH_TEMP_SIZE)+len);
                onProgresds(len);
                if(IS_DESTORY){
                    handler.sendEmptyMessage(Constans.DESTROY);
                    callList.get(temp).cancel();
                    break;
                }
                if(IS_PAUSE){
                    handler.sendEmptyMessage(Constans.PAUSE);
                    callList.get(temp).cancel();
                    break;
                }
            }
            addCount();
        }catch (Exception e){
            onError(e);
        }finally {

        }
    }

    private void addCount() {
        ++tempChildTaskCount;
    }

    /**
     * 读取保存的断点信息
     * @param tempFile
     */
    private Ranges readDownloadRange(File tempFile) {
        RandomAccessFile record = null;
        FileChannel fileChannel = null;
        try {
            record = new RandomAccessFile(tempFile, "rws");
            fileChannel = record.getChannel();
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, TEMP_FILE_TOTAL_SIZE);
            long[] startByteArray = new long[childTaskCount];
            long[] endByteArray = new long[childTaskCount];
            for (int i=0; i <childTaskCount; i++) {
                startByteArray[i] = buffer.getLong();
                endByteArray[i] = buffer.getLong();
            }
            return new Ranges(startByteArray, endByteArray);
        }catch (Exception e){
            e.printStackTrace();
            onError(e);
        }finally {
            try {
                fileChannel.close();
                record.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void onStart(long totalFileLength, long currentLength, String lastModify, boolean isSuportRange){
        Message message = new Message();
        message.what =Constans.START;
        Bundle bundle = new Bundle();
        bundle.putLong("totalFileLength", totalFileLength);
        bundle.putLong("currentLength", currentLength);
        bundle.putString("lastModify", lastModify);
        bundle.putBoolean("isSuportRange", isSuportRange);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    public void onProgresds(int currentLength){
        Message message = Message.obtain();
        message.what = Constans.PROGRESS;
        message.arg1 = currentLength;
        handler.sendMessage(message);
    }

    private void onError(Exception e) {

    }
}
