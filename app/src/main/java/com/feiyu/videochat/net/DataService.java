package com.feiyu.videochat.net;

import com.feiyu.videochat.net.api.Api;
import com.feiyu.videochat.model.basemodel.HttpResultModel;
import com.feiyu.videochat.model.GameAccountResultModel;
import com.feiyu.videochat.net.body.GameAccountRequestBody;

import io.reactivex.Flowable;


public class DataService {

    public static Flowable<HttpResultModel<GameAccountResultModel>> getGameAccountList(GameAccountRequestBody friendRangeRequestBody) {
        return Api.CreateApiService().getGameAccountList(friendRangeRequestBody);
    }
}
