package com.qdwang.mylibrary.downfile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qdwang.mylibrary.downfile.download.DownloadData;

/**
 * author: create by qdwang
 * date: 2018/11/12 15:39
 * described：
 */
public class Db {

    /**
     * 数据库名称
     */
    private final static String DB_NAME = "";

    /**
     * 数据库版本
     */
    private final static int VERSION = 0;

    /**
     * 表名
     */
    private final static String TABLE_NAME_OKDOWN = "";

    private static Db db;
    private SQLiteDatabase sqLiteDatabase;

    private Db(Context context) {
        DbOpenHelper dbOpenHelper = new DbOpenHelper(context, DB_NAME, null, VERSION);
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();
    }

    public static Db getInstance(Context context){
        if(db == null){
            synchronized (Db.class){
                if(db == null){
                    db = new Db(context);
                }
            }
        }
        return db;
    }

    /**
     * 向数据库中插入下载的数据
     * @param data
     */
    public void insertData(DownloadData data){
        ContentValues contentValues = new ContentValues();
        contentValues.put("url", data.url);
        contentValues.put("path", data.path);
        contentValues.put("name", data.name);
        contentValues.put("childTaskCount", data.childTaskCount);
        contentValues.put("currentLength", data.currentLength);
        contentValues.put("date", data.date);
        contentValues.put("percentage", data.percentage);
        contentValues.put("status", data.status);
        contentValues.put("totalLength", data.totalLength);
        contentValues.put("lastModify", data.lastModify);
        sqLiteDatabase.insert(TABLE_NAME_OKDOWN, null, contentValues);
    }

    /**
     * 获取数据库中下载的数据信息
     * @param url
     * @return
     */
    public DownloadData getData(String url){
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME_OKDOWN, null, "url = ?", new String[]{url}, null, null, null);
        if(!cursor.moveToFirst()){
            return null;
        }
        DownloadData data = new DownloadData();
        data.url = cursor.getString(cursor.getColumnIndex("url"));
        data.path = cursor.getString(cursor.getColumnIndex("path"));
        data.name = cursor.getString(cursor.getColumnIndex("name"));
        data.childTaskCount = cursor.getInt(cursor.getColumnIndex("childTaskCount"));
        data.currentLength = cursor.getInt(cursor.getColumnIndex("currentLength"));
        data.date = cursor.getLong(cursor.getColumnIndex("date"));
        data.percentage = cursor.getFloat(cursor.getColumnIndex("percentage"));
        data.status = cursor.getInt(cursor.getColumnIndex("status"));
        data.totalLength = cursor.getInt(cursor.getColumnIndex("totalLength"));
        data.lastModify = cursor.getString(cursor.getColumnIndex("lastModify"));
        return data;
    }

    /**
     * 更新下载信息，进度
     * @param currentSize
     * @param percentage
     * @param status
     * @param url
     */
    public void updateProgress(int currentSize, float percentage, int status, String url){

    }

    public void deleteData(String url) {

    }
}
