package com.feiyu.videochat.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.feiyu.videochat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sung on 2018/5/3.
 */

public class HomeHotBannerAdapter extends PagerAdapter {
    private static String TAG = HomeHotBannerAdapter.class.getSimpleName();
    private List mData = new ArrayList();
    private List<View> mViews = new ArrayList<>();
    private Context mContext;

    public HomeHotBannerAdapter(Context mContext, List mData) {
        this.mData = mData;
        this.mContext = mContext;
        if (mData != null && !mData.isEmpty()){
            initView();
        }
    }

    private void initView(){
        if (mContext == null) return;

        mViews.clear();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        for (int i = 0; i < mData.size(); i++) {
            View item = inflater.inflate(R.layout.view_banner_item,null,false);
            ImageView img = item.findViewById(R.id.banner_img);
            img.setImageResource(R.mipmap.ic_loading_fail);
            mViews.add(item);
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mViews.size() > position) {
            container.removeView(mViews.get(position));
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
