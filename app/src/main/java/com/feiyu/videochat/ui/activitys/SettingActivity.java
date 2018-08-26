package com.feiyu.videochat.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseActivity;
import com.feiyu.videochat.common.XBaseFragment;
import com.feiyu.videochat.ui.fragments.setting.ChargeFragment;
import com.feiyu.videochat.ui.fragments.setting.HelpFragment;
import com.feiyu.videochat.ui.fragments.setting.SysSettingFragment;
import com.feiyu.videochat.ui.fragments.setting.UserInfoEditFragment;
import com.feiyu.videochat.utils.StringUtils;

public class SettingActivity extends XBaseActivity {
    public static final String TAG = SettingActivity.class.getSimpleName();

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
        if (fragmentTag.equals(ChargeFragment.TAG)){
            fragment = ChargeFragment.newInstance();
        }else if (fragmentTag.equals(HelpFragment.TAG)){
            fragment = HelpFragment.newInstance();
        }else if (fragmentTag.equals(UserInfoEditFragment.TAG)){
            fragment = UserInfoEditFragment.newInstance();
        }else {
            fragment = SysSettingFragment.newInstance();
        }
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container,fragment,fragmentTag)
                .show(fragment)
                .commitAllowingStateLoss();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static void open(Context context, String fragment) {
        Intent goTo = new Intent(context, SettingActivity.class);
        goTo.putExtra(TAG,fragment);
        context.startActivity(goTo);
    }
}
