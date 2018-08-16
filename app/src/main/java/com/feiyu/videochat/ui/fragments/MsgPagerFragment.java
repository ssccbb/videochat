package com.feiyu.videochat.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseFragment;
import com.feiyu.videochat.ui.activitys.MsgActivity;

import butterknife.BindView;

public class MsgPagerFragment extends XBaseFragment implements View.OnClickListener{
    public static MsgPagerFragment instance;

    @BindView(R.id.text)
    TextView mHeadText;
    @BindView(R.id.item_system)
    View mSystem;
    @BindView(R.id.item_commend)
    View mCommend;
    @BindView(R.id.item_like)
    View mLike;
    @BindView(R.id.item_black)
    View mBlack;

    public static MsgPagerFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new MsgPagerFragment();
    }

    public MsgPagerFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mHeadText.setText("消息");
        mSystem.setOnClickListener(this);
        mCommend.setOnClickListener(this);
        mLike.setOnClickListener(this);
        mBlack.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_msg;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View v) {
        Intent goTo = new Intent(getActivity(), MsgActivity.class);
        if (v == mSystem){
            goTo.putExtra(MsgActivity.TAG,"系统消息");
        }
        if (v == mCommend){
            goTo.putExtra(MsgActivity.TAG,"评论");
        }
        if (v == mLike){
            goTo.putExtra(MsgActivity.TAG,"点赞");
        }
        if (v == mBlack){
            goTo.putExtra(MsgActivity.TAG,"黑名单");
        }
        startActivity(goTo);
    }
}
