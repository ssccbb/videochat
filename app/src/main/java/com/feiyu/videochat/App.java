package com.feiyu.videochat;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.feiyu.videochat.net.api.Api;
import com.feiyu.videochat.utils.SharedPreUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.NetProvider;
import cn.droidlover.xdroidmvp.net.RequestHandler;
import cn.droidlover.xdroidmvp.net.XApi;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class App extends Application{
    private static Context context;
    private static App instance;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        initWX();
        SharedPreUtil.init(this);

        XApi.registerProvider(new NetProvider() {

            @Override
            public Interceptor[] configInterceptors() {
                Interceptor[] ins = new Interceptor[1];
                Interceptor in = new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Cookie", SharedPreUtil.getSessionId())
//                                .addHeader("imei", SystemUtil.getIMEI(context))
//                                .addHeader("os", SystemUtil.getAppVersionName(context))
                                .addHeader("plam", System.getProperty("os.name"))
                                .build();
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

    public static Context getContext() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
