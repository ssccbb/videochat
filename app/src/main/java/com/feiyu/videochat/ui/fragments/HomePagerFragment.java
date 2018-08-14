package com.feiyu.videochat.ui.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.feiyu.videochat.App;
import com.feiyu.videochat.R;
import com.feiyu.videochat.adapter.HomeChildPagerAdapter;
import com.feiyu.videochat.common.XBaseFragment;

import butterknife.BindView;

public class HomePagerFragment extends XBaseFragment implements ViewPager.OnPageChangeListener{
    @BindView(R.id.view_pager_1)
    ViewPager mPager;

    public static HomePagerFragment instance;
    private HomeChildPagerAdapter mHomeAdapter;

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
        if (mHomeAdapter != null) return;
        mHomeAdapter = new HomeChildPagerAdapter(App.getContext(), getActivity().getSupportFragmentManager());
        mPager.setAdapter(mHomeAdapter);
        mPager.setOffscreenPageLimit(0);
        mPager.addOnPageChangeListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
