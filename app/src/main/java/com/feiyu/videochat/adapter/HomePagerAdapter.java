package com.feiyu.videochat.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.feiyu.videochat.common.XBaseFragment;
import com.feiyu.videochat.ui.fragments.FindPagerFragment;
import com.feiyu.videochat.ui.fragments.GroupPagerFragment;
import com.feiyu.videochat.ui.fragments.HomePagerFragment;
import com.feiyu.videochat.ui.fragments.MsgPagerFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sung on 2018/4/24.
 */

public class HomePagerAdapter extends FragmentPagerAdapter {
    private FragmentManager manager;
    private List<XBaseFragment> fragments = new ArrayList();

    private Context context;

    public HomePagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        initFragments();
    }

    private void initFragments(){
        if (fragments == null) fragments = new ArrayList();
        HomePagerFragment fragment1 = HomePagerFragment.newInstance();
        GroupPagerFragment fragment2 = GroupPagerFragment.newInstance();
        FindPagerFragment fragment3 = FindPagerFragment.newInstance();
        MsgPagerFragment fragment4 = MsgPagerFragment.newInstance();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);
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
