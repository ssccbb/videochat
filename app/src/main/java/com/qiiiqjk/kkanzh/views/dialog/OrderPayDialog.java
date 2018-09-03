package com.qiiiqjk.kkanzh.views.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
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

import com.qiiiqjk.kkanzh.R;
import com.qiiiqjk.kkanzh.common.Constants;
import com.qiiiqjk.kkanzh.model.PhoneVertifyResult;
import com.qiiiqjk.kkanzh.model.UniformPayResult;
import com.qiiiqjk.kkanzh.net.api.Api;
import com.qiiiqjk.kkanzh.net.httprequest.ApiCallback;
import com.qiiiqjk.kkanzh.net.httprequest.okhttp.JKOkHttpParamKey;
import com.qiiiqjk.kkanzh.net.httprequest.okhttp.OkHttpRequestUtils;
import com.qiiiqjk.kkanzh.pay.AlipayH5Activity;
import com.qiiiqjk.kkanzh.utils.SharedPreUtil;
import com.qiiiqjk.kkanzh.model.UniformAlipayPayResult;
import com.qiiiqjk.kkanzh.model.UniformWeixinPayResult;

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
    private UniformAlipayPayResult alipayBody;
    private UniformWeixinPayResult wxpayBody;

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
        mWxpay.setOnClickListener(this);

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
                        UniformPayResult result = new UniformPayResult((String) response,true);
                        if (result.alipay != null) {
                            alipayBody = result.alipay;
                            Log.e(TAG, "onSuccess ali pay model: "+alipayBody.toString() );
                            if (onChargeClickListener != null){
                                onChargeClickListener.onChargeClick(alipayBody);
                            }
                            AlipayH5Activity.open(context,alipayBody);
                        }else {
                            Toast.makeText(context, "获取订单失败！", Toast.LENGTH_SHORT).show();
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

    /**
     * post微信支付
     */
    private void postUniformWxpay(String uid, int money) {
        OkHttpRequestUtils.getInstance().requestByPost(Api.API_BASE_URL + "/pay/uniform_pay",
                OkHttpRequestUtils.getInstance().JkRequestParameters(
                        JKOkHttpParamKey.UNIFORM_PAY, uid, String.valueOf(money), Constants.Charge_Channel_Weixin, "0", Constants.Charge_Type_Diamond),
                PhoneVertifyResult.class, getActivity(), new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.e(TAG, "onSuccess: " + (String) response);
                        UniformPayResult result = new UniformPayResult((String) response,false);
                        //Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show();
                        if (result.wx != null) {
                            wxpayBody = result.wx;
                            Log.e(TAG, "onSuccess wx pay model: "+wxpayBody.toString() );
                            if (onChargeClickListener != null){
                                onChargeClickListener.onChargeClick(wxpayBody);
                            }
                        }else {
                            Toast.makeText(context, "获取订单失败！", Toast.LENGTH_SHORT).show();
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

    public void addOnChargeClickListener(onChargeClickListener onChargeClickListener) {
        this.onChargeClickListener = onChargeClickListener;
    }

    @Override
    public void onClick(View v) {
        if (v == mCancel) {
            dismissAllowingStateLoss();
        }
        if (v == mAlipay) {
            postUniformAlipay(SharedPreUtil.getLoginInfo().uid, charge_value);
        }
        if (v == mWxpay){
            postUniformWxpay(SharedPreUtil.getLoginInfo().uid,charge_value);
        }
    }

    public interface onChargeClickListener {
        void onChargeClick(UniformAlipayPayResult alipayBody);
        void onChargeClick(UniformWeixinPayResult wxpayBody);
    }
}
