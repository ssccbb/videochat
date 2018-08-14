package com.feiyu.videochat.ui.fragments;

import android.os.Bundle;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseFragment;

public class MinePagerFragment extends XBaseFragment {
    public static MinePagerFragment instance;

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

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public Object newP() {
        return null;
    }
}
