package com.feiyu.videochat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.feiyu.videochat.R;
import java.util.ArrayList;
import java.util.List;

public class HostResAdapter extends RecyclerView.Adapter {
    private List data = new ArrayList();

    public HostResAdapter(List data) {
        if (data != null){
            this.data = data;
        }
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

        public ListHolder(View itemView) {
            super(itemView);
        }

        void onBind(int position){
        }
    }
}
