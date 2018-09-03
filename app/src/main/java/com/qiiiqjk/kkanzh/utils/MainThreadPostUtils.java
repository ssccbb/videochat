package com.qiiiqjk.kkanzh.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.qiiiqjk.kkanzh.App;

public class MainThreadPostUtils {

    private static final int MESSAGE_TOAST = 1001;
    private static final int MESSAGE_TOAST_LONG = 1002;

    private static Handler handler;

    private static final byte[] handlerLock = new byte[0];

    public static Handler getHandler() {
        synchronized (handlerLock) {
            if (handler == null) {
                handler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case MESSAGE_TOAST:
                                Toast.makeText(App.getContext(), msg.obj.toString(),
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case MESSAGE_TOAST_LONG:
                                Toast.makeText(App.getContext(), msg.obj.toString(),
                                        Toast.LENGTH_LONG).show();
                                break;
                            default:
                                break;
                        }
                    }
                };
            }
        }
        return handler;
    }

    public static void post(Runnable run) {
        if (isMainThread()) {
            run.run();
        } else {
            getHandler().post(run);
        }
    }

    public static void postDelayed(Runnable run, long delayMillis) {
        getHandler().postDelayed(run, delayMillis);
    }

    public static void toast(String message) {
        if (!TextUtils.isEmpty(message)) {
            checkContext();
            Message msg = getHandler().obtainMessage(MESSAGE_TOAST, message);
            msg.sendToTarget();
        }
    }

    public static void toastLong(String message) {
        if (!TextUtils.isEmpty(message)) {
            checkContext();
            Message msg = getHandler().obtainMessage(MESSAGE_TOAST_LONG, message);
            msg.sendToTarget();
        }
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private static void checkContext() {
        if (App.getContext() == null) {
            throw new IllegalStateException(
                    "please call GlobalConfig.setAppContext() before call MainThreadPostUtils.toast......");
        }
    }
}
