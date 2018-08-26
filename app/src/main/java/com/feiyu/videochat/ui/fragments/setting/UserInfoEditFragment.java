package com.feiyu.videochat.ui.fragments.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseFragment;
import com.feiyu.videochat.model.LoginInfoResults;
import com.feiyu.videochat.utils.SharedPreUtil;
import com.feiyu.videochat.views.CircleImageView;

import butterknife.BindView;

public class UserInfoEditFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = UserInfoEditFragment.class.getSimpleName();
    public static UserInfoEditFragment instance;
    private LoginInfoResults mUser;

    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.tittle)
    TextView mTIttle;

    @BindView(R.id.item_avatar)
    View mItemAvatar;
    @BindView(R.id.item_id)
    View mItemId;
    @BindView(R.id.item_nickname)
    View mItemNickname;
    @BindView(R.id.item_age)
    View mItemAge;
    @BindView(R.id.item_sex)
    View mItemSex;
    @BindView(R.id.item_motion)
    View mItemMotion;
    @BindView(R.id.item_sign)
    View mItemSign;

    @BindView(R.id.avatar)
    CircleImageView mAvatar;
    @BindView(R.id.id)
    TextView mId;
    @BindView(R.id.nickname)
    TextView mNcikname;
    @BindView(R.id.age)
    TextView mAge;
    @BindView(R.id.sex)
    TextView mSex;
    @BindView(R.id.motion)
    TextView mMotion;
    @BindView(R.id.desc)
    TextView mDesc;

    public static UserInfoEditFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new UserInfoEditFragment();
    }

    public UserInfoEditFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mTIttle.setText("资料编辑");
        mBack.setOnClickListener(this::onClick);
        mItemAvatar.setOnClickListener(this::onClick);
        mItemId.setOnClickListener(this::onClick);
        mItemNickname.setOnClickListener(this::onClick);
        mItemAge.setOnClickListener(this::onClick);
        mItemSex.setOnClickListener(this::onClick);
        mItemMotion.setOnClickListener(this::onClick);
        mItemSign.setOnClickListener(this::onClick);

        mUser = SharedPreUtil.getLoginInfo();
        if (mUser == null) return;
        mId.setText(mUser.user_id);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_info_edit;
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
        if (v == mItemAvatar){

        }
        if (v == mItemNickname){

        }
        if (v == mItemAge){

        }
        if (v == mItemSex){

        }
        if (v == mItemMotion){

        }
        if (v == mItemSign){

        }
    }
}
