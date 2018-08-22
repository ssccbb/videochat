package com.feiyu.videochat.common;

import com.feiyu.videochat.R;

/**
 * Created by sung on 2018/5/4.
 */

public class Constants {
    public static final int[] round_color = {
            R.color.app_pink,
            R.color.app_purpes,
            R.color.app_color,
            R.color.app_brown};
    public static final String[] home_indicator_tag = {"首页","视频","消息","我的"};

    /**
     * 用户状态
     * HOST_STATUS_BUSY 勿扰
     * HOST_STATUS_CHAT 在聊
     * HOST_STATUS_FREE 空闲
     * */
    public static final int HOST_STATUS_BUSY = 0;
    public static final int HOST_STATUS_CHAT = 1;
    public static final int HOST_STATUS_FREE = 2;

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
