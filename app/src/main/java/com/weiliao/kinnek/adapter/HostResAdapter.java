package com.weiliao.kinnek.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.weiliao.kinnek.App;
import com.weiliao.kinnek.common.Constants;
import com.weiliao.kinnek.R;
import com.weiliao.kinnek.model.AnchorInfoResult;
import com.weiliao.kinnek.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class HostResAdapter extends RecyclerView.Adapter {
    public static final String TAG = HostResAdapter.class.getSimpleName();
    private List data = new ArrayList();

    public HostResAdapter(List data) {
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_host_res_item,null,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ListHolder)holder).onBind(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ListHolder extends RecyclerView.ViewHolder {
        public ImageView img;

        public ListHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }

        void onBind(int position){
            AnchorInfoResult.VideoListBean bean = (AnchorInfoResult.VideoListBean) data.get(position);
            Glide.with(App.getContext()).load(StringUtils.convertUrlStr(bean.cover_url))
                    .bitmapTransform(new BlurTransformation(App.getContext(),
                            Constants.BLUR_RADIUS,Constants.BLUR_SAMPLING))
                    .crossFade()/*.thumbnail(0.1f)*/.into(img);
        }
    }
}
