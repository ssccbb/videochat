package com.qiiiqjk.kkanzh.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.qiiiqjk.kkanzh.R;
import com.qiiiqjk.kkanzh.common.XBaseActivity;

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
