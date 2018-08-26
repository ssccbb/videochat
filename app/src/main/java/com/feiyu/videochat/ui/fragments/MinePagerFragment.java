package com.feiyu.videochat.ui.fragments;

import android.os.Bundle;
import android.view.View;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseFragment;
import com.feiyu.videochat.ui.activitys.SettingActivity;
import com.feiyu.videochat.ui.activitys.UserActivity;
import com.feiyu.videochat.ui.activitys.VipActivity;
import com.feiyu.videochat.ui.fragments.setting.ChargeFragment;
import com.feiyu.videochat.ui.fragments.setting.HelpFragment;
import com.feiyu.videochat.ui.fragments.setting.SysSettingFragment;
import com.feiyu.videochat.ui.fragments.setting.UserInfoEditFragment;
import com.feiyu.videochat.utils.SharedPreUtil;

import butterknife.BindView;

public class MinePagerFragment extends XBaseFragment implements View.OnClickListener{
    public static MinePagerFragment instance;

    @BindView(R.id.follow)
    View mFollow;
    @BindView(R.id.fans)
    View mFans;
    @BindView(R.id.video)
    View mVideo;
    @BindView(R.id.item_wallet)
    View mWallet;
    @BindView(R.id.item_profit)
    View mProfit;
    @BindView(R.id.item_vip)
    View mVip;
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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public Object newP() {
        return null;
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
