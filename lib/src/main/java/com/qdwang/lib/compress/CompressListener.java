package com.qdwang.lib.compress;

import java.io.File;

/**
 * author: create by qdwang
 * date: 2018/11/1 14:57
 * described：
 */
public interface CompressListener {

    void onStart();

    void onSuccess(File file);

    void onError(Exception e);
}
