package com.feiyu.videochat.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.feiyu.videochat.R;
import com.feiyu.videochat.adapter.HostResAdapter;
import com.feiyu.videochat.adapter.SimpleDividerDecoration;
import com.feiyu.videochat.common.XBaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HostInfoActivity extends XBaseActivity implements View.OnClickListener{
    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.connect)
    View mConnect;
    @BindView(R.id.pic_list)
    RecyclerView mPicList;
    @BindView(R.id.video_list)
    RecyclerView mVideoList;

    @Override
    public void initData(Bundle savedInstanceState) {
        mBack.setOnClickListener(this);
        mConnect.setOnClickListener(this);

        initPicList();
        initVideoList();
    }

    private void initPicList(){
        List data = new ArrayList();
        for (int i = 0; i < 6; i++) {
            data.add(i);
        }
        HostResAdapter adapter = new HostResAdapter(data);
        LinearLayoutManager mannager = new LinearLayoutManager(
                this,LinearLayoutManager.HORIZONTAL,false);
        mPicList.setLayoutManager(mannager);
        mPicList.setAdapter(adapter);
        mPicList.setItemAnimator(new DefaultItemAnimator());
        mVideoList.addItemDecoration(new SimpleDividerDecoration(this));
        mPicList.setHasFixedSize(true);
    }

    private void initVideoList(){
        List data = new ArrayList();
        for (int i = 0; i < 4; i++) {
            data.add(i);
        }
        HostResAdapter adapter = new HostResAdapter(data);
        LinearLayoutManager mannager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL,false);
        mVideoList.setLayoutManager(mannager);
        mVideoList.setAdapter(adapter);
        mVideoList.setItemAnimator(new DefaultItemAnimator());
        mVideoList.addItemDecoration(new SimpleDividerDecoration(this));
        mVideoList.setHasFixedSize(true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_host_info;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static void open(Context context){
        context.startActivity(new Intent(context,HostInfoActivity.class));
    }

    @Override
    public void onClick(View v) {
        if (v == mBack){
            this.finish();
        }
        if (v == mConnect){
            ChatActivity.open(this);
        }
    }
}
