package com.qdwang.lib.business;

import com.qdwang.lib.net.RxHttp;
import com.qdwang.lib.net.converter.StringConverterFactory;
import com.qdwang.lib.net.observer.BaseApi;

/**
 * author: create by qdwang
 * date: 2018/9/5 09:34
 * described：
 */
public class NetMobileService extends BaseApi implements INetService {

    private static NetworkServiceApi networkServiceApi;

    private static INetService intances;
    private NetMobileService(){}
    public static synchronized INetService getIntances(){
        if(intances == null){
            synchronized (NetMobileService.class){
                if(intances == null){
                    intances = new NetMobileService();
                }
            }
        }
        return intances;
    }

    private static NetworkServiceApi getNetworkServiceApi(){
        if(networkServiceApi == null){
            networkServiceApi = new RxHttp.NetworkApiBuilder()
                    .setConvertFactory(StringConverterFactory.create())
                    .build()
                    .create(NetworkServiceApi.class);
        }
        return networkServiceApi;
    }

//    @Override
//    public Observable login(Map<String, String> map) {
//        return getObservable(getNetworkServiceApi().login(toReString(map)));
//    }
}
