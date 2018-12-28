package com.qdwang.mylibrary.downfile.download;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.qdwang.mylibrary.downfile.Constans;

/**
 * author: create by qdwang
 * date: 2018/11/12 15:51
 * described：
 */
public class DownloadData implements Parcelable {

    public String url;
    public String path;
    public String name;
    public int currentLength;
    public int totalLength;
    /**
     * 已下载的百分比
     */
    public float percentage;
    /**
     * 状态时间
     */
    public int status =Constans.NONE;
    /**
     * 线程数
     */
    public int childTaskCount;
    public long date;
    public String lastModify;

    public DownloadData(){

    }

    public DownloadData(String url, String path, String name) {
        this.url=url;
        this.path=path;
        this.name=name;
    }

    public DownloadData(String url, String path, String name, int childTaskCount) {
        this.url=url;
        this.path=path;
        this.name=name;
        this.childTaskCount=childTaskCount;
    }

    public DownloadData(String url, String path, String name, int currentLength, int totalLength, int childTaskCount, long date) {
        this.url=url;
        this.path=path;
        this.name=name;
        this.currentLength=currentLength;
        this.totalLength=totalLength;
        this.status=Constans.START;
        this.childTaskCount=childTaskCount;
        this.date=date;
    }

    protected DownloadData(Parcel in) {
        url=in.readString();
        path=in.readString();
        name=in.readString();
        currentLength=in.readInt();
        totalLength=in.readInt();
        percentage=in.readFloat();
        status=in.readInt();
        childTaskCount=in.readInt();
        date=in.readLong();
        lastModify=in.readString();
    }

    public static final Creator<DownloadData> CREATOR=new Creator<DownloadData>() {
        @Override
        public DownloadData createFromParcel(Parcel in) {
            return new DownloadData(in);
        }

        @Override
        public DownloadData[] newArray(int size) {
            return new DownloadData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(url);
        dest.writeString(path);
        dest.writeString(name);
        dest.writeInt(currentLength);
        dest.writeInt(totalLength);
        dest.writeFloat(percentage);
        dest.writeInt(status);
        dest.writeInt(childTaskCount);
        dest.writeLong(date);
        dest.writeString(lastModify);
    }
}
