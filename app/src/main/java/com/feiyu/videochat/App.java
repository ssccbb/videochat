package com.feiyu.videochat;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.feiyu.videochat.common.Constants;
import com.feiyu.videochat.net.TripleDES;
import com.feiyu.videochat.net.api.Api;
import com.feiyu.videochat.utils.SharedPreUtil;
import com.feiyu.videochat.utils.Utils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.log.XLog;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.NetProvider;
import cn.droidlover.xdroidmvp.net.RequestHandler;
import cn.droidlover.xdroidmvp.net.XApi;
import io.reactivex.annotations.Beta;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class App extends Application {
    public static final String TAG = App.class.getSimpleName();
    private static Context context;
    private static App instance;
    private String url;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        initWX();
        Fresco.initialize(this);
        SharedPreUtil.init(this);
        UMConfigure.setLogEnabled(BuildConfig.DEBUG ? true : false);
        UMConfigure.init(context,
                BuildConfig.UMENG_APP_KEY, //appkey
                BuildConfig.CHANNEL,//channel
                BuildConfig.DEVICE_TYPE,//device
                BuildConfig.APPLICATION_ID);//push secret
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);
//        CrashReport.initCrashReport(context,BuildConfig.BUGLY_APP_ID,BuildConfig.DEBUG ? true : false);
        com.tencent.bugly.beta.Beta.autoCheckUpgrade = true;
        Bugly.init(context,BuildConfig.BUGLY_APP_ID,BuildConfig.DEBUG ? true : false);
//        Log.e("app", "onCreate: "+CrashReport.getBuglyVersion(this) );
    }

    public static App getInstance() {
        if (instance == null) {
            return new App();
        }
        return instance;
    }

    /**
     * 初始化微信支付SDK
     */
    private void initWX() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
//        api = WXAPIFactory.createWXAPI(this, RxConstant.ThirdPartKey.WeixinId);
//        api.registerApp(RxConstant.ThirdPartKey.WeixinId);

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
