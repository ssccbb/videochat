package com.weiliao.kinnek.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weiliao.kinnek.App;
import com.weiliao.kinnek.utils.ScreenUtils;
import com.weiliao.kinnek.utils.Utils;
import com.weiliao.kinnek.R;
import com.weiliao.kinnek.model.HotHostResults;
import com.weiliao.kinnek.utils.StringUtils;
import com.weiliao.kinnek.views.banner.AutoScrollViewPager;
import com.weiliao.kinnek.views.banner.PagerIndicatorView;
import com.weiliao.kinnek.views.banner.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/6/11.
 * 用于首页热门host
 */
public class HomeHotHostAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    public static final String TAG = HomeHotHostAdapter.class.getSimpleName();
    public static final int AUTO_SCROLL_DELAYED_MS = 3000;
    private static final int ITEM_TYPE_VIDEO = 0;
    public static final int ITEM_TYPE_BANNER = 1;

    private Context mContext;
    private LayoutInflater mInflater;
    private List<HotHostResults.HotHostResult> mList = new ArrayList();
    private HomeHotBannerAdapter mBannerAdapter;
    private OnItemClickListener mOnItemClickListener;

    public HomeHotHostAdapter(Context context, List list) {
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

        View view = mInflater.inflate(R.layout.view_hot_video_item, parent, false);
        return new HotHostHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HotHostHolder) {
            HotHostResults.HotHostResult hotHost = getItem(position);
            HotHostHolder hotHostHolder = (HotHostHolder) holder;

            hotHostHolder.root.setTag(hotHost);
            hotHostHolder.root.setOnClickListener(this);
            hotHostHolder.name.setText(hotHost.nickname);
            try {
                hotHostHolder.status.setImageResource(Utils.getHostStatus(Integer.parseInt(hotHost.anchor_state)));
            }catch (NumberFormatException e){
                Log.e(TAG, "onBindViewHolder: "+e.toString() );
                hotHostHolder.status.setImageResource(Utils.getHostStatus(-1));
            }
            Glide.with(App.getContext()).load(StringUtils.convertUrlStr(hotHost.avatar))
                    .crossFade()/*.thumbnail(0.1f)*/.centerCrop().into(hotHostHolder.cover);
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

    public HotHostResults.HotHostResult getItem(int position) {
        if (position > 0 && mBannerAdapter != null && mBannerAdapter.getCount() != 0) {
            return mList.get(position - 1);
        }
        return mList.get(position);
    }


    @Override
    public void onClick(View v) {
        Object object = v.getTag();
        if (object instanceof HotHostResults.HotHostResult && mOnItemClickListener != null) {
            HotHostResults.HotHostResult hotHost = (HotHostResults.HotHostResult) object;
            mOnItemClickListener.onItemClick(v, hotHost.position, hotHost);
        }
    }

    class HotHostHolder extends RecyclerView.ViewHolder {
        public View root;
        public ImageView cover;
        public ImageView status;
        public ImageView level;
        public TextView name;

        public HotHostHolder(View itemView) {
            super(itemView);
            root = itemView;
            cover = root.findViewById(R.id.cover);
            status = root.findViewById(R.id.status);
            level = root.findViewById(R.id.level);
            name = root.findViewById(R.id.name);
        }
    }

    class BannerHolder extends RecyclerView.ViewHolder {

        public AutoScrollViewPager mAutoPager;
        public PagerIndicatorView mIndicator;

        public BannerHolder(View itemView) {
            super(itemView);
            mAutoPager = itemView.findViewById(R.id.auto_scroll_view_pager);
            mIndicator = itemView.findViewById(R.id.auto_scroll_pager_indicator);

            float screenWidth = ScreenUtils.getScreenWidth(mContext);
            float height = screenWidth / (float) 3;
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int) height));
            mAutoPager.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int) height));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, HotHostResults.HotHostResult hotHost);
    }

    public void addOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

}
