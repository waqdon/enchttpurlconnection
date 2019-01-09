package com.qdwang.lib.net.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * author: create by qdwang
 * date: 2018/9/4 14:35
 * described：默认加载网络请求dialog
 */
public class WaitingDialog extends Dialog {

    private ProgressDialog dialog;

    public WaitingDialog(@NonNull Context context) {
        super(context);
        dialog = new ProgressDialog(context);
        dialog.setMessage("正在加载中");
    }

    @Override
    public void show() {
        super.show();
        if (dialog == null){
            return;
        }
        dialog.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (dialog == null){
            return;
        }
        dialog.dismiss();
    }
}
