package com.weiliao.kinnek.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weiliao.kinnek.App;
import com.weiliao.kinnek.common.Constants;
import com.weiliao.kinnek.utils.Utils;
import com.weiliao.kinnek.R;
import com.weiliao.kinnek.model.HotVideoResult;
import com.weiliao.kinnek.utils.ScreenUtils;
import com.weiliao.kinnek.utils.StringUtils;
import com.weiliao.kinnek.views.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;

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
            if(mList.get(0).type != 0 && !VIP_MODE && !Utils.isHideMode()){
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
            if (hotVideo.type == 0 && !VIP_MODE){
                mOnItemClickListener.onVipItemClick();
                return;
            }
            mOnItemClickListener.onItemClick(v, hotVideo.type, hotVideo);
        }
    }

    class HotVideoHolder extends RecyclerView.ViewHolder {
        public View root;
        public CircleImageView avatar;
        public ImageView cover;
        public TextView name;
        public TextView tittle;
        public View heart;
        public TextView heartNum;

        public HotVideoHolder(View itemView) {
            super(itemView);
            root = itemView;
            cover = root.findViewById(R.id.cover);
            avatar = root.findViewById(R.id.avatar);
            name = root.findViewById(R.id.name);
            tittle = root.findViewById(R.id.tittle);
            heart = root.findViewById(R.id.heart);
            heartNum = root.findViewById(R.id.heart_num);
        }

        void onBind(int position){
            changeItemHeight();
            HotVideoResult hotVideo = getItem(position);
            root.setTag(hotVideo);
            root.setOnClickListener(VipVideoAdapter.this::onClick);

            if (hotVideo.type == 0 && !VIP_MODE && !Utils.isHideMode()) {
                cover.setImageResource(R.mipmap.ic_video_vip);
                name.setVisibility(View.GONE);
                avatar.setVisibility(View.GONE);
                tittle.setVisibility(View.GONE);
                heartNum.setVisibility(View.GONE);
                heart.setVisibility(View.GONE);
                return;
            }

            name.setText(StringUtils.isEmpty(hotVideo.anchor_name) ? Constants.default_user_name : hotVideo.anchor_name);
            tittle.setText(hotVideo.title);
            heartNum.setText(hotVideo.pick_num);
            Glide.with(App.getContext()).load(hotVideo.cover_url)
                    .crossFade().centerCrop().thumbnail(0.1f).into(cover);
            Glide.with(App.getContext()).load(hotVideo.avatar)
                    .crossFade().centerCrop().thumbnail(0.1f).into(avatar);
            heartNum.setVisibility(View.VISIBLE);
            heart.setVisibility(View.VISIBLE);
            avatar.setVisibility(View.VISIBLE);
            tittle.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);
        }

        void changeItemHeight(){
            int screenWidth = ScreenUtils.getScreenWidth(mContext);
            int width = (screenWidth - Utils.dip2px(mContext,2)) / 2;
            int height = (int) ((float)width / (float)9 * (float) 16);
            //Log.e(TAG, "changeItemHeight: "+width+"/"+height );
            root.setLayoutParams(new ViewGroup.LayoutParams(width,height));
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
