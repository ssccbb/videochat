package com.qiiiqjk.kkanzh.net.api;

import android.app.Activity;

import com.qiiiqjk.kkanzh.model.PhoneVertifyResult;
import com.qiiiqjk.kkanzh.net.httprequest.ApiCallback;
import com.qiiiqjk.kkanzh.net.httprequest.CommonApiRequest;
import com.qiiiqjk.kkanzh.net.httprequest.CommonRetrofitCallback;
import com.qiiiqjk.kkanzh.net.httprequest.retrofit.JkRetrofitCallBack;

import retrofit2.Call;

public class Api {
    public static final String API_BASE_URL = "https://a.kinnekpro.com";

    private ApiService apiService;

    public Api() {
        apiService = CommonApiRequest.getInstance().create(ApiService.class,API_BASE_URL);
    }

    /**
     * 获取验证码
     * @param phone 手机号
     * @param activity 当前activity
     * @param callback 回调
     * */
    public void getVerifyCode(String phone, Activity activity, ApiCallback<PhoneVertifyResult> callback){
        Call<PhoneVertifyResult> mCall = apiService.getVerifyCode(phone);
        mCall.enqueue(new JkRetrofitCallBack(callback,activity,PhoneVertifyResult.class, CommonRetrofitCallback.REQUEST_ID_THREE));
    }

}
