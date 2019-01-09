package com.qdwang.lib.net.exceptions;

/**
 * @author qdwang$
 * @date 2018/8/31$
 * @describe
 */
public class ApiException extends Exception {

    int code;
    public ApiException(int code,String s) {
        super(s);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
