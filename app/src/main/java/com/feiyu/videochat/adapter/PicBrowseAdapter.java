package com.feiyu.videochat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.feiyu.videochat.ui.fragments.SubSampleImagesViewFragment;

import java.util.ArrayList;
import java.util.List;

public class PicBrowseAdapter extends FragmentStatePagerAdapter {

    private List<String> mList = new ArrayList<>();

    public PicBrowseAdapter(FragmentManager fm, ArrayList<String> list) {
        super(fm);
        mList = list;
    }

    public void addData(List<String> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        SubSampleImagesViewFragment fragment = SubSampleImagesViewFragment.getInstance(mList.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
