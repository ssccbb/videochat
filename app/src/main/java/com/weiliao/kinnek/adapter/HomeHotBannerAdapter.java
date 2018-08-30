package com.weiliao.kinnek.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.weiliao.kinnek.R;
import com.weiliao.kinnek.model.BannerResults;
import com.weiliao.kinnek.ui.activitys.WebBrowseActivity;
import com.weiliao.kinnek.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sung on 2018/5/3.
 */

public class HomeHotBannerAdapter extends PagerAdapter {
    private static String TAG = HomeHotBannerAdapter.class.getSimpleName();
    private List<BannerResults.Banner> mData = new ArrayList();
    private List<View> mViews = new ArrayList<>();
    private Context mContext;

    public HomeHotBannerAdapter(Context mContext, List<BannerResults.Banner> data) {
        this.mData.clear();
        this.mContext = mContext;
        if (data != null) this.mData.addAll(data);
        if (this.mData.isEmpty()) this.mData.add(new BannerResults.Banner());
        initView();
    }

    public void setData(List<BannerResults.Banner> data){
        this.mData.clear();
        if (data != null) mData.addAll(data);
        if (mData.isEmpty()) mData.add(new BannerResults.Banner());
        initView();
        notifyDataSetChanged();
    }

    private void initView(){
        if (mContext == null) return;

        mViews.clear();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        for (int i = 0; i < mData.size(); i++) {
            View item = inflater.inflate(R.layout.view_banner_item,null,false);
            ImageView img = item.findViewById(R.id.banner_img);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            BannerResults.Banner banner = (BannerResults.Banner) mData.get(i);
            if (!StringUtils.isEmpty(banner.cover_url)) {
                Glide.with(mContext).load(StringUtils.convertUrlStr(banner.cover_url))
                        .crossFade()/*.thumbnail(0.1f)*/.into(img);
            }
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(mContext, banner.link_url+"", Toast.LENGTH_SHORT).show();
                    if (banner == null || StringUtils.isEmpty(banner.link_url)) return;
                    WebBrowseActivity.open(mContext,mContext.getResources().getString(R.string.app_name),banner.link_url);
                }
            });
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
