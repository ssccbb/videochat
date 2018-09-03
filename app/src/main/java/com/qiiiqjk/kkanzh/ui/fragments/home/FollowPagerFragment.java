package com.qiiiqjk.kkanzh.ui.fragments.home;

import android.os.Bundle;

import com.qiiiqjk.kkanzh.common.XBaseFragment;
import com.qiiiqjk.kkanzh.R;

public class FollowPagerFragment extends XBaseFragment {
    public static FollowPagerFragment instance;

    public static FollowPagerFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new FollowPagerFragment();
    }

    public FollowPagerFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_child_follow;
    }

    @Override
    public Object newP() {
        return null;
    }
}
