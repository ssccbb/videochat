package com.feiyu.videochat.net.httprequest.retrofit;

import android.app.Activity;

import com.feiyu.videochat.net.httprequest.ApiCallback;
import com.feiyu.videochat.net.httprequest.CommonRetrofitCallback;

/**
 * @className: JkRetrofitCallBack
 * @classDescription: retrofit健客回调（用于兼容数据返回）
 * @author: leibing
 * @createTime: 2017/4/16
 */
public class JkRetrofitCallBack extends CommonRetrofitCallback {

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/4/16
     * @lastModify 2017/4/16
     * @param mCallback
     * @param activity
     * @param typeCls
     * @param requestId
     * @return
     */
    public JkRetrofitCallBack(ApiCallback mCallback, Activity activity,
                              Class typeCls, String requestId) {
        super(mCallback, activity, typeCls, requestId);
    }

    @Override
    public void compatibleData() {
        System.out.println("xxxxxxxxxxxx compatibleData ing ");
    }
}
