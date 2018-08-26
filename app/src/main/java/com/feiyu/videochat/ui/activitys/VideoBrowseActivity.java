package com.feiyu.videochat.ui.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import com.feiyu.videochat.App;
import com.feiyu.videochat.R;
import com.feiyu.videochat.common.Constants;
import com.feiyu.videochat.common.XBaseActivity;
import com.feiyu.videochat.model.UGCVideoResult;
import com.feiyu.videochat.ui.fragments.VideoBrowseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频浏览页面
 * */
public class VideoBrowseActivity extends XBaseActivity {
    public static final String TAG = VideoBrowseActivity.class.getSimpleName();
    public static final String SHORT_VIDEO_LIST = "short_video_list";
    public static final String SHORT_VIDEO = "short_video";
    public static final String DATA_PAGE = "data_page";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Bundle extras = this.getIntent().getExtras();
        int page = extras.getInt(DATA_PAGE);
        UGCVideoResult video = extras.getParcelable(SHORT_VIDEO);
        ArrayList<UGCVideoResult> videos = extras.getParcelableArrayList(SHORT_VIDEO_LIST);
        VideoBrowseFragment fragment = VideoBrowseFragment.newInstance(Constants.UGC_PLAY_TYPE_MULTIPLE,page,video,videos);
        ft.add(R.id.container,fragment,VideoBrowseFragment.TAG).show(fragment).commitAllowingStateLoss();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_browse;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static void open(Context context,int dataPage,UGCVideoResult currentData,ArrayList<UGCVideoResult> datas){
        Intent goTo = new Intent(context, VideoBrowseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(SHORT_VIDEO,currentData);
        bundle.putParcelableArrayList(SHORT_VIDEO_LIST,datas);
        bundle.putInt(DATA_PAGE,dataPage);
        goTo.putExtras(bundle);
        context.startActivity(goTo);
    }
}
