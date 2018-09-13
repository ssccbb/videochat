package com.weiliao.kinnek.ui.fragments.home;

import android.os.Bundle;

import com.weiliao.kinnek.common.XBaseFragment;
import com.weiliao.kinnek.R;

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
