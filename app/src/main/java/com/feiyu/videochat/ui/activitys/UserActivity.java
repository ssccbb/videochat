package com.feiyu.videochat.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseActivity;
import com.feiyu.videochat.common.XBaseFragment;
import com.feiyu.videochat.ui.fragments.setting.UserInfoEditFragment;
import com.feiyu.videochat.ui.fragments.UserVideoFragment;
import com.feiyu.videochat.ui.fragments.UserFansFragment;
import com.feiyu.videochat.ui.fragments.UserFollowFragment;
import com.feiyu.videochat.utils.StringUtils;

public class UserActivity extends XBaseActivity {
    public static final String TAG = UserActivity.class.getSimpleName();

    @Override
    public void initData(Bundle savedInstanceState) {
        if (!this.getIntent().hasExtra(TAG)) {
            this.finish();
            return;
        }
        String fragmentTag = this.getIntent().getStringExtra(TAG);
        if (StringUtils.isEmpty(fragmentTag)){
            this.finish();
            return;
        }
        XBaseFragment fragment = null;
        if (fragmentTag.equals(UserFollowFragment.TAG)){
            fragment = UserFollowFragment.newInstance();
        }else if (fragmentTag.equals(UserVideoFragment.TAG)){
            fragment = UserVideoFragment.newInstance();
        }else {
            fragment = UserFansFragment.newInstance();
        }
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container,fragment,fragmentTag)
                .show(fragment)
                .commitAllowingStateLoss();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static void open(Context context, String fragment) {
        Intent goTo = new Intent(context, UserActivity.class);
        goTo.putExtra(TAG,fragment);
        context.startActivity(goTo);
    }
}
