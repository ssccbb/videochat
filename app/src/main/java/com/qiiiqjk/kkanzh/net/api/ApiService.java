package com.qiiiqjk.kkanzh.net.api;

import com.qiiiqjk.kkanzh.model.PhoneVertifyResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("/user/send_verification_code")
    Call<PhoneVertifyResult> getVerifyCode(@Field("phone") String phone);
}
