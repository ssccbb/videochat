package com.feiyu.videochat.ui.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.feiyu.videochat.App;
import com.feiyu.videochat.R;
import com.feiyu.videochat.adapter.VipVideoAdapter;
import com.feiyu.videochat.common.XBaseActivity;
import com.feiyu.videochat.model.HotVideoResults;
import com.feiyu.videochat.views.XReloadableRecyclerContentLayout;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import cn.droidlover.xrecyclerview.XRecyclerView;
import cn.droidlover.xstatecontroller.XStateController;

public class VipVideoActivity extends XBaseActivity implements XRecyclerView.OnRefreshAndLoadMoreListener, View.OnClickListener,VipVideoAdapter.OnItemClickListener{
    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.tittle)
    TextView mTittle;
    @BindView(R.id.list)
    XReloadableRecyclerContentLayout mList;

    private VipVideoAdapter mVideoAdapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        mTittle.setText("VIP视频专区");
        mBack.setOnClickListener(this);
        recyclerSet();
    }

    private void recyclerSet(){
        if (mVideoAdapter != null) return;
        mList.showLoading();
        mVideoAdapter = new VipVideoAdapter(this,true,null);
        mVideoAdapter.addOnItemClickListener(this);
        mList.getRecyclerView().setAdapter(mVideoAdapter);
        mList.getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        mList.getRecyclerView().setLayoutManager(new GridLayoutManager(this,2));
        List<HotVideoResults> data = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            data.add(new HotVideoResults(i));
        }
        mVideoAdapter.addData(data,true);
        mList.getRecyclerView().setOnRefreshAndLoadMoreListener(this);
        mList.getLoadingView().setVisibility(View.GONE);
        mList.setDisplayState(XStateController.STATE_CONTENT);
    }

    @Override
    public void onRefresh() {
        mList.refreshState(false);
    }

    @Override
    public void onLoadMore(int page) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vip_video;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static void open(Context context){
        context.startActivity(new Intent(context,VipVideoActivity.class));
    }

    @Override
    public void onClick(View v) {
        if (v == mBack){
            this.finish();
        }
    }

    @Override
    public void onItemClick(View view, int position, HotVideoResults hotVideo) {
        VideoBrowseActivity.open(this);
    }

    @Override
    public void onVipItemClick() {//无用
    }
}
