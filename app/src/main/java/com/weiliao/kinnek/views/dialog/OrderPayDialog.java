package com.weiliao.kinnek.views.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.weiliao.kinnek.BuildConfig;
import com.weiliao.kinnek.R;
import com.weiliao.kinnek.common.Constants;
import com.weiliao.kinnek.model.PhoneVertifyResult;
import com.weiliao.kinnek.model.UniformAlipayPayResult;
import com.weiliao.kinnek.model.UniformPayResult;
import com.weiliao.kinnek.net.api.Api;
import com.weiliao.kinnek.net.httprequest.ApiCallback;
import com.weiliao.kinnek.net.httprequest.okhttp.JKOkHttpParamKey;
import com.weiliao.kinnek.net.httprequest.okhttp.OkHttpRequestUtils;
import com.weiliao.kinnek.pay.AlipayH5Activity;
import com.weiliao.kinnek.ui.activitys.VipActivity;
import com.weiliao.kinnek.utils.SharedPreUtil;

/**
 * Created by sun on 21/12/2017.
 */

@SuppressLint("ValidFragment")
public class OrderPayDialog extends DialogFragment implements View.OnClickListener {
    public static final String TAG = OrderPayDialog.class.getSimpleName();
    private onChargeClickListener onChargeClickListener;
    private Activity context;
    private String alipay_state = "0";
    private String weixin_state = "0";
    private View mCancel, mAlipay, mWxpay;
    private TextView mPrice;
    private int charge_value = 0;
    private UniformAlipayPayResult payBody;

    @SuppressLint("ValidFragment")
    public OrderPayDialog(Activity context, String alipay_state, String weixin_state) {
        this.context = context;
        this.alipay_state = alipay_state;
        this.weixin_state = weixin_state;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout(dm.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_FullScreenDialog);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View inflate = layoutInflater.inflate(R.layout.view_charge_dialog, null);
        dialog.setContentView(inflate);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        //window.setWindowAnimations(R.style.BottomDialogAnimation);

        mCancel = inflate.findViewById(R.id.cancel);
        mAlipay = inflate.findViewById(R.id.alipay);
        mWxpay = inflate.findViewById(R.id.weixin);
        mPrice = inflate.findViewById(R.id.price);

        if (alipay_state.equals("0")){
            mAlipay.setVisibility(View.GONE);
        }
        if (weixin_state.equals("0")){
            mWxpay.setVisibility(View.GONE);
        }

        mCancel.setOnClickListener(this);
        mAlipay.setOnClickListener(this);

        charge_value = this.getArguments().getInt(TAG, 0);
        charge_value = 1;
        mPrice.setText(charge_value + ".00");
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commit();
        } catch (IllegalStateException e) {
            Log.d("ABSDIALOGFRAG", "Exception", e);
        }
    }

    /**
     * post支付宝支付
     */
    private void postUniformAlipay(String uid, int money) {
        OkHttpRequestUtils.getInstance().requestByPost(Api.API_BASE_URL + "/pay/uniform_pay",
                OkHttpRequestUtils.getInstance().JkRequestParameters(
                        JKOkHttpParamKey.UNIFORM_PAY, uid, String.valueOf(money), Constants.Charge_Channel_Alipay, "0", Constants.Charge_Type_Diamond),
                PhoneVertifyResult.class, getActivity(), new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.e(TAG, "onSuccess: " + (String) response);
                        UniformPayResult result = new UniformPayResult((String) response);
                        if (result.alipay != null) {
                            payBody = result.alipay;
                            Log.e(TAG, "onSuccess: "+payBody.toString() );
                            AlipayH5Activity.open(context,payBody);
                        }
                    }

                    @Override
                    public void onError(String err_msg) {
                        Log.e(TAG, "onError: " + err_msg);
                    }

                    @Override
                    public void onFailure() {
                        Log.e(TAG, "onFailure !");
                    }
                });
    }

    private void pay() {
        if (payBody == null || charge_value <= 0) {
            return;
        }
        OkHttpRequestUtils.getInstance().requestAlipayByPost(OkHttpRequestUtils.getInstance().JkRequestParameters(
                        JKOkHttpParamKey.ALI_PAY,
                payBody.uid,/*商户uid*/
                payBody.price,/*价格*/
                payBody.istype,/*支付渠道*/
                payBody.notify_url,/*通知回调网址*/
                payBody.return_url,/*跳转网址*/
                payBody.orderid,/*商户自定义订单号*/
                payBody.orderuid,/*商户自定义客户号*/
                payBody.goodsname,/*商品名称*/
                payBody.key),/*秘钥*/
                PhoneVertifyResult.class, getActivity(), new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.e(TAG, "onSuccess: " + (String) response);
                    }

                    @Override
                    public void onError(String err_msg) {
                        Log.e(TAG, "onError: " + err_msg);
                    }

                    @Override
                    public void onFailure() {
                        Log.e(TAG, "onFailure !");
                    }
                });
    }


    public void addOnChargeClickListener(onChargeClickListener onChargeClickListener) {
        this.onChargeClickListener = onChargeClickListener;
    }

    @Override
    public void onClick(View v) {
        if (v == mCancel) {
            dismissAllowingStateLoss();
        }
        if (v == mAlipay) {
//            if (payBody == null){
//                Toast.makeText(getActivity(), "获取参数失败", Toast.LENGTH_SHORT).show();
//                return;
//            }
            postUniformAlipay(SharedPreUtil.getLoginInfo().uid, charge_value);
//            onChargeClickListener.onChargeClick(payBody);
        }
        if (v == mWxpay){

        }
    }

    public interface onChargeClickListener {
        void onChargeClick(UniformAlipayPayResult payBody);
    }
}
