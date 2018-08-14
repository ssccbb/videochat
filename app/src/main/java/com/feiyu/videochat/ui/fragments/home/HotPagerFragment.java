package com.feiyu.videochat.ui.fragments.home;

import android.os.Bundle;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseFragment;

public class HotPagerFragment extends XBaseFragment {
    public static HotPagerFragment instance;

    public static HotPagerFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new HotPagerFragment();
    }

    public HotPagerFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_child_hot;
    }

    @Override
    public Object newP() {
        return null;
    }
}
