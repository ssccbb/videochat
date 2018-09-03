package com.qiiiqjk.kkanzh.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qiiiqjk.kkanzh.common.Constants;
import com.qiiiqjk.kkanzh.model.UGCVideoResult;
import com.qiiiqjk.kkanzh.ui.activitys.VideoBrowseActivity;
import com.qiiiqjk.kkanzh.views.VerticalScrollViewPager;
import com.qiiiqjk.kkanzh.R;
import com.qiiiqjk.kkanzh.adapter.ShortCoverAdapter;
import com.qiiiqjk.kkanzh.common.XBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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
        mVerticalPager.setCurrentItem(mDataPage);
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
