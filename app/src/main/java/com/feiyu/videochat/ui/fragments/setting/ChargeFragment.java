package com.feiyu.videochat.ui.fragments.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseFragment;
import com.feiyu.videochat.model.PhoneVertifyResult;
import com.feiyu.videochat.model.UserInfoResult;
import com.feiyu.videochat.net.StateCode;
import com.feiyu.videochat.net.api.Api;
import com.feiyu.videochat.net.httprequest.ApiCallback;
import com.feiyu.videochat.net.httprequest.okhttp.JKOkHttpParamKey;
import com.feiyu.videochat.net.httprequest.okhttp.OkHttpRequestUtils;
import com.feiyu.videochat.utils.SharedPreUtil;
import com.feiyu.videochat.views.dialog.OrderPayDialog;

import butterknife.BindView;

public class ChargeFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = ChargeFragment.class.getSimpleName();
    public static ChargeFragment instance;
    private UserInfoResult user;

    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.item_500)
    View mItem500;
    @BindView(R.id.item_1580)
    View mItem1580;
    @BindView(R.id.item_2980)
    View mItem2980;
    @BindView(R.id.item_5200)
    View mItem5200;
    @BindView(R.id.item_13140)
    View mItem13140;
    @BindView(R.id.item_28880)
    View mItem28880;
    @BindView(R.id.item_38880)
    View mItem38880;
    @BindView(R.id.question)
    View mQuestion;
    @BindView(R.id.diamond)
    TextView diamond;

    public static ChargeFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new ChargeFragment();
    }

    public ChargeFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mBack.setOnClickListener(this::onClick);
        mItem500.setOnClickListener(this::onClick);
        mItem1580.setOnClickListener(this::onClick);
        mItem2980.setOnClickListener(this::onClick);
        mItem5200.setOnClickListener(this::onClick);
        mItem13140.setOnClickListener(this::onClick);
        mItem28880.setOnClickListener(this::onClick);
        mItem38880.setOnClickListener(this::onClick);
        mQuestion.setOnClickListener(this::onClick);

        if (!SharedPreUtil.isLogin()) return;
        postUserInfo(SharedPreUtil.getLoginInfo().uid);
    }

    /**
     * post获取用户
     * */
    private void postUserInfo(String uid){
        OkHttpRequestUtils.getInstance().requestByPost(Api.API_BASE_URL +"/user/get_info",
                OkHttpRequestUtils.getInstance().JkRequestParameters(JKOkHttpParamKey.GET_USER_INFO, uid),
                PhoneVertifyResult.class, getActivity(), new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.e(TAG, "onSuccess: "+(String) response );
                        user = new UserInfoResult((String) response);
                        if (!user.code.equals(StateCode.STATE_0000)){
                            Toast.makeText(getActivity(), user.message, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        diamond.setText("账户余额："+user.diamond);
                    }

                    @Override
                    public void onError(String err_msg) {
                        //Toast.makeText(getActivity(), "用户拉取失败！", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onError: "+err_msg );
                    }

                    @Override
                    public void onFailure() {
                        //Toast.makeText(getActivity(), "用户拉取失败！", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onFailure !");
                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_charge;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == mBack){
            getActivity().finish();
        }
        if (v == mItem500){
            showChargeDialog(50);
        }
        if (v == mItem1580){
            showChargeDialog(158);
        }
        if (v == mItem2980){
            showChargeDialog(298);
        }
        if (v == mItem5200){
            showChargeDialog(520);
        }
        if (v == mItem13140){
            showChargeDialog(1314);
        }
        if (v == mItem28880){
            showChargeDialog(2888);
        }
        if (v == mItem38880){
            showChargeDialog(3888);
        }
        if (v == mQuestion){
            Toast.makeText(getActivity(), "帮助", Toast.LENGTH_SHORT).show();
        }
    }

    private void showChargeDialog(int price){
        OrderPayDialog orderPayDialog = new OrderPayDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(OrderPayDialog.TAG, price);
        orderPayDialog.addOnChargeClickListener(new OrderPayDialog.onChargeClickListener() {
            @Override
            public void onChargeClick(int charge_value) {
                orderPayDialog.dismissAllowingStateLoss();
                Toast.makeText(getActivity(), ""+charge_value, Toast.LENGTH_SHORT).show();
            }
        });
        orderPayDialog.setArguments(bundle);
        orderPayDialog.show(getChildFragmentManager(), OrderPayDialog.TAG);
    }
}
