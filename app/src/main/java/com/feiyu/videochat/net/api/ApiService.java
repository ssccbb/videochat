package com.feiyu.videochat.net.api;

import com.feiyu.videochat.model.GameAccountResultModel;
import com.feiyu.videochat.model.basemodel.HttpResultModel;
import com.feiyu.videochat.net.body.GameAccountRequestBody;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    //获取游戏账号列表
    @POST("/member/get_game_account_list/")
    Flowable<HttpResultModel<GameAccountResultModel>> getGameAccountList(@Body GameAccountRequestBody gameAccountRequestBody/*@Path("page") int page, @Path("plat_id") int plat_id, @Path("option_game_id") int option_game_id, @Path("option_channel_id") int option_channel_id*/);

}
