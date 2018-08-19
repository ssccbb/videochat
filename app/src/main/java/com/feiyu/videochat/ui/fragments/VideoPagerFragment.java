package com.feiyu.videochat.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.feiyu.videochat.R;
import com.feiyu.videochat.adapter.VipVideoAdapter;
import com.feiyu.videochat.common.XBaseFragment;
import com.feiyu.videochat.model.HotVideoResults;
import com.feiyu.videochat.ui.activitys.VipVideoActivity;
import com.feiyu.videochat.views.XReloadableRecyclerContentLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xrecyclerview.XRecyclerView;
import cn.droidlover.xstatecontroller.XStateController;

public class VideoPagerFragment extends XBaseFragment implements XRecyclerView.OnRefreshAndLoadMoreListener,
        VipVideoAdapter.OnItemClickListener{
    public static final String TAG = VideoPagerFragment.class.getSimpleName();
    public static VideoPagerFragment instance;
    private VipVideoAdapter mVideoAdapter;

    @BindView(R.id.text)
    TextView mHeadText;
    @BindView(R.id.list)
    XReloadableRecyclerContentLayout mList;

    public static VideoPagerFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new VideoPagerFragment();
    }

    public VideoPagerFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mHeadText.setText("视频专区");
        recyclerSet();
    }

    private void recyclerSet(){
        if (mVideoAdapter != null) return;
        mList.showLoading();
        mVideoAdapter = new VipVideoAdapter(getActivity(),false,null);
        mVideoAdapter.addOnItemClickListener(this);
        mList.getRecyclerView().setAdapter(mVideoAdapter);
        mList.getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        mList.getRecyclerView().setLayoutManager(new GridLayoutManager(getActivity(),2));
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
    public void onItemClick(View view, int position, HotVideoResults hotVideo) {
    }

    @Override
    public void onVipItemClick() {
        startActivity(new Intent(getActivity(), VipVideoActivity.class));
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
        return R.layout.fragment_video;
    }

    @Override
    public Object newP() {
        return null;
    }
}
