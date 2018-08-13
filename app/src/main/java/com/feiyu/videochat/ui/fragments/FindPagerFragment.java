package com.feiyu.videochat.ui.fragments;

import android.os.Bundle;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseFragment;

public class FindPagerFragment extends XBaseFragment {
    public static FindPagerFragment instance;

    public static FindPagerFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new FindPagerFragment();
    }

    public FindPagerFragment() {
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
