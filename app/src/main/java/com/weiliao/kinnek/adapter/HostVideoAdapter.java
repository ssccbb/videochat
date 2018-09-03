package com.weiliao.kinnek.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.weiliao.kinnek.App;
import com.weiliao.kinnek.common.Constants;
import com.weiliao.kinnek.model.AnchorInfoResult;
import com.weiliao.kinnek.utils.StringUtils;
import com.weiliao.kinnek.R;

import java.util.ArrayList;
import java.util.List;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class HostVideoAdapter extends RecyclerView.Adapter {
    public static final String TAG = HostVideoAdapter.class.getSimpleName();
    private List data = new ArrayList();
    public int pay_status = -1;
    private onHostVideoItemClickListener onHostVideoItemClickListener;

    public HostVideoAdapter(List data) {
        if (data != null){
            this.data = data;
        }
    }

    public void addData(List list, boolean clear) {
        if (list == null) return;
        if (clear) {
            data.clear();
        }
        data.addAll(list);
        notifyDataSetChanged();
    }

    public void setPayStatus(int payStatus){
        this.pay_status = payStatus;
        if (payStatus != 1) return;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_host_video,null,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ListHolder)holder).onBind(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public List getData() {
        return data;
    }

    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public View root;
        public ImageView cover;
        public TextView num;
        public TextView tittle;
        public ImageView lock;

        public ListHolder(View itemView) {
            super(itemView);
            root = itemView;
            cover = itemView.findViewById(R.id.cover);
            num = itemView.findViewById(R.id.num);
            tittle = itemView.findViewById(R.id.tittle);
            lock = itemView.findViewById(R.id.lock);
        }

        void onBind(int position){
            AnchorInfoResult.VideoListBean videoListBean = (AnchorInfoResult.VideoListBean) data.get(position);
            Log.e(TAG, "onBind: "+ StringUtils.convertUrlStr(videoListBean.cover_url.toString()) );
            if (pay_status != 1) {
                Glide.with(App.getContext()).load(StringUtils.convertUrlStr(videoListBean.cover_url.toString()))
                        .bitmapTransform(new BlurTransformation(App.getContext(), Constants.BLUR_RADIUS, Constants.BLUR_SAMPLING))
                        .crossFade()/*.thumbnail(0.1f)*/.into(cover);
            }else {
                Glide.with(App.getContext()).load(StringUtils.convertUrlStr(videoListBean.cover_url.toString()))
                        .crossFade()/*.thumbnail(0.1f)*/.into(cover);
            }
            num.setText(videoListBean.total_viewer);
            tittle.setText(videoListBean.title);

            root.setTag(videoListBean);
            root.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == root){
                AnchorInfoResult.VideoListBean bean = (AnchorInfoResult.VideoListBean) v.getTag();
                if (onHostVideoItemClickListener != null){
                    onHostVideoItemClickListener.onHostVideoItemClick(bean);
                }
            }
        }
    }

    public interface onHostVideoItemClickListener{
        void onHostVideoItemClick(AnchorInfoResult.VideoListBean bean);
    }

    public void addOnHostVideoItemClickListener(onHostVideoItemClickListener onHostVideoItemClickListener){
        this.onHostVideoItemClickListener = onHostVideoItemClickListener;
    }
}
