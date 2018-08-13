package com.feiyu.videochat.ui.fragments;

import android.os.Bundle;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseFragment;

public class GroupPagerFragment extends XBaseFragment {
    public static GroupPagerFragment instance;

    public static GroupPagerFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new GroupPagerFragment();
    }

    public GroupPagerFragment() {
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
