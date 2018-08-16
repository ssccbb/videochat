package com.feiyu.videochat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.Constants;
import com.feiyu.videochat.model.HotVideoResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/6/11.
 */
public class VipVideoAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<HotVideoResults> mList = new ArrayList();
    private OnItemClickListener mOnItemClickListener;

    public VipVideoAdapter(Context context, List list) {
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
        view.setOnClickListener(VipVideoAdapter.this);
        return new HotVideoHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HotVideoHolder) {
            HotVideoResults hotVideo = getItem(position);
            HotVideoHolder hotVideoHolder = (HotVideoHolder) holder;
            hotVideoHolder.cover.setBackground(
                    mContext.getResources().getDrawable(
                            Constants.round_color[(int) (Math.random()*4)]));
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

    public HotVideoResults getItem(int position) {
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
        public View root;
        public ImageView cover;
        public TextView name;

        public HotVideoHolder(View itemView) {
            super(itemView);
            root = itemView;
            cover = root.findViewById(R.id.cover);
            name = root.findViewById(R.id.name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, HotVideoResults hotVideo);
    }

    public void addOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

}
