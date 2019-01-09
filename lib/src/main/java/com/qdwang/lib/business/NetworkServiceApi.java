package com.qdwang.lib.business;

import io.reactivex.Observer;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 使用Retrofit2 进行网络请求，这里是接口配置interface
 * author: create by qdwang
 * date: 2018/9/4 14:05
 * described：
 */
public interface NetworkServiceApi {

    /**
     * 可以使用这个方法 进行统一发送网络请求， 方便把接口地址进行统一管理。
     *
     * @param url        接口地址
     * @param jsonParams 加密后的参数
     * @return
     */
    @FormUrlEncoded
    @POST("{url}")
    Observer<String> httpRequest(@Path(value="url", encoded=true) String url, @Field("jsonParams") String jsonParams);

//    @FormUrlEncoded
//    @POST("gusuier/api/user/login")
//    Observable<ResponseInfo<UserResponse>> login(@Field("jsonParams") String jsonParams);
}
