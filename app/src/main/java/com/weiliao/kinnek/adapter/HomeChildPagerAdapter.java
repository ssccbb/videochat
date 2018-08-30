package com.weiliao.kinnek.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.weiliao.kinnek.common.XBaseFragment;
import com.weiliao.kinnek.ui.fragments.HomePagerFragment;
import com.weiliao.kinnek.ui.fragments.MinePagerFragment;
import com.weiliao.kinnek.ui.fragments.MsgPagerFragment;
import com.weiliao.kinnek.ui.fragments.VideoPagerFragment;
import com.weiliao.kinnek.ui.fragments.home.FollowPagerFragment;
import com.weiliao.kinnek.ui.fragments.home.HotPagerFragment;
import com.weiliao.kinnek.ui.fragments.home.RecommendPagerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sung on 2018/4/24.
 */

public class HomeChildPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager manager;
    private List<XBaseFragment> fragments = new ArrayList();

    private Context context;

    public HomeChildPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        initFragments();
    }

    private void initFragments(){
        if (fragments == null) fragments = new ArrayList();
        FollowPagerFragment fragment1 = FollowPagerFragment.newInstance();
        HotPagerFragment fragment2 = HotPagerFragment.newInstance();
        RecommendPagerFragment fragment3 = RecommendPagerFragment.newInstance();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
    }

    @Override
    public XBaseFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
