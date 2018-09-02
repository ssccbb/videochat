package com.weiliao.kinnek.ui.fragments.setting;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.weiliao.kinnek.R;
import com.weiliao.kinnek.adapter.ChargeListAdapter;
import com.weiliao.kinnek.common.XBaseFragment;
import com.weiliao.kinnek.model.PayChargeItemResult;
import com.weiliao.kinnek.model.PhoneVertifyResult;
import com.weiliao.kinnek.model.UniformAlipayPayResult;
import com.weiliao.kinnek.model.UserInfoResult;
import com.weiliao.kinnek.net.StateCode;
import com.weiliao.kinnek.net.api.Api;
import com.weiliao.kinnek.net.httprequest.ApiCallback;
import com.weiliao.kinnek.net.httprequest.okhttp.JKOkHttpParamKey;
import com.weiliao.kinnek.net.httprequest.okhttp.OkHttpRequestUtils;
import com.weiliao.kinnek.utils.SharedPreUtil;
import com.weiliao.kinnek.views.dialog.OrderPayDialog;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

public class ChargeFragment extends XBaseFragment implements View.OnClickListener,ChargeListAdapter.OnChargeItemClickListener{
    public static final String TAG = ChargeFragment.class.getSimpleName();
    public static ChargeFragment instance;
    private UserInfoResult user;
    private ChargeListAdapter mAdapter;
    private String alipay_state = "0";
    private String weixin_state = "0";

    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.list)
    RecyclerView mList;
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
        mQuestion.setOnClickListener(this::onClick);
        checkChargeList();

        if (!SharedPreUtil.isLogin()) return;
        postUserInfo();
        postChargeList();
        postChargeType();
    }

    private void checkChargeList(){
        mAdapter = new ChargeListAdapter(getActivity(),null);
        mAdapter.addOnChargeItemClickListener(this);
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.setItemAnimator(new DefaultItemAnimator());
        mList.setAdapter(mAdapter);
        mList.setHasFixedSize(true);
    }

    /**
     * post获取用户
     * */
    private void postUserInfo(){
        OkHttpRequestUtils.getInstance().requestByPost(Api.API_BASE_URL +"/user/get_info",
                OkHttpRequestUtils.getInstance().JkRequestParameters(JKOkHttpParamKey.GET_USER_INFO, SharedPreUtil.getLoginInfo().uid),
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

    /**
     * post获取充值列表
     * */
    private void postChargeList(){
        OkHttpRequestUtils.getInstance().requestByPost(Api.API_BASE_URL +"/pay/item_list",
                OkHttpRequestUtils.getInstance().JkRequestParameters(JKOkHttpParamKey.GET_USER_INFO, SharedPreUtil.getLoginInfo().uid),
                PhoneVertifyResult.class, getActivity(), new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.e(TAG, "onSuccess: "+(String) response );
                        PayChargeItemResult payChargeItemResult = new PayChargeItemResult((String) response);
                        if (payChargeItemResult.code.equals(StateCode.STATE_0000)) {
                            mAdapter.addData(payChargeItemResult.data);
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

    /**
     * post获取可用充值方式
     * */
    private void postChargeType(){
        OkHttpRequestUtils.getInstance().requestByPost(Api.API_BASE_URL +"/pay/pay_config",
                OkHttpRequestUtils.getInstance().JkRequestParameters(JKOkHttpParamKey.GET_USER_INFO, SharedPreUtil.getLoginInfo().uid),
                PhoneVertifyResult.class, getActivity(), new ApiCallback() {
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
                            Log.e(TAG, "postChargeType: "+e.toString() );
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
        if (v == mQuestion){
            Toast.makeText(getActivity(), "帮助", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onChargeItemClick(int price) {
        showChargeDialog(price);
    }

    private void showChargeDialog(int price){
        OrderPayDialog orderPayDialog = new OrderPayDialog(getActivity(), alipay_state, weixin_state);
        Bundle bundle = new Bundle();
        bundle.putInt(OrderPayDialog.TAG, price);
        orderPayDialog.addOnChargeClickListener(new OrderPayDialog.onChargeClickListener() {
            @Override
            public void onChargeClick(UniformAlipayPayResult payBody) {
                orderPayDialog.dismissAllowingStateLoss();
            }
        });
        orderPayDialog.setArguments(bundle);
        orderPayDialog.show(getChildFragmentManager(), OrderPayDialog.TAG);
    }
}
