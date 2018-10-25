package com.qdwang.enchttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.qdwang.enchttp.bean.BaseModel;
import com.qdwang.enchttp.http.IDataCallBack;
import com.qdwang.enchttp.http.JsonRequestHttp;

public class MainActivity extends AppCompatActivity {

    private String url = "http://v.juhe.cn/toutiao/index?type=top&key=dd87f5c7306328ecc5e07c9e0185130e";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view) {
        requestTest();
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
