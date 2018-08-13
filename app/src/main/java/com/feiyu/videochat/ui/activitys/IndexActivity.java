package com.feiyu.videochat.ui.activitys;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.feiyu.videochat.R;
import com.feiyu.videochat.adapter.HomePagerAdapter;
import com.feiyu.videochat.common.XBaseActivity;
import com.feiyu.videochat.views.TabIndicatorView;

import butterknife.BindView;

public class IndexActivity extends XBaseActivity implements TabIndicatorView.OnTabIndicatorSelectListener,ViewPager.OnPageChangeListener{
    @BindView(R.id.tab_indicator)
    TabIndicatorView mIndicator;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private HomePagerAdapter mHomeAdapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        mHomeAdapter = new HomePagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mHomeAdapter);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.addOnPageChangeListener(this);
        mIndicator.addOnTabIndicatorSelectListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_index;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onTabSelect(int position) {
        mViewPager.setCurrentItem(position, true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mIndicator.select(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
