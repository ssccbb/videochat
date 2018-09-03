package com.qiiiqjk.kkanzh.common;

import com.qiiiqjk.kkanzh.R;

/**
 * Created by sung on 2018/5/4.
 */

public class Constants {
    public static final String default_user_name = "微聊用户";
    public static final int[] round_color = {
            R.color.app_pink,
            R.color.app_purpes,
            R.color.app_color,
            R.color.app_brown};
    public static final int[] round_bg = {
            R.drawable.bg_gradient_radius10_hoop_blue,
            R.drawable.bg_gradient_radius10_hoop_orange,
            R.drawable.bg_gradient_radius10_hoop_pink,
            R.drawable.bg_gradient_radius10_hoop_purpers,
            R.drawable.bg_gradient_radius10_hoop_theme};
    public static final String[] home_indicator_tag = {"首页","视频","消息","我的"};

    public static final String SP_LOGIN_INFO = "sp_login_info";

    public static final int BLUR_RADIUS = 4;
    public static final int BLUR_SAMPLING = 2;

    // 微信支付APPID 每次请求后改变
    public static String WECHAT_APP_ID = "";

    public static final String Charge_Type_Diamond = "0";
    public static final String Charge_Type_Vip = "1";
    public static final String Charge_Channel_Alipay = "alipay";
    public static final String Charge_Channel_Weixin = "lx_wx";

    public static final String HOST_IS_WATCH = "1";
    public static final String HOST_NULL_WATCH = "0";

    /**
     * 普通用户
     * */
    public static final int NORMAL_USER_TYPE = 0;

    /**
     * 用户状态
     * HOST_STATUS_BUSY 勿扰
     * HOST_STATUS_CHAT 在聊
     * HOST_STATUS_FREE 空闲
     * */
    public static final int HOST_STATUS_BUSY = 3;
    public static final int HOST_STATUS_CHAT = 2;
    public static final int HOST_STATUS_FREE = 1;

    /**
     * 响应状态
     * CONNECTTING_REFUSE 拒接
     * CONNECTTING_ACCEPT 接受
     * */
    public static final int CONNECTTING_REFUSE = 0;
    public static final int CONNECTTING_ACCEPT = 1;

    public static final String EXTIME = "extime";
    public static final String API_KEY = "kinneks";

    //UGC短视频播放类型
    public static final String UGC_PLAY_TYPE = "ucg_play_type";
    public static final int UGC_PLAY_TYPE_SINGLE = 1;//一个视频
    public static final int UGC_PLAY_TYPE_MULTIPLE = 2;
    public static final int UGC_PLAY_TYPE_NET = 3;

}
