package com.qdwang.enchttp;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.qdwang.enchttp.bean.BaseModel;
import com.qdwang.enchttp.http.IDataCallBack;
import com.qdwang.enchttp.http.JsonRequestHttp;
import com.qdwang.mylibrary.compress.CompressListener;
import com.qdwang.mylibrary.compress.Luban;
import com.qdwang.mylibrary.permission.PermissionManager;
import com.qdwang.mylibrary.permission.annotation.IPermission;

import java.io.File;

public class MainActivity extends BaseActivity {

    private String url = "http://v.juhe.cn/toutiao/index?type=top&key=dd87f5c7306328ecc5e07c9e0185130e";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view) {
//        requestTest();
//        Luban.builder(this).load(new ArrayList<>()).
        requestPermissionStorage();
    }


    @IPermission(100)
    private void requestPermissionStorage(){
        if(PermissionManager.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)){
//            openAlbm();
            String path = Environment.getExternalStorageDirectory() + "/DCIM/Camera/IMG_20181101_174415.jpg";
            Luban.builder(this).setmTargetDir(getSDPath()).load(path).setCompressListener(new CompressListener() {
                @Override
                public void onStart() {
                    Log.e("qdwang====", "onStart()");
                }

                @Override
                public void onSuccess(File file) {
                    Log.e("qdwang====", "onSuccess()  file = " + file.getPath());
                }

                @Override
                public void onError(Exception e) {
                    Log.e("qdwang====", "onError() e = " + e.getMessage());
                }
            }).build();

        }else {
            PermissionManager.requestPermissions(this, "请求sd卡读写权限", 100, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void openAlbm(){
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(pickIntent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            Uri uri=data.getData();
            if(uri == null){
                return;
            }
            Luban.builder(this).setmTargetDir(getSDPath()).load(uri).setCompressListener(new CompressListener() {
                @Override
                public void onStart() {
                    Log.e("qdwang====", "onStart()");
                }

                @Override
                public void onSuccess(File file) {
                    Log.e("qdwang====", "onSuccess()  file = " + file.getPath());
                }

                @Override
                public void onError(Exception e) {
                    Log.e("qdwang====", "onError() e = " + e.getMessage());
                }
            }).build();
        }
    }

    /**
     * 获取sd卡根目录
     * @return
     */
    private String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if(sdCardExist){
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    public void requestTest(){
        JsonRequestHttp.sendJsonRequest("", url, BaseModel.class, new IDataCallBack<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                Log.d("qdwang==", "reason = " +baseModel.reason);
                Toast.makeText(MainActivity.this, baseModel.reason, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFalure() {

            }
        });
    }
}
