package com.qiiiqjk.kkanzh.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.qiiiqjk.kkanzh.R;
import com.qiiiqjk.kkanzh.common.XBaseActivity;
import com.qiiiqjk.kkanzh.common.XBaseFragment;
import com.qiiiqjk.kkanzh.ui.fragments.UserVideoFragment;
import com.qiiiqjk.kkanzh.ui.fragments.UserFansFragment;
import com.qiiiqjk.kkanzh.ui.fragments.UserFollowFragment;
import com.qiiiqjk.kkanzh.utils.StringUtils;

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
