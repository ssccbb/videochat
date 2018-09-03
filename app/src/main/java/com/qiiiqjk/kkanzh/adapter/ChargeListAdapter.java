package com.qiiiqjk.kkanzh.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qiiiqjk.kkanzh.model.PayChargeItemResult;
import com.qiiiqjk.kkanzh.R;

import java.util.ArrayList;
import java.util.List;

public class ChargeListAdapter extends RecyclerView.Adapter {
    public static final String TAG = ChargeListAdapter.class.getSimpleName();
    private OnChargeItemClickListener onChargeItemClickListener;
    private Context context;
    private List data = new ArrayList();

    public ChargeListAdapter(Context context, List data) {
        this.context = context;
        if (data != null){
            this.data.addAll(data);
        }
    }

    public void addData(List data){
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ChargeItemHolder(inflater.inflate(R.layout.view_charge_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ChargeItemHolder){
            ChargeItemHolder chargeItemHolder = (ChargeItemHolder) holder;
            chargeItemHolder.onbind(position);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ChargeItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public View root;
        public TextView price;
        public TextView tittle;

        public ChargeItemHolder(View itemView) {
            super(itemView);
            root = itemView;
            price = root.findViewById(R.id.price);
            tittle = root.findViewById(R.id.tittle);
        }

        void onbind(int position){
            root.setOnClickListener(this);
            PayChargeItemResult.PayChargeItem payChargeItem = (PayChargeItemResult.PayChargeItem) data.get(position);
            float p = Float.parseFloat(payChargeItem.price) / (float)100;
            price.setText("¥\t"+p);
            tittle.setText(payChargeItem.diamond);
            root.setTag((int)p);
        }

        @Override
        public void onClick(View v) {
            if (v == root && root.getTag() != null){
                int price = (int) root.getTag();
                onChargeItemClickListener.onChargeItemClick(price);
            }
        }
    }

    public interface OnChargeItemClickListener{
        void onChargeItemClick(int price);
    }

    public void addOnChargeItemClickListener(OnChargeItemClickListener onChargeItemClickListener){
        this.onChargeItemClickListener = onChargeItemClickListener;
    }
}