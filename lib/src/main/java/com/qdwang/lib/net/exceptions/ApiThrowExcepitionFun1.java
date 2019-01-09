package com.qdwang.lib.net.exceptions;


import com.qdwang.lib.net.ResponseInfo;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * @author qdwang$
 * @date 2018/8/31$
 * @describe 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
 * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
 */
public class ApiThrowExcepitionFun1<T> implements Function<ResponseInfo<T>, Observable<T>> {

    @Override
    public Observable<T> apply(ResponseInfo<T> responseInfo) {
        if (responseInfo.rspCode != 200) {  //如果code返回的不是200,则抛出ApiException异常，否则返回data数据
            return Observable.error(new ApiException(responseInfo.rspCode, responseInfo.rspMsg));
        }
        return Observable.just(responseInfo.data);
    }
}
