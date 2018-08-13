package com.feiyu.videochat.net.api;

import cn.droidlover.xdroidmvp.net.XApi;

public class Api {

    //release
    public static final String API_BASE_URL = "https://apip.g9yx.com";  // "http://gank.io/api/";（基础接口地址）load用户头像拼接
    public static final String API_PAY_OR_IMAGE_URL = "https://bm.g9yx.com";  // 支付或者图片地址

    //debug
    public static final String API_BASE_URL_TEST = "http://60.205.204.218:8000";  // "http://gank.io/api/";（基础接口地址）load用户头像拼接
    public static final String API_PAY_OR_IMAGE_URL_TEST = "http://60.205.204.218:8080";  // 支付或者图片地址

    private static ApiService gankService;
    private static ApiService PayService;

    public static ApiService CreateApiService() {
        if (gankService == null) {
            synchronized (Api.class) {
                if (gankService == null) {
                    gankService = XApi.getInstance().getRetrofit(API_BASE_URL, true).create(ApiService.class);
                }
            }
        }
        return gankService;
    }

    public static ApiService CreateApiServiceTest() {
        if (gankService == null) {
            synchronized (Api.class) {
                if (gankService == null) {
                    gankService = XApi.getInstance().getRetrofit(API_BASE_URL_TEST, true).create(ApiService.class);
                }
            }
        }
        return gankService;
    }

    public static ApiService CreatePayOrImageApiService() {
        if (PayService == null) {
            synchronized (Api.class) {
                if (PayService == null) {
                    PayService = XApi.getInstance().getRetrofit(API_PAY_OR_IMAGE_URL, true).create(ApiService.class);
                }
            }
        }
        return PayService;
    }

    public static ApiService CreateHanXinApiService() {
        if (PayService == null) {
            synchronized (Api.class) {
                if (PayService == null) {
                    PayService = XApi.getInstance().getRetrofit("http://kefu.easemob.com/v1/Tenants/", true).create(ApiService.class);
                }
            }
        }
        return PayService;
    }
}
