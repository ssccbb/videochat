package com.qiiiqjk.kkanzh;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.qiiiqjk.kkanzh.BuildConfig;
import com.qiiiqjk.kkanzh.common.Constants;
import com.qiiiqjk.kkanzh.net.api.Api;
import com.qiiiqjk.kkanzh.utils.SharedPreUtil;
import com.qiiiqjk.kkanzh.utils.Utils;
import com.tencent.bugly.Bugly;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.util.List;

import okhttp3.Cookie;

public class App extends Application {
    public static final String TAG = App.class.getSimpleName();
    private static Context context;
    private static App instance;
    private String url;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        Fresco.initialize(this);
        SharedPreUtil.init(this);
        UMConfigure.setLogEnabled(BuildConfig.DEBUG ? true : false);
        UMConfigure.init(context,
                BuildConfig.UMENG_APP_KEY, //appkey
                BuildConfig.CHANNEL,//channel
                BuildConfig.DEVICE_TYPE,//device
                BuildConfig.APPLICATION_ID);//push secret
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);
        com.tencent.bugly.beta.Beta.autoCheckUpgrade = false;
        Bugly.init(context,BuildConfig.BUGLY_APP_ID,BuildConfig.DEBUG ? true : false);
    }

    public static App getInstance() {
        if (instance == null) {
            return new App();
        }
        return instance;
    }

    /**
     * Returns a 'Cookie' HTTP request header with all cookies, like {@code a=b; c=d}.
     */
    private String cookieHeader(List<Cookie> cookies) {
        StringBuilder cookieHeader = new StringBuilder();
        for (int i = 0, size = cookies.size(); i < size; i++) {
            if (i > 0) {
                cookieHeader.append("; ");
            }
            Cookie cookie = cookies.get(i);
            cookieHeader.append(cookie.name()).append('=').append(cookie.value());
        }
        return cookieHeader.toString();
    }

    @NonNull
    private String getToken(String method, String url, long l) {
//        url = url.substring(0,url.length()-1);
        String replace = url.replace(Api.API_BASE_URL + "/", "");
        if (method.equals("GET")) {
            int i = replace.indexOf("?");
            if (i > 0) {
                replace = replace.substring(0, i);
            }
        }
        replace = replace.toLowerCase();

        String token = Utils.getSha1(Constants.API_KEY + replace + Long.toHexString(l));
        token = token.toLowerCase();
        return token;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
