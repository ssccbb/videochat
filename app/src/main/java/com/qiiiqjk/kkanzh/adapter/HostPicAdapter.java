package com.qiiiqjk.kkanzh.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qiiiqjk.kkanzh.App;
import com.qiiiqjk.kkanzh.common.Constants;
import com.qiiiqjk.kkanzh.utils.StringUtils;
import com.qiiiqjk.kkanzh.R;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class HostPicAdapter extends RecyclerView.Adapter {
    public static final String TAG = HostPicAdapter.class.getSimpleName();
    private onHostPicItemClickListener onHostPicItemClickListener;
    private List data = new ArrayList();
    public int pay_status = -1;

    public HostPicAdapter(List data) {
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

    public List getData() {
        return data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_host_pic,null,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ListHolder)holder).onBind(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View root;
        public ImageView cover;
        public ImageView lock;

        public ListHolder(View itemView) {
            super(itemView);
            root = itemView;
            cover = itemView.findViewById(R.id.cover);
            lock = itemView.findViewById(R.id.lock);
        }

        void onBind(int position){
            Log.e(TAG, "onBind: "+ StringUtils.convertUrlStr(data.get(position).toString()) );
            if (pay_status != 1) {
                Glide.with(App.getContext()).load(StringUtils.convertUrlStr(data.get(position).toString()))
                        .bitmapTransform(new BlurTransformation(App.getContext(), Constants.BLUR_RADIUS, Constants.BLUR_SAMPLING))
                        .crossFade()/*.thumbnail(0.1f)*/.into(cover);
            }else {
                Glide.with(App.getContext()).load(StringUtils.convertUrlStr(data.get(position).toString()))
                        .crossFade()/*.thumbnail(0.1f)*/.into(cover);
            }
            lock.setVisibility(pay_status == 1 ? View.GONE : View.VISIBLE);
            root.setTag(data.get(position).toString());
            root.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == root){
                String url = (String) root.getTag();
                if (onHostPicItemClickListener != null){
                    onHostPicItemClickListener.onHostPicItemClick(url);
                }
            }
        }
    }

    public interface onHostPicItemClickListener{
        void onHostPicItemClick(String url);
    }

    public void addOnHostPicItemClickListener(onHostPicItemClickListener onHostPicItemClickListener){
        this.onHostPicItemClickListener = onHostPicItemClickListener;
    }
}
