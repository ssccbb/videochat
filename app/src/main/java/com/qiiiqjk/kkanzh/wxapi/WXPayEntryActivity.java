package com.qiiiqjk.kkanzh.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.qiiiqjk.kkanzh.common.Constants;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by timzhang on 2016/12/19.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    /**
     * 微信支付返回
     *
     * @param resp
     */
    @Override
    public void onResp(BaseResp resp) {
        Log.e("HHH", String.format("微信支付返回 resp type %d err code %d err str %s", resp.getType(), resp.errCode, resp.errStr));
        Log.e("HHH", "微信支付返回 " + resp);
//        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            if (resp instanceof PayResp) {
        PayResp payResp = (PayResp) resp;
        EventBus.getDefault().post(payResp);
//                switch (resp.errCode) {
//                    case 0: // 支付成功
//                        RxBus.getDefault().post(new WechatPayEvent(payResp.errCode, payResp));
//                        break;
//                    case -1: // 支付失败
//                        ToastUtils.showShortToastSafe(R.string.rechargeFail);
//                        break;
//                    case -2: // 支付取消
//                        break;
//                }
//            }
//        }
        finish();
    }
}
