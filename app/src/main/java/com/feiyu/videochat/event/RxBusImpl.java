package com.feiyu.videochat.event;


import android.support.annotation.NonNull;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * Created by wanglei on 2016/12/22.
 */

public class RxBusImpl implements IBus {

    private FlowableProcessor<Object> bus = null;

    private RxBusImpl() {
        bus = PublishProcessor.create().toSerialized();
    }

    public static RxBusImpl get() {
        return Holder.instance;
    }

    @Override
    public void register(Object object) {
    }

    @Override
    public void unregister(Object object) {
        //会将所有由mBus生成的Flowable都置completed状态后续的所有消息都收不到了
        bus.onComplete();
    }

    @Override
    public void post(IEvent event) {
        bus.onNext(event);
    }

    public <T extends IEvent> Flowable<T> toFlowable(Class<T> eventType) {
        return bus.ofType(eventType).onBackpressureBuffer();
    }

    @Override
    public void postSticky(IEvent event) {

    }

    public void post(@NonNull Object obj) {
        bus.onNext(obj);
    }

    public <T> Flowable<T> receive(Class<T> clz) {
        return bus.ofType(clz).onBackpressureBuffer();
    }

    public boolean hasSubscribers() {
        return bus.hasSubscribers();
    }

    private static class Holder {
        private static final RxBusImpl instance = new RxBusImpl();
    }
}
