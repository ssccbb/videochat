package com.weiliao.kinnek.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.weiliao.kinnek.model.UGCVideoResult;
import com.weiliao.kinnek.ui.fragments.VideoBrowsePlayerFragment;

import java.util.List;

/**
 * Created by 神荼 on 2017/12/21.
 */

public class ShortCoverAdapter extends SmartFragmentStatePagerAdapter{

    private List<UGCVideoResult> shortVideoList;

    public ShortCoverAdapter(FragmentManager fm, List<UGCVideoResult> videoList) {
        super(fm);
        shortVideoList = videoList;
    }

    @Override
    public Fragment getItem(int position) {
        UGCVideoResult videoResult = shortVideoList.get(position);
        return VideoBrowsePlayerFragment.newInstance(videoResult);
    }

    @Override
    public int getCount() {
        return shortVideoList.size();
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
