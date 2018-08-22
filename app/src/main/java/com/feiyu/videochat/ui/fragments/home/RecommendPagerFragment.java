package com.feiyu.videochat.ui.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.feiyu.videochat.R;
import com.feiyu.videochat.adapter.RecommendAdapter;
import com.feiyu.videochat.adapter.VipVideoAdapter;
import com.feiyu.videochat.common.XBaseFragment;
import com.feiyu.videochat.model.HotVideoResults;
import com.feiyu.videochat.ui.activitys.VideoBrowseActivity;
import com.feiyu.videochat.views.XReloadableRecyclerContentLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xrecyclerview.XRecyclerView;
import cn.droidlover.xstatecontroller.XStateController;

public class RecommendPagerFragment extends XBaseFragment implements XRecyclerView.OnRefreshAndLoadMoreListener,RecommendAdapter.OnItemClickListener{
    public static RecommendPagerFragment instance;
    private RecommendAdapter mVideoAdapter;

    @BindView(R.id.list)
    XReloadableRecyclerContentLayout mList;

    public static RecommendPagerFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new RecommendPagerFragment();
    }

    public RecommendPagerFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        recyclerSet();
    }

    private void recyclerSet(){
        if (mVideoAdapter != null) return;
        mList.showLoading();
        mVideoAdapter = new RecommendAdapter(getActivity(),null);
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
    public void onRefresh() {
        mList.refreshState(false);
    }

    @Override
    public void onLoadMore(int page) {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_child_recommend;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onItemClick(View view, int position, HotVideoResults hotVideo) {
        startActivity(new Intent(getActivity(), VideoBrowseActivity.class));
    }
}
