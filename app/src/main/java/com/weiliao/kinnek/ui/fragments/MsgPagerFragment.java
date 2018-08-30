package com.weiliao.kinnek.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.weiliao.kinnek.R;
import com.weiliao.kinnek.common.XBaseFragment;
import com.weiliao.kinnek.ui.activitys.MsgActivity;

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
        if (v == mSystem){
            MsgActivity.open(getActivity(),"系统消息");
        }
        if (v == mCommend){
            MsgActivity.open(getActivity(),"评论");
        }
        if (v == mLike){
            MsgActivity.open(getActivity(),"点赞");
        }
        if (v == mBlack){
            MsgActivity.open(getActivity(),"黑名单");
        }
    }
}
