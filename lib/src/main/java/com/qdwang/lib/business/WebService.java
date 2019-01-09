package com.qdwang.lib.business;

import com.qdwang.lib.net.RxHttp;
import com.qdwang.lib.net.converter.WebConverterFactory;
import com.qdwang.lib.net.observer.BaseApi;
import io.reactivex.Observable;

import java.util.Map;

/**
 * author: create by qdwang
 * date: 2018/9/5 09:34
 * described：提供h5直接调用接口
 */
public class WebService extends BaseApi implements IWebService {

    private static WebNetServiceApi webService;

    private static IWebService intances;
    private WebService(){}
    public static synchronized IWebService getIntances(){
        if(intances == null){
            synchronized (WebService.class){
                if(intances == null){
                    intances = new WebService();
                }
            }
        }
        return intances;
    }

    private static WebNetServiceApi getWebService(){
        if(webService == null){
            webService = new RxHttp.NetworkApiBuilder()
                    .setConvertFactory(WebConverterFactory.create())
                    .build()
                    .create(WebNetServiceApi.class);
        }
        return webService;
    }

    @Override
    public Observable h5Request(Map<String, String> map) {
        return getObservable(getWebService().h5Request(toReBody(map)));
    }
}
