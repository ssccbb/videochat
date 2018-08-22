package com.feiyu.videochat.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.feiyu.videochat.R;
import com.feiyu.videochat.adapter.ShortCoverAdapter;
import com.feiyu.videochat.adapter.VipVideoAdapter;
import com.feiyu.videochat.common.Constants;
import com.feiyu.videochat.common.XBaseFragment;
import com.feiyu.videochat.model.HotVideoResults;
import com.feiyu.videochat.model.UGCVideoResult;
import com.feiyu.videochat.ui.activitys.VideoBrowseActivity;
import com.feiyu.videochat.ui.activitys.VipVideoActivity;
import com.feiyu.videochat.views.VerticalScrollViewPager;
import com.feiyu.videochat.views.XReloadableRecyclerContentLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xrecyclerview.XRecyclerView;
import cn.droidlover.xstatecontroller.XStateController;

/**
 * 视频播放上层容器fragment
 * */
public class VideoBrowseFragment extends XBaseFragment{
    public static final String TAG = VideoBrowseFragment.class.getSimpleName();
    private UGCVideoResult videoResult;
    private List<UGCVideoResult> videoResultList;
    private int mDataPage;
    private int playType;

    @BindView(R.id.verticalPager)
    VerticalScrollViewPager mVerticalPager;

    /**
     * 网络获取视频数据
     */
    public static VideoBrowseFragment newInstance(int playType) {
        Bundle args = new Bundle();
        args.putInt(Constants.UGC_PLAY_TYPE, playType);
        VideoBrowseFragment fragment = new VideoBrowseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 播放单个视频
     */
    public static VideoBrowseFragment newInstance(int playType, UGCVideoResult videoResult) {
        Bundle args = new Bundle();
        args.putInt(Constants.UGC_PLAY_TYPE, playType);
        args.putParcelable(VideoBrowseActivity.SHORT_VIDEO, videoResult);
        VideoBrowseFragment fragment = new VideoBrowseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 播放多个视频
     */
    public static VideoBrowseFragment newInstance(int playType, int mDataPage/*当前处于第几页*/, UGCVideoResult videoResult, ArrayList<UGCVideoResult> videoResultList) {
        Bundle args = new Bundle();
        args.putInt(Constants.UGC_PLAY_TYPE, playType);
        args.putInt(VideoBrowseActivity.DATA_PAGE, mDataPage);
        args.putParcelable(VideoBrowseActivity.SHORT_VIDEO, videoResult);
        args.putParcelableArrayList(VideoBrowseActivity.SHORT_VIDEO_LIST, videoResultList);
        VideoBrowseFragment fragment = new VideoBrowseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public VideoBrowseFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playType = getArguments().getInt(Constants.UGC_PLAY_TYPE);
        videoResult = getArguments().getParcelable(VideoBrowseActivity.SHORT_VIDEO);
        videoResultList = getArguments().getParcelableArrayList(VideoBrowseActivity.SHORT_VIDEO_LIST);
        mDataPage = getArguments().getInt(VideoBrowseActivity.DATA_PAGE, 1);
        if (videoResultList == null) {
            videoResultList = new ArrayList<>();
        }
        if (playType == Constants.UGC_PLAY_TYPE_SINGLE) {
            videoResultList.add(videoResult);
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        ShortCoverAdapter adapter = new ShortCoverAdapter(getActivity().getSupportFragmentManager(),videoResultList);
        mVerticalPager.setAdapter(adapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_video_browse;
    }

    @Override
    public Object newP() {
        return null;
    }
}
