package com.weiliao.kinnek.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.weiliao.kinnek.model.PhoneVertifyResult;
import com.weiliao.kinnek.net.api.Api;
import com.weiliao.kinnek.net.httprequest.ApiCallback;
import com.weiliao.kinnek.net.httprequest.okhttp.JKOkHttpParamKey;
import com.weiliao.kinnek.net.httprequest.okhttp.OkHttpRequestUtils;
import com.weiliao.kinnek.R;
import com.weiliao.kinnek.common.XBaseActivity;
import com.weiliao.kinnek.model.UniformAlipayPayResult;
import com.weiliao.kinnek.model.UniformWeixinPayResult;
import com.weiliao.kinnek.utils.SharedPreUtil;
import com.weiliao.kinnek.views.dialog.OrderPayDialog;

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

                }
            }
        });
        orderPayDialog.setArguments(bundle);
        orderPayDialog.show(this.getSupportFragmentManager(), OrderPayDialog.TAG);
    }
}
