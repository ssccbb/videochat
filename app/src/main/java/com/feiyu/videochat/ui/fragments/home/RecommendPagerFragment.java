package com.feiyu.videochat.ui.fragments.home;

import android.os.Bundle;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseFragment;

public class RecommendPagerFragment extends XBaseFragment {
    public static RecommendPagerFragment instance;

    public static RecommendPagerFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new RecommendPagerFragment();
    }

    public RecommendPagerFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_child_recommend;
    }

    @Override
    public Object newP() {
        return null;
    }
}
