package com.weiliao.kinnek.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.weiliao.kinnek.common.Constants;
import com.weiliao.kinnek.common.XBaseFragment;
import com.weiliao.kinnek.model.PhoneVertifyResult;
import com.weiliao.kinnek.model.UserInfoResult;
import com.weiliao.kinnek.net.StateCode;
import com.weiliao.kinnek.net.api.Api;
import com.weiliao.kinnek.net.httprequest.ApiCallback;
import com.weiliao.kinnek.net.httprequest.okhttp.JKOkHttpParamKey;
import com.weiliao.kinnek.net.httprequest.okhttp.OkHttpRequestUtils;
import com.weiliao.kinnek.ui.activitys.SettingActivity;
import com.weiliao.kinnek.ui.activitys.UserActivity;
import com.weiliao.kinnek.ui.activitys.VipActivity;
import com.weiliao.kinnek.ui.fragments.setting.ChargeFragment;
import com.weiliao.kinnek.ui.fragments.setting.HelpFragment;
import com.weiliao.kinnek.ui.fragments.setting.SysSettingFragment;
import com.weiliao.kinnek.utils.SharedPreUtil;
import com.weiliao.kinnek.utils.StringUtils;
import com.weiliao.kinnek.views.mine.SelectItemView;
import com.weiliao.kinnek.R;
import com.weiliao.kinnek.ui.fragments.setting.UserInfoEditFragment;

import butterknife.BindView;

public class MinePagerFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = MinePagerFragment.class.getSimpleName();
    public static MinePagerFragment instance;
    private UserInfoResult user;

    @BindView(R.id.follow)
    View mFollow;
    @BindView(R.id.fans)
    View mFans;
    @BindView(R.id.video)
    View mVideo;
    @BindView(R.id.item_wallet)
    SelectItemView mWallet;
    @BindView(R.id.item_profit)
    View mProfit;
    @BindView(R.id.item_vip)
    SelectItemView mVip;
    @BindView(R.id.item_level)
    View mLevel;
    @BindView(R.id.item_behost)
    View mBehost;
    @BindView(R.id.item_help)
    View mHelp;
    @BindView(R.id.item_setting)
    View mSetting;
    @BindView(R.id.user_head)
    View mUser;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.idnumber)
    TextView id;

    public static MinePagerFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new MinePagerFragment();
    }

    public MinePagerFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mFollow.setOnClickListener(this);
        mFans.setOnClickListener(this);
        mVideo.setOnClickListener(this);
        mWallet.setOnClickListener(this);
        mProfit.setOnClickListener(this);
        mVip.setOnClickListener(this);
        mLevel.setOnClickListener(this);
        mBehost.setOnClickListener(this);
        mHelp.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mUser.setOnClickListener(this);

        mProfit.setVisibility(View.GONE);
        mLevel.setVisibility(View.GONE);
        mBehost.setVisibility(View.GONE);
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
                        bindUiData();
                    }

                    @Override
                    public void onError(String err_msg) {
                        Toast.makeText(getActivity(), "用户拉取失败！", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onError: "+err_msg );
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getActivity(), "用户拉取失败！", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onFailure !");
                    }
                });
    }

    private void bindUiData(){
        id.setText("ID："+user.user_id);
        mWallet.setDataNumber(""+user.diamond);
        mVip.setDataNumber(user.vip.equals("0") ? "未开通会员" : "已开通会员");
        name.setText(StringUtils.isEmpty(user.nickname) ? Constants.default_user_name : user.nickname);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!SharedPreUtil.isLogin()) return;
        postUserInfo(SharedPreUtil.getLoginInfo().uid);
    }

    @Override
    public void onClick(View v) {
        if (v == mHelp){
            SettingActivity.open(getActivity(), HelpFragment.TAG);
            return;
        }
        if (!SharedPreUtil.isLogin()){
            login();
            return;
        }
        if (v == mFollow){
            UserActivity.open(getActivity(),UserFollowFragment.TAG);
        }
        if (v == mFans){
            UserActivity.open(getActivity(),UserFansFragment.TAG);
        }
        if (v == mVideo){
            UserActivity.open(getActivity(),UserVideoFragment.TAG);
        }
        if (v == mWallet){
            SettingActivity.open(getActivity(), ChargeFragment.TAG);
        }
        if (v == mProfit){

        }
        if (v == mVip){
            VipActivity.open(getActivity());
        }
        if (v == mLevel){

        }
        if (v == mBehost){

        }
        if (v == mSetting){
            SettingActivity.open(getActivity(), SysSettingFragment.TAG);
        }
        if (v == mUser){
            SettingActivity.open(getActivity(), UserInfoEditFragment.TAG);
        }
    }

    private void login(){
        LoginDialogFragment login = new LoginDialogFragment();
        login.show(getActivity().getSupportFragmentManager(),LoginDialogFragment.TAG);
    }
}
