package com.feiyu.videochat.net.api;

/**
 * author: zhoulei date: 15/11/24.
 */
public interface UserApi {
//  /**
//   * 查询用户账户信息
//   *
//   * @param unionId 用户unionId
//   * @return Observable<AccountInfo>
//   */
//  @GET("/v1/pikachu/totoro/accounts/{unionId}/get")
//  Observable<AccountInfo> getAccountInfo(@Path("unionId") String unionId);
//
//  /**
//   * 用户提现接口
//   *
//   * @param unionId 用户unionId
//   * @param wdAmount 提现金额
//   * @return Observable<WithdrawResult>
//   */
//  @PATCH("/v1/pikachu/totoro/accounts/{unionId}/withdraw/{wdAmount}")
//  Observable<WithdrawResult> withdraw(@Path("unionId") String unionId,
//                                      @Path("wdAmount") long wdAmount, @Nullable @Query("wdRedPacketId") Long wdRedPacketId);
//
//  /**
//   * 用户首次提现，发送短信验证码接口
//   *
//   * @param sendMsgRequestBody 参数@see #SendMsgRequestBody
//   * @return Observable<SendMsgVerifyCodeResult>
//   */
//  @POST("/v1/pikachu/totoro/customers/captcha")
//  Observable<SendMsgVerifyCodeResult> sendMsgVerifyCode(
//          @Body() SendMsgRequestBody sendMsgRequestBody);
//
//  /**
//   * 验证短信验证码接口
//   *
//   * @param unionId 用户unionId
//   * @param msgVerifyRequestModel 手机号和验证码
//   * @return Observable<VerifyMsgCodeResult>
//   */
//  @POST("/v1/pikachu/totoro/accounts/{unionId}/add")
//  Observable<VerifyMsgCodeResult> verifyMsgCode(@Path("unionId") String unionId,
//                                                @Body MsgVerifyRequestModel msgVerifyRequestModel);
//
//  /**
//   * 获取用户提现纪录
//   *
//   * @param unionId 用户unionId
//   * @return Observable<List<WithdrawRecord>>
//   */
//  @GET("/v2/pikachu/totoro/accounts/{unionId}/withdraw")
//  Observable<WithdrawRecordWrapper> getWithdrawRecords(@Path("unionId") String unionId);
//
//  /**
//   * 带参数二维码和微信订阅量
//   *
//   * @param unionId 用户unionId
//   * @return Observable<InvitePageQrcode>
//   */
//  @GET("/v1/pikachu/totoro/customers/{unionId}/qrcode")
//  Observable<InvitePageQrcode> getInvitePageQrcode(@Path("unionId") String unionId);
//
//  /**
//   * 收益汇总明细
//   *
//   * @param unionId 用户unionId
//   * @return Observable<ProfitOverview>
//   */
//  @GET("/v1/pikachu/totoro/accounts/{unionId}/profit")
//  Observable<ProfitOverview> getProfitOverview(@Path("unionId") String unionId);
//
//  /**
//   * 用户是否关注公众号
//   *
//   * @param unionId 用户unionId
//   * @return Observable<FollowResult>
//   */
//  @GET("/v1/pikachu/totoro/customers/{unionId}")
//  Observable<FollowResult> getUserFollowResult(@Path("unionId") String unionId);
//
//  /**
//   * 拉取全局配置
//   *
//   * @return Observable<BaseResponse<ConfigModel>>
//   */
//  @POST("/v3/pikachu/totoro/common/settings/options")
//  Observable<BaseResponse<ConfigModel>> getConfig();
//
//  /**
//   * 推啊配置参数获取
//   *
//   * @return
//   */
//  @POST("/v3/pikachu/totoro/area/special/tuia/myConf")
//  Observable<BaseResponse<TuiaModel>> getTuiaData();
//
//  /**
//   * 获取投吧进入入口链接
//   *
//   * @return
//   */
//  @POST("/v3/pikachu/totoro/area/special/touba/entrance")
//  Observable<BaseResponse> getTouBaData();
//
//
//  /**
//   * 获取渠道包
//   *
//   * @return List<AppPackage>
//   */
//  @GET("/v2/pikachu/totoro/customers/{unionId}/channel/packages")
//  Observable<ChannelPackages> getAppPackages(@Path("unionId") String unionId);
//
//  /**
//   * 获取应用墙
//   *
//   * @return List<AppWall>
//   */
//  @GET("/v2/pikachu/totoro/advertising/walls")
//  Observable<AdvertisingWalls> getAppWall();
//
//  /**
//   * 获取插件列表
//   *
//   * @return List<AppPlugin>
//   */
//  @GET("/v2/pikachu/totoro/plugins")
//  Observable<AppPlugins> getAppPlugins();
//
//  /**
//   * 接入渠道列表
//   *
//   * @return
//   */
//  @POST("/v3/pikachu/totoro/area/pack/list")
//  Observable<BaseResponse<List<AppChannels>>> getAppChannels();
//
//  /**
//   * 接入渠道详情列表
//   *
//   * @param channel
//   * @return
//   */
//  @POST("/v3/pikachu/totoro/area/pack/app/list/{channel}")
//  Observable<BaseResponse<List<AppChannelPackages>>> getAppChannelPackages(
//          @Path("channel") String channel,
//          @Body PlatinumRequestBody appChannelPackagesRequestBody);
//
//  /**
//   * 获取APP首页特工任务列表
//   *
//   * @return List<AppAgents>
//   */
//  @GET("/v2/pikachu/totoro/advertising/cards")
//  Observable<AppAgents> getAppAgents();
//
//  /**
//   * 获取特工有效任务汇总
//   *
//   * @return AgentTaskCollect
//   */
//  @GET("/v1/pikachu/totoro/task/{unionId}/total")
//  Observable<AgentTaskCollect> getAgentTaskCollect(@Path("unionId") String unionId);
//
//  /**
//   * 新任务通知消息已读
//   *
//   * @param unionId
//   * @param type //0:失败审核已读，1:成功审核已读
//   * @return
//   */
//  @GET("/v1/pikachu/totoro/task/{unionId}/read/{type}")
//  Observable<BaseModel> getAgentTaskCollectReadStatus(@Path("unionId") String unionId,
//                                                      @Path("type") int type);
//
//  /**
//   * 显示或者隐藏任务(忽略)
//   *
//   */
//  @POST("/v3/pikachu/totoro/agent/task/ignore")
//  Observable<BaseResponse> getAgentValidTaskIgnore(@Query("id") long id,
//                                                   @Query("status") boolean status);
//
//  /**
//   * 获取特工有效任务列表
//   *
//   * @return List<>
//   */
//  @POST("/v3/pikachu/totoro/agent/task/available")
//  Observable<BaseResponse<AgentValidTaskInfo>> getAgentValidTask(
//          @Body AgentRequestBody agentListRequestBody);
//
//  /**
//   * 保存用户位置信息
//   *
//   * @return
//   */
//  @POST("/v3/pikachu/totoro/common/settings/location")
//  Observable<BaseResponse> saveLocation(@Query("latitude") String latitude,
//                                        @Query("longitude") String longitude);
//
//  /**
//   * 特工排序列表
//   *
//   * @return AgentSortType
//   */
//  @POST("v3/pikachu/totoro/agent/task/tags")
//  Observable<AgentSortType> getAgentSortType();
//
//  /**
//   * 获取特工任务类型列表
//   *
//   * @return List<AgentValidTaskBean>
//   */
//  @POST("/v1/pikachu/totoro/task/{unionId}/my/{listType}")
//  Observable<AgentValidTask> getAgentListTask(@Path("unionId") String unionId,
//                                              @Path("listType") long agentType, @Body BaseRequestBody agentListRequestBody);
//
//  /**
//   * 获取渠道包详情页信息
//   *
//   * @param packageName Apk名称
//   * @return ChannelApkDetailInfo
//   */
//  @GET("/v2/pikachu/totoro/channel/package/{packageName}/details")
//  Observable<ChannelApkDetailInfo> getApkDetailInfo(@Path("packageName") String packageName);
//
//  /**
//   * 获取海报数据
//   *
//   * @return List<PosterData>
//   */
//  @GET("/v2/pikachu/totoro/posters")
//  Observable<PosterData> getPosterData();
//
//  /**
//   * 获取留存应用任务列表
//   *
//   * @return LaunchTasks
//   */
//  @POST("/v3/pikachu/totoro/area/checkin/list")
//  Observable<BaseResponse<LaunchTasks>> getLaunchTasks();
//
//  /**
//   * 获取可用提现红包
//   *
//   * @param unionId 用户unionId
//   * @param wdAmount 提现金额
//   * @return 提现红包
//   */
//  @GET("/v1/pikachu/totoro/accounts/{unionId}/withdraw/redpackets/{wdAmount}")
//  Observable<WithdrawRedPackets> getWithdrawRedPackets(@Path("unionId") String unionId,
//                                                       @Path("wdAmount") long wdAmount);
//
//  /**
//   * 特工任务详情
//   *
//   * @return TaskDetail
//   */
//  @POST("/v3/pikachu/totoro/agent/task/detail")
//  Observable<BaseResponse<TaskDetail>> getAgentTaskDetail(
//          @Body AgentDetailRequestBody agentDetailRequestBody);
//
//  /**
//   * 特工任务分步列表
//   *
//   * @return BaseResponse
//   */
//  @POST("/v3/pikachu/totoro/agent/task/steps")
//  Observable<BaseResponse<List<TaskStepDetail>>> getAgentTaskStepList(@Query("id") long taskNo,
//                                                                      @Query("certId") long certId);
//
//  /**
//   * 获取七牛token
//   *
//   * @return
//   */
//  @GET("/v1/pikachu/totoro/common/qixiu/token")
//  Observable<QiniuToken> getQiniuToken();
//
//  /**
//   * 获取FAQ列表
//   *
//   * @return
//   */
//  @GET("v1/pikachu/totoro/common/helps")
//  Observable<FAQDetailItemModel> getFAQList();
//
//  /**
//   * 获取红包列表
//   *
//   * @param unionId
//   * @return
//   */
//  @GET("v1/pikachu/totoro/accounts/{unionId}/redpacket")
//  Observable<RedEnvelopeInfo> getRedEnvelopeInfo(@Path("unionId") String unionId);
//
//  /**
//   * 获取晒收入列表
//   *
//   * @return
//   */
//  @POST("v1/pikachu/totoro/common/share/list")
//  Observable<AppIncome> getAppIncome(@Body BaseRequestBody baseRequestBody);
//
//  /**
//   * 晒收入立即参加
//   *
//   * @param unionId
//   * @return
//   */
//  @GET("v1/pikachu/totoro/task/{unionId}/share/detail")
//  Observable<TaskDetail> getAppIncomeJoin(@Path("unionId") String unionId);
//
//  /**
//   * 晒收入立即参加Banner图片
//   *
//   * @return
//   */
//  @GET("v1/pikachu/totoro/common/share/banner")
//  Observable<AppIncomeBanner> getAppIncomeBanner();
//
//  /**
//   * 晒收入立即参加Banner详情
//   *
//   * @return
//   */
//  @GET("v1/pikachu/totoro/common/share/detail")
//  Observable<AppIncomeBannerDetail> getAppIncomeBannerDetail();
//
//  /**
//   * 获取当前用户新手任务进度
//   */
//  @GET("v1/pikachu/totoro/accounts/{unionId}/newer-task-status")
//  Observable<NewPersonTask> getNewPersonTask(@Path("unionId") String unionId);
//
//  /**
//   * 推荐任务
//   */
//  @GET("v1/pikachu/totoro/task/{unionId}/recommend")
//  Observable<RecommendTask> getRecommendTask(@Path("unionId") String unionId);
//
//  /**
//   * 版本更新
//   */
//  @GET("/v1/pikachu/totoro/client/android/{version}/update-check")
//  Observable<UpdateResponse> getUpdate(@Path("version") String version);
//
//  /**
//   * 百度（紫金）任务是否跳过安装直接给奖励
//   */
//  @GET("/v1/pikachu/totoro/customers/skipped")
//  Observable<Skipped> getSkipped();
//
//  /**
//   * 分享任务
//   */
//  @GET("v1/pikachu/totoro/task/{unionId}/share/wx/{taskId}")
//  Observable<ShareAgentTask> getTaskShare(@Path("unionId") String unionId,
//                                          @Path("taskId") String taskId);
//
//  /**
//   * 首页banner
//   */
//  @POST("/v3/pikachu/totoro/common/banner/list")
//  Observable<BroadcastAdItems> getBroadcastAd(@Query("type") int type);
//
//  /**
//   * 任务凭证信息
//   */
//  @POST("v3/pikachu/totoro/agent/task/cert/component")
//  Observable<BaseResponse<Component>> getSubmitInfo(@Query("taskNo") long taskNo,
//                                                    @Query("certId") long certId);
//
//  /**
//   * 保存任务凭证
//   */
//  @POST("/v3/pikachu/totoro/agent/task/cert/save")
//  Observable<BaseResponse> saveTaskStepInfo(@Body Object taskStepInfoBody);
//
//  /**
//   * 提交任务凭证
//   */
//  @POST("v3/pikachu/totoro/agent/task/cert/submit")
//  Observable<BaseResponse> submitTask(@Body Object submitCertBody);
//
//  /**
//   * 收益明细总汇
//   */
//  @POST("/v3/pikachu/totoro/accounts/income/total")
//  Observable<BaseResponse<List<IncomeTotal>>> getIncomeTotal();
//
//  /**
//   * 各项收益明细
//   */
//  @POST("/v3/pikachu/totoro/accounts/income/list")
//  Observable<BaseResponse<IncomeList>> getIncomeList(@Body IncomeListBody incomeListBody);
//
//  /**
//   * 登录
//   */
//  @POST("/v3/pikachu/totoro/common/index/login")
//  Observable<BaseResponse<WxUserInfo>> login(@Body LoginBody loginBody);
//
//  /**
//   * 检查今天是否需要签到
//   *
//   * @return
//   */
//  @POST("/v3/pikachu/totoro/area/checkin/exist")
//  Observable<BaseResponse<Boolean>> checkInExist();
//
//  /**
//   * 特工新手引导
//   *
//   * @return
//   */
//  @POST("/v3/pikachu/totoro/accounts/newer-guide")
//  Observable<BaseResponse<AgentGuide>> getAgentNewerGuide();
//
//  /**
//   * 保存阿里妈妈成功订单信息
//   *
//   * @param orderSaveBody
//   * @return
//   */
//  @POST("/v3/pikachu/totoro/alimama/order/save")
//  Observable<BaseResponse> saveAlimamaOrder(@Body OrderSaveBody orderSaveBody);
//
//  /**
//   * 获取返钱列表
//   *
//   * @param lastTime
//   * @return
//   */
//  @POST("/v3/pikachu/totoro/alimama/order/list")
//  Observable<BaseResponse<List<AlimamaOrderList>>> getAlimamaOrderList(
//          @Body AlimamaOrderListBody lastTime);
//
//  /**
//   * 获取返钱比率
//   *
//   * @return
//   */
//  @POST("/v3/pikachu/totoro/alimama/order/percent")
//  Observable<BaseResponse<RefundInfo>> getRefundRate();
//
//  /**
//   * 试玩专区所有渠道加载
//   *
//   * @return
//   */
//  @POST("/v3/pikachu/totoro/area/pack/listAllType")
//  Observable<BaseResponse<List<AreaPackAllType>>> getListAllType();
//
//  /**
//   * 获取已做的用户列表
//   *
//   * @param uid
//   * @param id
//   * @return
//   */
//  @POST("/v3/pikachu/totoro/agent/share/list")
//  Observable<BaseResponse<ShareAgentResult>> getSearchResult(@Query("uid") String uid,
//                                                             @Query("id") String id);
}
