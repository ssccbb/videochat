package com.qiiiqjk.kkanzh.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qiiiqjk.kkanzh.common.XBaseFragment;
import com.qiiiqjk.kkanzh.R;

import butterknife.BindView;

public class UserFansFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = UserFansFragment.class.getSimpleName();
    public static UserFansFragment instance;

    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.tittle)
    TextView mTIttle;

    public static UserFansFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new UserFansFragment();
    }

    public UserFansFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mTIttle.setText("粉丝");
        mBack.setOnClickListener(this::onClick);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_fans;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == mBack){
            getActivity().finish();
        }
    }
}
