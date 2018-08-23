package com.feiyu.videochat.ui.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.feiyu.videochat.App;
import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseActivity;
import com.feiyu.videochat.model.PhoneVertifyResult;
import com.feiyu.videochat.net.api.Api;
import com.feiyu.videochat.net.httprequest.ApiCallback;
import com.feiyu.videochat.net.httprequest.okhttp.CommonOkHttpCallBack;
import com.feiyu.videochat.net.httprequest.okhttp.JKOkHttpParamKey;
import com.feiyu.videochat.net.httprequest.okhttp.OkHttpRequestUtils;

import butterknife.BindView;

public class SearchActivity extends XBaseActivity implements View.OnClickListener{
    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.search)
    View mSearch;
    @BindView(R.id.content)
    EditText mContent;

    public static final String TAG = SearchActivity.class.getSimpleName();

    @Override
    public void initData(Bundle savedInstanceState) {
        mBack.setOnClickListener(this);
        mSearch.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static void open(Context context){
        context.startActivity(new Intent(context,SearchActivity.class));
    }

    @Override
    public void onClick(View v) {
        if (v == mBack){
            this.finish();
        }
        if (v == mSearch){
        }
    }
}
