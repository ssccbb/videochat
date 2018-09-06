package com.qiiiqjk.kkanzh.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qiiiqjk.kkanzh.App;
import com.qiiiqjk.kkanzh.model.LoginInfoResults;
import com.qiiiqjk.kkanzh.net.api.Api;
import com.qiiiqjk.kkanzh.utils.Utils;
import com.qiiiqjk.kkanzh.R;
import com.qiiiqjk.kkanzh.model.PhoneVertifyResult;
import com.qiiiqjk.kkanzh.net.StateCode;
import com.qiiiqjk.kkanzh.net.httprequest.ApiCallback;
import com.qiiiqjk.kkanzh.net.httprequest.okhttp.JKOkHttpParamKey;
import com.qiiiqjk.kkanzh.net.httprequest.okhttp.OkHttpRequestUtils;
import com.qiiiqjk.kkanzh.utils.ScreenUtils;
import com.qiiiqjk.kkanzh.utils.SharedPreUtil;
import com.qiiiqjk.kkanzh.utils.StringUtils;
import com.qiiiqjk.kkanzh.views.dialog.VCDialog;

public class LoginDialogFragment extends DialogFragment implements View.OnClickListener,TextWatcher {
    public static final String TAG = LoginDialogFragment.class.getSimpleName();
    private VCDialog.onDialogActionListner onDialogActionListner;
    private onLoginListener onLoginListener;
    private Context mContext;

    private EditText mPhone,mCode;
    private TextView mGetCode;
    private View mClose,mAgreement,mLogin;
    private View mClearCode,mClearNumber;
    private MyCountDownTimer timer;

    public LoginDialogFragment() {
        mContext = App.getContext();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login,container,false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() == null) return;
        if (!getDialog().isShowing()) return;
        Window dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = ScreenUtils.getScreenWidth(getContext()) / 5 * 4;
        dialogWindow.setAttributes(lp);
        this.getDialog().setCancelable(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPhone = view.findViewById(R.id.phone);
        mCode = view.findViewById(R.id.code);
        mGetCode = view.findViewById(R.id.get_code);
        mClose = view.findViewById(R.id.close);
        mAgreement = view.findViewById(R.id.user_agreement);
        mLogin = view.findViewById(R.id.login);
        mClearCode = view.findViewById(R.id.clear_code);
        mClearNumber = view.findViewById(R.id.clear_number);

        timer = new MyCountDownTimer(60000,1000);
        mGetCode.setOnClickListener(this);
        mClose.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mPhone.addTextChangedListener(this);
        mCode.addTextChangedListener(this);
        mClearCode.setOnClickListener(this);
        mClearNumber.setOnClickListener(this);
    }

    private void switchUserAgreement(){

    }

    /**
     * post获取验证码
     * */
    private void postVertifyCode(String phone){
        mGetCode.setText("已发送验证码");
        mGetCode.setTextColor(Color.parseColor("#999999"));
        mGetCode.setEnabled(false);
        timer.start();
        OkHttpRequestUtils.getInstance().requestByPost(Api.API_BASE_URL +"/user/send_verification_code",
            OkHttpRequestUtils.getInstance().JkRequestParameters(JKOkHttpParamKey.PHONE_VERTIFY_CODE, phone),
            PhoneVertifyResult.class, getActivity(), new ApiCallback() {
                @Override
                public void onSuccess(Object response) {
                    //Log.e(TAG, "onSuccess: "+(String) response );
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

    /**
     * post登录
     * */
    private void postVertifyLogin(String phone, String code){
        OkHttpRequestUtils.getInstance().requestByPost(Api.API_BASE_URL +"/user/login_v",
                OkHttpRequestUtils.getInstance().JkRequestParameters(
                        JKOkHttpParamKey.VERTIFY_CODE_LOGIN, phone,code,"1"),
                PhoneVertifyResult.class, getActivity(), new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        //Log.e(TAG, "onSuccess: "+(String) response );
                        LoginInfoResults userInfo = new LoginInfoResults(response.toString());
                        if (userInfo.code.equals(StateCode.STATE_0000)){
                            SharedPreUtil.saveLoginInfo(userInfo);
                            Toast.makeText(mContext, "登录成功!", Toast.LENGTH_SHORT).show();
                            LoginDialogFragment.this.dismissAllowingStateLoss();
                            if (onLoginListener != null){
                                onLoginListener.onLoginCallback();
                            }
                        }else {
                            Log.e(TAG, "onError: "+userInfo.message.toString() );
                            Toast.makeText(mContext, userInfo.message.toString(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (timer != null){
            timer.onFinish();
            timer = null;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mGetCode){
            String phone = mPhone.getText().toString().trim();
            if (!Utils.isMobile(phone)){
                Toast.makeText(mContext, "输入有误,请重新输入", Toast.LENGTH_SHORT).show();
                return;
            }
            postVertifyCode(phone);
        }
        if (v == mClose){
            this.getDialog().dismiss();
        }
        if (v == mLogin){
            String phone = mPhone.getText().toString().trim();
            String code = mCode.getText().toString().trim();
            if (!Utils.isMobile(phone) || StringUtils.isEmpty(code)){
                Toast.makeText(mContext, "输入有误,请重新输入", Toast.LENGTH_SHORT).show();
                return;
            }
            postVertifyLogin(phone,code);
        }
        if (v == mAgreement){

        }
        if (v == mClearCode){
            mCode.setText("");
        }
        if (v == mClearNumber){
            mPhone.setText("");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s!=null||s.length()!=0){
            if (mPhone.isFocused()){
                mClearNumber.setVisibility(View.VISIBLE);
            }
            if (mCode.isFocused()){
                mClearCode.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s==null||s.length()==0){
            if (mPhone.isFocused()){
                mClearNumber.setVisibility(View.GONE);
            }
            if (mCode.isFocused()){
                mClearCode.setVisibility(View.GONE);
            }
        }
    }

    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            mGetCode.setEnabled(false);
            mGetCode.setText("重新获取("+l/1000+"s)");
        }

        @Override
        public void onFinish() {
            mGetCode.setText("获取验证码");
            mGetCode.setTextColor(Color.parseColor("#ec6053"));
            mGetCode.setEnabled(true);
        }
    }

    public interface onLoginListener {
        void onLoginCallback();
    }

    public void addOnLoginListener(onLoginListener onLoginListener){
        this.onLoginListener = onLoginListener;
    }
}
