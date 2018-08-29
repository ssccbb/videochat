package com.feiyu.videochat.net.httprequest.okhttp;

/**
 * @className: JKOkHttpParamKey
 * @classDescription: 参数key集合
 * @author: leibing
 * @createTime: 2017/4/11
 */
public class JKOkHttpParamKey {
    public final static String[] GET_USER_INFO = {"uid"};
    public final static String[] SINGLE_PAGE = {"page"};
    public final static String[] PHONE_VERTIFY_CODE = {"phone"};
    public final static String[] VERTIFY_CODE_LOGIN = {"phone","verification_code","os"};
    public final static String[] VIP_VIDEO = {"page","uid"};
    public final static String[] ANCHOR_INFO = {"anchor_uid","uid"};
}
