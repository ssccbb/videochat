package com.feiyu.videochat.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseFragment;

import butterknife.BindView;

public class UserFollowFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = UserFollowFragment.class.getSimpleName();
    public static UserFollowFragment instance;

    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.tittle)
    TextView mTIttle;

    public static UserFollowFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new UserFollowFragment();
    }

    public UserFollowFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mTIttle.setText("关注");
        mBack.setOnClickListener(this::onClick);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_follow;
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
    }
}