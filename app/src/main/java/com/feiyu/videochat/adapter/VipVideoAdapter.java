package com.feiyu.videochat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feiyu.videochat.App;
import com.feiyu.videochat.R;
import com.feiyu.videochat.common.Constants;
import com.feiyu.videochat.model.HotVideoResult;
import com.feiyu.videochat.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/6/11.
 * 用于首页视频列表和VIP视频专区列表
 */
public class VipVideoAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    public static final String TAG = VipVideoAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater mInflater;
    private List<HotVideoResult> mList = new ArrayList();
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
        if (!mList.isEmpty()){
            if(mList.get(0).type != 0 && !VIP_MODE){
                mList.add(0,new HotVideoResult(0));
            }
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

    public List<HotVideoResult> getData() {
        return mList;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public HotVideoResult getItem(int position) {
        return mList.get(position);
    }

    @Override
    public void onClick(View v) {
        Object object = v.getTag();
        if (object instanceof HotVideoResult && mOnItemClickListener != null) {
            HotVideoResult hotVideo = (HotVideoResult) object;
            Log.e(TAG, "onClick: "+ hotVideo.position);
            if (hotVideo.type == 0 && !VIP_MODE){
                mOnItemClickListener.onVipItemClick();
                return;
            }
            mOnItemClickListener.onItemClick(v, hotVideo.type, hotVideo);
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
            HotVideoResult hotVideo = getItem(position);
            root.setTag(hotVideo);
            root.setOnClickListener(VipVideoAdapter.this::onClick);

            if (hotVideo.type == 0 && !VIP_MODE) {
                cover.setImageResource(R.mipmap.ic_video_vip);
                name.setText("VIP私密视频专区");
                name.setTextColor(mContext.getResources().getColor(R.color.app_red));
                heartNum.setVisibility(View.GONE);
                heart.setVisibility(View.GONE);
                return;
            }

            name.setText(hotVideo.title);
            name.setTextColor(mContext.getResources().getColor(R.color.app_black));
            Glide.with(App.getContext()).load(StringUtils.convertUrlStr(hotVideo.cover_url)).crossFade()/*.thumbnail(0.1f)*/.centerCrop().into(cover);
            cover.setBackground(
                    mContext.getResources().getDrawable(
                            Constants.round_color[(int) (Math.random()*4)]));
            heartNum.setVisibility(View.VISIBLE);
            heartNum.setText(hotVideo.total_viewer);
            heart.setVisibility(View.VISIBLE);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int type, HotVideoResult hotVideo);
        void onVipItemClick();
    }

    public void addOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

}
