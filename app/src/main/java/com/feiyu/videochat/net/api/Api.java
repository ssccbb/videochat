package com.feiyu.videochat.net.api;

import cn.droidlover.xdroidmvp.net.XApi;

public class Api {

    //release
    public static final String API_BASE_URL = "http://a.kinnekpro.com";  // "http://gank.io/api/";（基础接口地址）load用户头像拼接
    public static final String API_PAY_OR_IMAGE_URL = "http://a.kinnekpro.com/";  // 支付或者图片地址

    //debug
    public static final String API_BASE_URL_TEST = "http://ta.kinnekpro.com/";  // "http://gank.io/api/";（基础接口地址）load用户头像拼接
    public static final String API_PAY_OR_IMAGE_URL_TEST = "http://ta.kinnekpro.com/";  // 支付或者图片地址

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

}
