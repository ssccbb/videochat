package com.feiyu.videochat.ui.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
        ArrayList<UGCVideoResult> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UGCVideoResult video = new UGCVideoResult(i);
            data.add(video);
        }
        VideoBrowseFragment fragment = VideoBrowseFragment.newInstance(Constants.UGC_PLAY_TYPE_MULTIPLE,1,data.get(0),data);
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

    public static void open(Context context){
        context.startActivity(new Intent(context,VideoBrowseActivity.class));
    }
}
