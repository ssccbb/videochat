package com.qiiiqjk.kkanzh.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.qiiiqjk.kkanzh.common.XBaseActivity;
import com.qiiiqjk.kkanzh.common.XBaseFragment;
import com.qiiiqjk.kkanzh.model.AnchorInfoResult;
import com.qiiiqjk.kkanzh.ui.fragments.HostVideoFragment;
import com.qiiiqjk.kkanzh.utils.StringUtils;
import com.qiiiqjk.kkanzh.R;
import com.qiiiqjk.kkanzh.ui.fragments.HostPicFragment;
import com.google.gson.Gson;

public class HostResActivity extends XBaseActivity {
    public static final String TAG = HostResActivity.class.getSimpleName();
    public static final String HOST = "host_bundle";

    @Override
    public void initData(Bundle savedInstanceState) {
        if (!this.getIntent().hasExtra(TAG)) {
            this.finish();
            return;
        }
        String fragmentTag = this.getIntent().getStringExtra(TAG);
        if (StringUtils.isEmpty(fragmentTag)) {
            this.finish();
            return;
        }
        XBaseFragment fragment = null;
        if (fragmentTag.equals(HostPicFragment.TAG)) {
            fragment = HostPicFragment.newInstance();
        } else if (fragmentTag.equals(HostVideoFragment.TAG)) {
            fragment = HostVideoFragment.newInstance();
        }
        if (this.getIntent().hasExtra(HOST)) {
            String data = this.getIntent().getStringExtra(HOST);
            Bundle bundle = new Bundle();
            bundle.putString(fragmentTag,data);
            fragment.setArguments(bundle);
        }
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, fragment, fragmentTag)
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

    public static void open(Context context, String fragment, AnchorInfoResult host) {
        Intent goTo = new Intent(context, HostResActivity.class);
        goTo.putExtra(TAG, fragment);
        if (host != null) {
            Gson gson = new Gson();
            goTo.putExtra(HOST,gson.toJson(host));
        }
        context.startActivity(goTo);
    }
}
