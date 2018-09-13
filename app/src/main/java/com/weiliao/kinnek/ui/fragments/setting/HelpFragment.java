package com.weiliao.kinnek.ui.fragments.setting;

import android.os.Bundle;
import android.view.View;

import com.weiliao.kinnek.common.XBaseFragment;
import com.weiliao.kinnek.R;

public class HelpFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = HelpFragment.class.getSimpleName();
    public static HelpFragment instance;

    public static HelpFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new HelpFragment();
    }

    public HelpFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_help;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View v) {
    }
}
