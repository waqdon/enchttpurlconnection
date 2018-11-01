package com.qdwang.mylibrary.permission.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * author: create by qdwang
 * date: 2018/10/26 09:45
 * described：跳转去设置界面的对话框
 */
public class DialogAppSettings implements DialogInterface.OnClickListener{

    private Activity activity;
    private String tilte;
    private String rationale;
    private String positiveButton;
    private String negativeButton;
    private DialogInterface.OnClickListener listener;
    private int requestCode = -1;

    /**
     * 跳转设置监听回调的标识码
     */
    public static final int SETTING_CODE = 1000;

    public DialogAppSettings(Activity activity, String tilte, String rationale, String positiveButton, String negativeButton, DialogInterface.OnClickListener listener, int requestCode) {
        this.activity = activity;
        this.tilte = tilte;
        this.rationale = rationale;
        this.positiveButton = positiveButton;
        this.negativeButton = negativeButton;
        this.listener = listener;
        this.requestCode = requestCode;
    }

    public void show(){
        if(listener != null){
            showDialog();
        }else {
            throw new IllegalArgumentException("对话框参数不能为null");
        }
    }

    private void showDialog() {
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle(tilte)
                .setMessage(rationale)
                .setPositiveButton(positiveButton, this)
                .setNegativeButton(negativeButton, listener)
                .create()
                .show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, requestCode);
    }

    public static class Builder{
        private Activity activity;
        private String tilte;
        private String rationale;
        private String positiveButton;
        private String negativeButton;
        private DialogInterface.OnClickListener listener;
        private int requestCode = -1;

        public Builder(Activity activity){
            this.activity = activity;
        }

        public Builder setListener(DialogInterface.OnClickListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        public Builder setRationale(String rationale) {
            this.rationale=rationale;
            return this;
        }

        public DialogAppSettings build(){
            tilte = "需要的权限";
            rationale="打开设置，去申请"+ this.rationale +"权限";
            positiveButton = activity.getString(android.R.string.ok);
            negativeButton = activity.getString(android.R.string.cancel);
            requestCode = requestCode > 0 ? requestCode : SETTING_CODE;
            return new DialogAppSettings(
                    activity,
                    tilte,
                    rationale,
                    positiveButton,
                    negativeButton,
                    listener,
                    requestCode);
        }
    }
}
