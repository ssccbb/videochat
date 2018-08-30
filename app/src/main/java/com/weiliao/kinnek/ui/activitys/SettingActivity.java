package com.weiliao.kinnek.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.weiliao.kinnek.R;
import com.weiliao.kinnek.common.XBaseActivity;
import com.weiliao.kinnek.common.XBaseFragment;
import com.weiliao.kinnek.ui.fragments.setting.ChargeFragment;
import com.weiliao.kinnek.ui.fragments.setting.HelpFragment;
import com.weiliao.kinnek.ui.fragments.setting.SysSettingFragment;
import com.weiliao.kinnek.ui.fragments.setting.UserInfoEditFragment;
import com.weiliao.kinnek.utils.StringUtils;
import com.google.gson.Gson;

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
