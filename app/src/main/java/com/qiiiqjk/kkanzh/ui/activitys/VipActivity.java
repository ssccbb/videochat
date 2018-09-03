package com.qiiiqjk.kkanzh.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qiiiqjk.kkanzh.common.Constants;
import com.qiiiqjk.kkanzh.model.PhoneVertifyResult;
import com.qiiiqjk.kkanzh.net.api.Api;
import com.qiiiqjk.kkanzh.net.httprequest.ApiCallback;
import com.qiiiqjk.kkanzh.net.httprequest.okhttp.JKOkHttpParamKey;
import com.qiiiqjk.kkanzh.net.httprequest.okhttp.OkHttpRequestUtils;
import com.qiiiqjk.kkanzh.R;
import com.qiiiqjk.kkanzh.common.XBaseActivity;
import com.qiiiqjk.kkanzh.model.UniformAlipayPayResult;
import com.qiiiqjk.kkanzh.model.UniformWeixinPayResult;
import com.qiiiqjk.kkanzh.utils.SharedPreUtil;
import com.qiiiqjk.kkanzh.utils.WXPayUtils;
import com.qiiiqjk.kkanzh.views.dialog.OrderPayDialog;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

/**
 * VIP开通页面
 * */
public class VipActivity extends XBaseActivity implements View.OnClickListener{
    public static final String TAG = VipActivity.class.getSimpleName();
    private String alipay_state = "0";
    private String weixin_state = "0";

    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.tittle)
    TextView mTittle;
    @BindView(R.id.open_vip)
    View open;

    @Override
    public void initData(Bundle savedInstanceState) {
        mTittle.setText("VIP中心");
        mBack.setOnClickListener(this);
        open.setOnClickListener(this);
        postChargeType();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vip;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static void open(Context context){
        Intent goTo = new Intent(context, VipActivity.class);
        context.startActivity(goTo);
    }

    @Override
    public void onClick(View v) {
        if (v == mBack){
            this.finish();
        }
        if (v == open){
            showChargeDialog(58);
        }
    }

    /**
     * post获取可用充值方式
     * */
    private void postChargeType(){
        OkHttpRequestUtils.getInstance().requestByPost(Api.API_BASE_URL +"/pay/pay_config",
                OkHttpRequestUtils.getInstance().JkRequestParameters(JKOkHttpParamKey.GET_USER_INFO, SharedPreUtil.getLoginInfo().uid),
                PhoneVertifyResult.class, VipActivity.this, new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.e(TAG, "onSuccess: "+(String) response );
                        try {
                            JSONObject jsonObject = new JSONObject((String)response);
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONObject wx = data.getJSONObject("wx");
                            weixin_state = wx.getString("state");
                            JSONObject alipay = data.getJSONObject("alipay");
                            alipay_state = alipay.getString("state");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String err_msg) {
                        Log.e(TAG, "onError: "+err_msg );
                    }

                    @Override
                    public void onFailure() {
                        Log.e(TAG, "onFailure !");
                    }
                });
    }

    private void showChargeDialog(int price){
        OrderPayDialog orderPayDialog = new OrderPayDialog(VipActivity.this, alipay_state, weixin_state);
        Bundle bundle = new Bundle();
        bundle.putInt(OrderPayDialog.TAG, price);
        orderPayDialog.addOnChargeClickListener(new OrderPayDialog.onChargeClickListener() {
            @Override
            public void onChargeClick(UniformAlipayPayResult payBody) {
                orderPayDialog.dismissAllowingStateLoss();
            }

            @Override
            public void onChargeClick(UniformWeixinPayResult wxpayBody) {
                orderPayDialog.dismissAllowingStateLoss();
                if (wxpayBody != null){
                    PayReq payReq = WXPayUtils.weChatPay(wxpayBody);
                    Constants.WECHAT_APP_ID = wxpayBody.appid;
                    IWXAPI iwxapi = WXAPIFactory.createWXAPI(VipActivity.this, null, true);
                    iwxapi.registerApp(wxpayBody.appid);
                    // 拉起微信支付
                    iwxapi.sendReq(payReq);
                }
            }
        });
        orderPayDialog.setArguments(bundle);
        orderPayDialog.show(this.getSupportFragmentManager(), OrderPayDialog.TAG);
    }

    // 支付结果返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PayResp payResp) {
        switch (payResp.errCode) {
            case 0: // 支付成功
                Toast.makeText(VipActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                break;
            case -1: // 支付失败
                Toast.makeText(VipActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                break;
            case -2: // 支付取消
                Toast.makeText(VipActivity.this, "支付取消", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
