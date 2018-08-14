package com.feiyu.videochat.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.feiyu.videochat.App;
import com.feiyu.videochat.R;
import com.feiyu.videochat.model.HotVideoResults;
import com.feiyu.videochat.views.banner.AutoScrollViewPager;
import com.feiyu.videochat.views.banner.PagerIndicatorView;
import com.feiyu.videochat.views.banner.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/6/11.
 */
public class HomeHotVideoAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    public static final int AUTO_SCROLL_DELAYED_MS = 3000;
    private static final int ITEM_TYPE_VIDEO = 0;
    public static final int ITEM_TYPE_BANNER = 1;

    private Context mContext;
    private LayoutInflater mInflater;
    private List<HotVideoResults> mList = new ArrayList();
    private HomeHotBannerAdapter mBannerAdapter;
    private OnItemClickListener mOnItemClickListener;

    public HomeHotVideoAdapter(Context context, List list) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList.clear();
        if (list != null) {
            mList.addAll(list);
        }
    }

    public void addData(List list, boolean clear) {
        if (clear) {
            mList.clear();
        }
        if (list != null) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void setBannerAdapter(HomeHotBannerAdapter bannerAdapter) {
        this.mBannerAdapter = bannerAdapter;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_BANNER) {
            View view = mInflater.inflate(R.layout.view_banner, parent, false);
            return new BannerHolder(view);
        }

        View view = mInflater.inflate(R.layout.view_hot_item, parent, false);
        view.setOnClickListener(HomeHotVideoAdapter.this);
        return new HotVideoHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HotVideoHolder) {
            HotVideoResults hotVideo = getItem(position);
            HotVideoHolder HotVideoResultsHolder = (HotVideoHolder) holder;

            return;
        }

        if (holder instanceof BannerHolder) {
            BannerHolder bannerHolder = (BannerHolder) holder;
            if (mBannerAdapter != null && mBannerAdapter.getCount() != 0) {
                bannerHolder.mIndicator.setPagerCount(mBannerAdapter.getCount());
                bannerHolder.mIndicator.setVisibility(View.VISIBLE);
                bannerHolder.mAutoPager.setAdapter(mBannerAdapter);
                bannerHolder.mAutoPager.setInterval(AUTO_SCROLL_DELAYED_MS);
                bannerHolder.mAutoPager.setPageTransformer(false, new ScaleTransformer());
                bannerHolder.mAutoPager.setCurrentItem(0);
                bannerHolder.mAutoPager.startAutoScroll(AUTO_SCROLL_DELAYED_MS);
                bannerHolder.mAutoPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        bannerHolder.mIndicator.setSelectPosition(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 &&
                mBannerAdapter != null && mBannerAdapter.getCount() != 0) {
            return ITEM_TYPE_BANNER;
        }
        return ITEM_TYPE_VIDEO;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        if (mBannerAdapter != null && mBannerAdapter.getCount() != 0) {
            return mList.size() + 1;
        }
        return mList.size();
    }

    public HotVideoResults getItem(int position) {
        if (position > 0 && mBannerAdapter != null && mBannerAdapter.getCount() != 0) {
            return mList.get(position - 1);
        }
        return mList.get(position);
    }


    @Override
    public void onClick(View v) {
        Object object = v.getTag();
        if (object instanceof HotVideoResults && mOnItemClickListener != null) {
            HotVideoResults hotVideo = (HotVideoResults) object;
            mOnItemClickListener.onItemClick(v, hotVideo.position, hotVideo);
        }
    }

    class HotVideoHolder extends RecyclerView.ViewHolder {

        public HotVideoHolder(View itemView) {
            super(itemView);
        }
    }

    class BannerHolder extends RecyclerView.ViewHolder {

        public AutoScrollViewPager mAutoPager;
        public PagerIndicatorView mIndicator;

        public BannerHolder(View itemView) {
            super(itemView);
            mAutoPager = itemView.findViewById(R.id.auto_scroll_view_pager);
            mIndicator = itemView.findViewById(R.id.auto_scroll_pager_indicator);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, HotVideoResults hotVideo);
    }

    public void addOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

}
