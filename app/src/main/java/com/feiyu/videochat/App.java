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

        XApi.registerProvider(new NetProvider() {

            @Override
            public Interceptor[] configInterceptors() {
                Interceptor[] ins = new Interceptor[1];
                Interceptor in = new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        long l = System.currentTimeMillis() / 1000;
                        l = 5 * 60 + l + Utils.getLongValue(App.getContext(), Constants.EXTIME);
                        String token = getToken(chain.request().method(),
                                chain.request().url().url().toString(),l);
                        Request request = chain.request()
                                .newBuilder()
//                                .addHeader("Cookie", SharedPreUtil.getSessionId())
//                                .addHeader("imei", SystemUtil.getIMEI(context))
//                                .addHeader("os", SystemUtil.getAppVersionName(context))
//                                .addHeader("plam", System.getProperty("os.name"))
                                .addHeader("Extime",Long.toString(l))
                                .addHeader("apiVersion","1")
                                .addHeader("Apitoken",token)
                                .build();
                        Log.e(TAG, "intercept: header == "+request.headers().toString() );
                        return chain.proceed(request);
                    }

                };
                ins[0] = in;
                return ins;
            }

            @Override
            public void configHttps(OkHttpClient.Builder builder) {
            }

            @Override
            public CookieJar configCookie() {
                return new CookieJar() {
//                    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        final StringBuilder sb = new StringBuilder();
                        for (Cookie cookie : cookies) {
                            sb.append(cookie.toString());
                            sb.append(";");
                            if (cookie.name().equals("sessionid")) {
                                SharedPreUtil.saveSessionId(sb.toString());
                            }
                        }
//                        cookieStore.put(url.host(), cookies);
//                        SharedPreUtil.saveObject(url.host(), cookies);

                        String cookes = cookieHeader(cookies);
                        CookieSyncManager.createInstance(getApplicationContext());
                        CookieManager cm = CookieManager.getInstance();
                        cm.setAcceptCookie(true);
//                        cm.removeSessionCookie();//移除    
                        cm.setCookie(Api.API_BASE_URL, cookes);
                        CookieSyncManager.getInstance().sync();

                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = SharedPreUtil.getObject(url.host());
//                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                };
            }

            @Override
            public RequestHandler configHandler() {
                return null;
            }

            @Override
            public long configConnectTimeoutMills() {
                return 20000;
            }

            @Override
            public long configReadTimeoutMills() {
                return 20000;
            }

            @Override
            public boolean configLogEnable() {
                return true;
            }

            @Override
            public boolean handleError(NetError error) {
                if (error.getType() == NetError.AuthError) {
                    //清除登陆重新跳转
                    /*SharedPreUtil.clearSessionId();
                    SharedPreUtil.cleanLoginUserInfo();
                    ILFactory.getLoader().clearMemoryCache(context);
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            ILFactory.getLoader().clearDiskCache(context);
                        }
                    });
                    DetailFragmentsActivity.launch(GameMarketApplication.this, null, Intent.FLAG_ACTIVITY_NEW_TASK, LoginFragment.newInstance());*/
                    return true;
                }
                return false;
            }
        });
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
