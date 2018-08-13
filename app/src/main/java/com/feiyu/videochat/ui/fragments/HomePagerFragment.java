package com.feiyu.videochat.ui.fragments;

import android.os.Bundle;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseFragment;

public class HomePagerFragment extends XBaseFragment {
    public static HomePagerFragment instance;

    public static HomePagerFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new HomePagerFragment();
    }

    public HomePagerFragment() {
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
