package com.feiyu.videochat.net.api;

import android.app.Activity;

import com.feiyu.videochat.model.PhoneVertifyResult;
import com.feiyu.videochat.net.httprequest.ApiCallback;
import com.feiyu.videochat.net.httprequest.CommonApiRequest;
import com.feiyu.videochat.net.httprequest.CommonRetrofitCallback;
import com.feiyu.videochat.net.httprequest.retrofit.JkRetrofitCallBack;

import retrofit2.Call;

public class Api {
    public static final String API_BASE_URL = "http://ta.kinnekpro.com";

    private ApiService apiService;

    public Api() {
        apiService = CommonApiRequest.getInstance().create(ApiService.class,API_BASE_URL);
    }

    public void getVerifyCode(String phone, Activity activity, ApiCallback<PhoneVertifyResult> callback){
        Call<PhoneVertifyResult> mCall = apiService.getVerifyCode(phone);
        mCall.enqueue(new JkRetrofitCallBack(callback,activity,PhoneVertifyResult.class, CommonRetrofitCallback.REQUEST_ID_THREE));
    }
}
