package com.feiyu.videochat.ui.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.feiyu.videochat.App;
import com.feiyu.videochat.R;
import com.feiyu.videochat.adapter.HomeHotBannerAdapter;
import com.feiyu.videochat.adapter.HomeHotVideoAdapter;
import com.feiyu.videochat.common.XBaseFragment;
import com.feiyu.videochat.model.HotVideoResults;
import com.feiyu.videochat.model.PhoneVertifyResultModel;
import com.feiyu.videochat.model.basemodel.HttpResultModel;
import com.feiyu.videochat.net.DataService;
import com.feiyu.videochat.net.body.PhoneVertifyRequestBody;
import com.feiyu.videochat.ui.activitys.HostInfoActivity;
import com.feiyu.videochat.utils.RxLoadingUtils;
import com.feiyu.videochat.views.XReloadableRecyclerContentLayout;
import com.feiyu.videochat.views.banner.AutoScrollViewPager;
import com.feiyu.videochat.views.banner.PagerIndicatorView;
import com.feiyu.videochat.views.banner.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class HotPagerFragment extends XBaseFragment implements XRecyclerView.OnRefreshAndLoadMoreListener,HomeHotVideoAdapter.OnItemClickListener{
    @BindView(R.id.list)
    XReloadableRecyclerContentLayout mList;

    private static final String TAG = HotPagerFragment.class.getSimpleName();
    public static HotPagerFragment instance;
    private HomeHotVideoAdapter mHotAdapter;

    public static HotPagerFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new HotPagerFragment();
    }

    public HotPagerFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mList.showLoading();
        List<HotVideoResults> data = new ArrayList();
        for (int i = 0; i < 20; i++) {
            data.add(new HotVideoResults(i));
        }
        List bannerData = new ArrayList();
        bannerData.add(0);
        bannerData.add(1);
        bannerData.add(2);
        bannerData.add(3);
        HomeHotBannerAdapter adapter = new HomeHotBannerAdapter(App.getContext(),bannerData);
        mHotAdapter = new HomeHotVideoAdapter(getActivity(),null);
        mHotAdapter.setBannerAdapter(adapter);
        mList.getRecyclerView().setAdapter(mHotAdapter);
        mList.getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        mList.getRecyclerView().setLayoutManager(manager);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? manager.getSpanCount() : 1;
            }
        });
        mHotAdapter.addData(data,true);
        mList.getRecyclerView().setOnRefreshAndLoadMoreListener(this);
        mList.getLoadingView().setVisibility(View.GONE);
        mHotAdapter.addOnItemClickListener(this);

        //getPhoneVertify();
    }

    private void getPhoneVertify() {
        Flowable<HttpResultModel<PhoneVertifyResultModel>> fr = DataService.getPhoneVertify(new PhoneVertifyRequestBody("18610488283"));
        RxLoadingUtils.subscribeWithReload(mList, fr, bindToLifecycle(), new Consumer<HttpResultModel<PhoneVertifyResultModel>>() {
            @Override
            public void accept(HttpResultModel<PhoneVertifyResultModel> gameAccountResultModelHttpResultModel) throws Exception {
//                notifyData(gameAccountResultModelHttpResultModel.data, page);
//                mContent.getRecyclerView().setPage(gameAccountResultModelHttpResultModel.current_page, gameAccountResultModelHttpResultModel.total_page);
                Log.e(TAG, "accept: ok!" );
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "accept: "+netError.getMessage() );
            }
        }, null, true);
    }

    @Override
    public void onItemClick(View view, int position, HotVideoResults hotVideo) {
        startActivity(new Intent(getActivity(), HostInfoActivity.class));
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
        return R.layout.fragment_home_child_hot;
    }

    @Override
    public Object newP() {
        return null;
    }
}
