package com.qdwang.lib.net.observer;

import com.qdwang.lib.net.exceptions.ApiThrowExcepitionFun1;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * author: create by qdwang
 * date: 2018/9/20 17:05
 * described：
 */
public class ObserableBuilder {

    private Observable observable;
    private boolean apiException;
    private Scheduler subscribeScheduler;
    private Scheduler obscerveScheduler;

    public void setObscerveScheduler(Scheduler obscerveScheduler) {
        this.obscerveScheduler = obscerveScheduler;
    }

    public void setSubscribeScheduler(Scheduler subscribeScheduler) {
        this.subscribeScheduler = subscribeScheduler;
    }

    public ObserableBuilder(Observable o) {
        this.observable = o;
    }

    public ObserableBuilder addApiException() {
        apiException = true;
        return this;
    }

    public Observable build() {
        if (apiException) {
            observable = observable.flatMap(new ApiThrowExcepitionFun1());
        }
        if (subscribeScheduler != null) {
            observable = observable.subscribeOn(subscribeScheduler);
        } else {
            observable = observable.subscribeOn(Schedulers.io());
        }
        if (obscerveScheduler != null) {
            observable = observable.observeOn(obscerveScheduler);
        } else {
            observable = observable.observeOn(AndroidSchedulers.mainThread());
        }
        return observable;
    }
}
