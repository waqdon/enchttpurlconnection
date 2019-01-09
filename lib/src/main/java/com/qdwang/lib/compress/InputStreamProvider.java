package com.qdwang.lib.compress;

import java.io.InputStream;

/**
 * author: create by qdwang
 * date: 2018/11/1 11:13
 * described：接口兼容文件，uri
 */
public interface InputStreamProvider {

    InputStream open() throws Exception;

    String getPath();
}
