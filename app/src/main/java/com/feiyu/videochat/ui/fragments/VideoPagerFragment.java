package com.feiyu.videochat.ui.fragments;

import android.os.Bundle;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseFragment;

public class VideoPagerFragment extends XBaseFragment {
    public static VideoPagerFragment instance;

    public static VideoPagerFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new VideoPagerFragment();
    }

    public VideoPagerFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    public Object newP() {
        return null;
    }
}
