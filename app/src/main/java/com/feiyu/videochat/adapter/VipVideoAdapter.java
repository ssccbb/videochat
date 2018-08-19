package com.feiyu.videochat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.Constants;
import com.feiyu.videochat.model.HotVideoResults;
import com.zhouwei.blurlibrary.EasyBlur;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/6/11.
 */
public class VipVideoAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    public static final String TAG = VipVideoAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater mInflater;
    private List<HotVideoResults> mList = new ArrayList();
    private OnItemClickListener mOnItemClickListener;

    private boolean VIP_MODE = false;//false：普通模式 true：VIP模式

    public VipVideoAdapter(Context context, boolean vip_mode, List list) {
        mContext = context;
        VIP_MODE = vip_mode;
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.view_vip_video_item, parent, false);
        return new HotVideoHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HotVideoHolder) {
            HotVideoHolder hotVideoHolder = (HotVideoHolder) holder;
            hotVideoHolder.onBind(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public HotVideoResults getItem(int position) {
        return mList.get(position);
    }

    @Override
    public void onClick(View v) {
        Object object = v.getTag();
        if (object instanceof HotVideoResults && mOnItemClickListener != null) {
            HotVideoResults hotVideo = (HotVideoResults) object;
            Log.e(TAG, "onClick: "+ hotVideo.position);
            if (hotVideo.position == 0 && !VIP_MODE){
                mOnItemClickListener.onVipItemClick();
                return;
            }
            mOnItemClickListener.onItemClick(v, hotVideo.position, hotVideo);
        }
    }

    class HotVideoHolder extends RecyclerView.ViewHolder {
        public View root;
        public ImageView cover;
        public TextView name;
        public View heart;
        public TextView heartNum;

        public HotVideoHolder(View itemView) {
            super(itemView);
            root = itemView;
            cover = root.findViewById(R.id.cover);
            name = root.findViewById(R.id.name);
            heart = root.findViewById(R.id.heart);
            heartNum = root.findViewById(R.id.heart_num);
        }

        void onBind(int position){
            HotVideoResults hotVideo = getItem(position);
            root.setTag(hotVideo);
            root.setOnClickListener(VipVideoAdapter.this::onClick);
            name.setText("小蓝姐");
            name.setTextColor(mContext.getResources().getColor(R.color.app_black));
            cover.setImageResource(0);
            cover.setBackground(
                    mContext.getResources().getDrawable(
                            Constants.round_color[(int) (Math.random()*4)]));
            heartNum.setVisibility(View.VISIBLE);
            heart.setVisibility(View.VISIBLE);

            if (hotVideo.position == 0 && !VIP_MODE) {
                cover.setImageResource(R.mipmap.ic_video_vip);
                name.setText("VIP私密视频专区");
                name.setTextColor(mContext.getResources().getColor(R.color.app_red));
                heartNum.setVisibility(View.GONE);
                heart.setVisibility(View.GONE);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, HotVideoResults hotVideo);
        void onVipItemClick();
    }

    public void addOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

}
