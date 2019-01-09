package com.qdwang.lib.net;

import android.content.Context;
import android.text.TextUtils;

import com.qdwang.lib.net.dialog.WaitingDialog;
import com.qdwang.lib.net.interceptor.DynamicParameterInterceptor;
import com.qdwang.lib.net.interceptor.LogInterceptor;
import com.qdwang.lib.net.observer.ApiObserver;
import com.qdwang.lib.net.observer.ApiWebObserver;
import com.qdwang.lib.net.parser.GsonParser;
import com.qdwang.lib.utils.SharedPreferencesUtils;
import com.qdwang.lib.utils.configs.DebugConfigs;
import com.qdwang.lib.utils.configs.HostConfigs;
import io.reactivex.Observable;
import io.reactivex.Observer;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * author: create by qdwang
 * date: 2018/9/4 13:42
 * described：
 */
public class RxHttp {
    public static final String TAG = "RxHttp";
    private static RxHttp instance = new RxHttp();
    private Observable observable;
    private static WeakReference<Context> wrContext;
    private boolean isShowWaitingDialog;
    private WaitingDialog waitingDialog;

    /**
     * 设置Context，使用弱引用
     * @param context
     * @return
     */
    public static RxHttp with(Context context){
        wrContext = new WeakReference<>(context);
        return instance;
    }

    /**
     * 设置是否显示加载动画
     * @param showWaitingDialog
     * @return
     */
    public RxHttp setShowWaitingDialog(boolean showWaitingDialog){
        isShowWaitingDialog = showWaitingDialog;
        return instance;
    }

    /**
     * 设置observable
     * @param observable
     * @return
     */
    public RxHttp setObservable(Observable observable){
        this.observable = observable;
        return instance;
    }

    public RxHttp setShowWaitingDialog(WaitingDialog waitingDialog){
        this.waitingDialog = waitingDialog;
        return instance;
    }

    /**设置ApiSubscriber
     * @param observer
     * @return
     */
    public RxHttp subscriber(Observer observer){
        if(observer instanceof ApiObserver){
            ((ApiObserver)observer).setCtx(wrContext.get());  //给subscriber设置Context，用于显示网络加载动画
            ((ApiObserver)observer).setShowWaitDialogIsVisible(isShowWaitingDialog); //控制是否显示动画
            ((ApiObserver)observer).setShowWaitingDialog(waitingDialog);
        }else if(observer instanceof ApiWebObserver){
            ((ApiWebObserver)observer).setCtx(wrContext.get());  //给subscriber设置Context，用于显示网络加载动画
            ((ApiWebObserver)observer).setShowWaitDialogIsVisible(isShowWaitingDialog); //控制是否显示动画
            ((ApiWebObserver)observer).setShowWaitingDialog(waitingDialog);
        }
        //RxJava 方法
        observable.subscribe(observer);
        return instance;
    }

    /**
     * 使用Retrofit.Builder和OkHttpClient.Builder构建NetworkApi
     */
    public static class NetworkApiBuilder{
        private String baseUrl;  //根地址
        private HashMap<String,String> addDynamicParameterMap; //url动态参数
        private Retrofit.Builder rtBuilder;
        private OkHttpClient.Builder okBuild;
        private Converter.Factory convertFactory;

        public NetworkApiBuilder setConvertFactory(Converter.Factory convertFactory) {
            this.convertFactory = convertFactory;
            return this;
        }

        public NetworkApiBuilder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public NetworkApiBuilder addDynamicParameter(HashMap map) {
            addDynamicParameterMap = map;
            return this;
        }

        public Retrofit build(){
            rtBuilder= new Retrofit.Builder();
            okBuild = new OkHttpClient.Builder();
            okBuild.connectTimeout(10L, TimeUnit.SECONDS)
                    .writeTimeout(10L, TimeUnit.SECONDS)
                    .readTimeout(30L, TimeUnit.SECONDS)
                    .sslSocketFactory(SSLSocketClient.getSSLSocketFactory()).hostnameVerifier(SSLSocketClient.getHostnameVerifier());//为OkHttp对象设置SocketFactory用于双向认证
            if(!TextUtils.isEmpty(baseUrl)){
                rtBuilder.baseUrl(baseUrl);
            }else{
                rtBuilder.baseUrl(SharedPreferencesUtils.getInstance(wrContext.get()).shareReadString("host",HostConfigs.baseDevUrl));
//                rtBuilder.baseUrl(HostConfigs.baseUrl);
            }
            if(addDynamicParameterMap!=null){
                okBuild.addInterceptor(new DynamicParameterInterceptor(addDynamicParameterMap));
            }
            /**
             * 注意：必须是在最后一个拦截器中对网络进行日志记录;
             */
            if(DebugConfigs.isDebug){ //改成自己的显示log判断逻辑
                okBuild.addInterceptor(new LogInterceptor());
            }
            if(convertFactory!=null){
                rtBuilder.addConverterFactory(convertFactory);
            }else{
                rtBuilder.addConverterFactory(GsonConverterFactory.create(GsonParser.getInstance().getGson()));
            }
            rtBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okBuild.build());
            return rtBuilder.build();
        }
    }
}
