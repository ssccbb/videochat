package com.feiyu.videochat.ui.fragments.home;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.feiyu.videochat.R;
import com.feiyu.videochat.adapter.HomeHotBannerAdapter;
import com.feiyu.videochat.common.XBaseFragment;
import com.feiyu.videochat.views.banner.AutoScrollViewPager;
import com.feiyu.videochat.views.banner.PagerIndicatorView;
import com.feiyu.videochat.views.banner.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HotPagerFragment extends XBaseFragment implements ViewPager.OnPageChangeListener{
    @BindView(R.id.auto_scroll_pager_indicator)
    PagerIndicatorView mPagerIndicator;
    @BindView(R.id.auto_scroll_view_pager)
    AutoScrollViewPager mBanner;

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
        if (mBanner.getAdapter() != null) return;
        List bannerData = new ArrayList();
        bannerData.add(0);
        bannerData.add(1);
        bannerData.add(2);
        bannerData.add(3);
        mPagerIndicator.setPagerCount(4);
        mPagerIndicator.setVisibility(View.VISIBLE);
        HomeHotBannerAdapter adapter = new HomeHotBannerAdapter(getActivity(),bannerData);
        mBanner.setAdapter(adapter);
        mBanner.setInterval(3000);
        mBanner.setPageTransformer(false, new ScaleTransformer());
        mBanner.setCurrentItem(0);
        mBanner.startAutoScroll(3000);
        mBanner.addOnPageChangeListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_child_hot;
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
        mPagerIndicator.setSelectPosition(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
