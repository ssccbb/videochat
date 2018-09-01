package com.weiliao.kinnek.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiliao.kinnek.R;
import com.weiliao.kinnek.common.Constants;
import com.weiliao.kinnek.model.HotVideoResult;
import com.weiliao.kinnek.utils.ScreenUtils;
import com.weiliao.kinnek.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/6/11.
 */
public class RecommendAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<HotVideoResult> mList = new ArrayList();
    private OnItemClickListener mOnItemClickListener;

    public RecommendAdapter(Context context, List list) {
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.view_vip_video_item, parent, false);
        return new HotVideoHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HotVideoHolder) {
            HotVideoResult hotVideo = getItem(position);
            HotVideoHolder hotVideoHolder = (HotVideoHolder) holder;
            hotVideoHolder.root.setTag(hotVideo);
            hotVideoHolder.root.setOnClickListener(this::onClick);
            hotVideoHolder.cover.setBackground(
                    mContext.getResources().getDrawable(
                            Constants.round_color[(int) (Math.random()*4)]));
            changeItemHeight(hotVideoHolder.root);
            return;
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

    public HotVideoResult getItem(int position) {
        return mList.get(position);
    }


    @Override
    public void onClick(View v) {
        Object object = v.getTag();
        if (object instanceof HotVideoResult && mOnItemClickListener != null) {
            HotVideoResult hotVideo = (HotVideoResult) object;
            mOnItemClickListener.onItemClick(v, hotVideo.position, hotVideo);
        }
    }

    private void changeItemHeight(View root){
        int screenWidth = ScreenUtils.getScreenWidth(mContext);
        int width = (screenWidth - Utils.dip2px(mContext,2)) / 2;
        int height = (int) ((float)width / (float)9 * (float) 16);
        //Log.e(TAG, "changeItemHeight: "+width+"/"+height );
        root.setLayoutParams(new ViewGroup.LayoutParams(width,height));
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
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, HotVideoResult hotVideo);
    }

    public void addOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

}
