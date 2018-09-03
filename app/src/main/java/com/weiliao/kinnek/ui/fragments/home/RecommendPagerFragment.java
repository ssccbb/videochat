package com.weiliao.kinnek.ui.fragments.home;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.weiliao.kinnek.adapter.RecommendAdapter;
import com.weiliao.kinnek.common.XBaseFragment;
import com.weiliao.kinnek.model.HotVideoResult;
import com.weiliao.kinnek.views.StateView;
import com.weiliao.kinnek.views.XReloadableRecyclerContentLayout;
import com.weiliao.kinnek.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xrecyclerview.XRecyclerView;
import cn.droidlover.xstatecontroller.XStateController;

public class RecommendPagerFragment extends XBaseFragment implements XRecyclerView.OnRefreshAndLoadMoreListener,RecommendAdapter.OnItemClickListener {
    public static RecommendPagerFragment instance;
    private RecommendAdapter mVideoAdapter;

    @BindView(R.id.list)
    XReloadableRecyclerContentLayout mList;
    @BindView(R.id.steta)
    StateView stateView;

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
        stateView.setMsg("敬请期待...");
        //recyclerSet();
    }

    private void recyclerSet(){
        if (mVideoAdapter != null) return;
        mList.showLoading();
        mVideoAdapter = new RecommendAdapter(getActivity(),null);
        mList.getRecyclerView().setAdapter(mVideoAdapter);
        mList.getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        mList.getRecyclerView().setLayoutManager(new GridLayoutManager(getActivity(),2));
        List<HotVideoResult> data = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            data.add(new HotVideoResult(i));
        }
        mVideoAdapter.addData(data,true);
        mList.getRecyclerView().setOnRefreshAndLoadMoreListener(this);
        mList.getRecyclerView().useDefLoadMoreView();
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
    public void onItemClick(View view, int position, HotVideoResult hotVideo) {
    }
}
