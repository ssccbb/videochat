package com.feiyu.videochat.ui.fragments.setting;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feiyu.videochat.App;
import com.feiyu.videochat.R;
import com.feiyu.videochat.adapter.HomeChildPagerAdapter;
import com.feiyu.videochat.common.XBaseFragment;
import com.feiyu.videochat.ui.activitys.MsgActivity;
import com.feiyu.videochat.ui.activitys.SearchActivity;
import com.feiyu.videochat.ui.activitys.SettingActivity;
import com.feiyu.videochat.utils.SharedPreUtil;
import com.feiyu.videochat.utils.Utils;
import com.feiyu.videochat.views.VCDialog;

import butterknife.BindView;

public class SysSettingFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = SysSettingFragment.class.getSimpleName();
    public static SysSettingFragment instance;
    private Handler handler = new Handler();

    @BindView(R.id.back)
    View mHeadBack;
    @BindView(R.id.tittle)
    TextView mHeadTittle;
    @BindView(R.id.ll_about_us)
    View mAboutUs;
    @BindView(R.id.tv_exit_login)
    View mExit;
    @BindView(R.id.tv_cache)
    TextView mCache;
    @BindView(R.id.ll_clear_cache)
    View mClearCache;
    @BindView(R.id.ll_black_list_setting_system)
    View mBlackList;
    @BindView(R.id.ll_update_version_setting_system)
    LinearLayout mLlUpdateVersion;

    public static SysSettingFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new SysSettingFragment();
    }

    public SysSettingFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mHeadTittle.setText("设置");
        mHeadBack.setOnClickListener(this);
        mAboutUs.setOnClickListener(this);
        mExit.setOnClickListener(this);
        mClearCache.setOnClickListener(this);
        mLlUpdateVersion.setOnClickListener(this);
        mBlackList.setOnClickListener(this);

        try {
            mCache.setText(Utils.getTotalCacheSize(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sys_setting;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack){
            getActivity().finish();
        }
        if (v == mBlackList){
            MsgActivity.open(getActivity(),"黑名单");
        }
        if (v == mClearCache){
            VCDialog dialog = new VCDialog(VCDialog.Ddialog_Without_tittle_Block_Confirm, "", "确认清除本地缓存？");
            dialog.addOnDialogActionListner(new VCDialog.onDialogActionListner() {
                @Override
                public void onCancel() {
                }

                @Override
                public void onConfirm() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mCache.setText("0MB");
                            Utils.cleanFiles(getContext());//data/data/file
                            Utils.cleanExternalCache(getContext());//外存
                            Utils.cleanInternalCache(getContext());//清除本应用内部缓存
                            Utils.clearAllCache(getContext());
                            Toast.makeText(getContext(), "清除缓存成功！", Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                }
            });
            dialog.show(getChildFragmentManager(), VCDialog.TAG);
        }
        if (v == mLlUpdateVersion){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "当前已是最新版本！", Toast.LENGTH_SHORT).show();
                }
            },1500);
        }
        if (v == mAboutUs){
            SettingActivity.open(getActivity(),HelpFragment.TAG);
        }
        if (v == mExit){
            VCDialog dialog = new VCDialog(VCDialog.Ddialog_Without_tittle_Block_Confirm, "退出登录", "确定要退出登录？");
            dialog.addOnDialogActionListner(new VCDialog.onDialogActionListner() {
                @Override
                public void onCancel() {
                    dialog.dismissAllowingStateLoss();
                }

                @Override
                public void onConfirm() {
                    //log out
                    SharedPreUtil.clearLoginInfo();
                    getActivity().finish();
                }
            });
            dialog.show(getChildFragmentManager(), VCDialog.TAG);
        }
    }
}
