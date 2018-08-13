package com.feiyu.videochat.ui.fragments;

import android.os.Bundle;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseFragment;

public class MsgPagerFragment extends XBaseFragment {
    public static MsgPagerFragment instance;

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

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public Object newP() {
        return null;
    }
}
