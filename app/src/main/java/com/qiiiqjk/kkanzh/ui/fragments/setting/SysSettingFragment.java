package com.qiiiqjk.kkanzh.ui.fragments.setting;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qiiiqjk.kkanzh.BuildConfig;
import com.qiiiqjk.kkanzh.common.XBaseFragment;
import com.qiiiqjk.kkanzh.model.CheckUpdateResult;
import com.qiiiqjk.kkanzh.model.PhoneVertifyResult;
import com.qiiiqjk.kkanzh.net.api.Api;
import com.qiiiqjk.kkanzh.net.httprequest.ApiCallback;
import com.qiiiqjk.kkanzh.net.httprequest.okhttp.JKOkHttpParamKey;
import com.qiiiqjk.kkanzh.net.httprequest.okhttp.OkHttpRequestUtils;
import com.qiiiqjk.kkanzh.ui.activitys.IndexActivity;
import com.qiiiqjk.kkanzh.utils.DownloadUtil;
import com.qiiiqjk.kkanzh.utils.StringUtils;
import com.qiiiqjk.kkanzh.utils.Utils;
import com.qiiiqjk.kkanzh.views.dialog.UpdateDialogFragment;
import com.qiiiqjk.kkanzh.views.dialog.VCDialog;
import com.qiiiqjk.kkanzh.R;
import com.qiiiqjk.kkanzh.ui.activitys.MsgActivity;
import com.qiiiqjk.kkanzh.ui.activitys.SettingActivity;
import com.qiiiqjk.kkanzh.utils.SharedPreUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

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
        if (Utils.isHideMode()) {
            mLlUpdateVersion.setVisibility(View.GONE);
        }

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
            checkUpdate();
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

    private void checkUpdate(){
        if (!SharedPreUtil.isLogin()) return;
        OkHttpRequestUtils.getInstance().requestByGet(Api.API_BASE_URL +"/config/check_update",
                OkHttpRequestUtils.getInstance().JkRequestParameters(JKOkHttpParamKey.CHECK_UPDATE,
                        SharedPreUtil.getLoginInfo().uid, "2", String.valueOf(BuildConfig.VERSION_CODE)),
                PhoneVertifyResult.class, getActivity(), new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.e("update", "onSuccess: "+(String) response );
                        try {
                            JSONObject object = new JSONObject((String)response);
                            if(object.get("data") != null){
                                Gson gson = new Gson();
                                CheckUpdateResult update = gson.fromJson(
                                        object.getJSONObject("data").toString(), CheckUpdateResult.class);
                                if (StringUtils.isEmpty(update.link)) return;
                                UpdateDialogFragment dialog = new UpdateDialogFragment();
                                Bundle b = new Bundle();
                                b.putSerializable(UpdateDialogFragment.TAG,update);
                                dialog.setArguments(b);
                                dialog.addOnClickListener(new UpdateDialogFragment.onClickListener() {
                                    @Override
                                    public void onConfirmCallback() {
                                        dialog.dismissAllowingStateLoss();
                                        Toast.makeText(getContext(), "后台下载", Toast.LENGTH_SHORT).show();
                                        download(update.link);
                                    }

                                    @Override
                                    public void onCancelCallback() {
                                        dialog.dismissAllowingStateLoss();
                                    }
                                });
                                dialog.show(getActivity().getSupportFragmentManager(),UpdateDialogFragment.TAG);
                            }else {
                                Toast.makeText(getContext(), "已经是最新版本！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "已经是最新版本！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String err_msg) {
                        Log.e("update", "onError: "+err_msg );
                        Toast.makeText(getContext(), "安装包下载失败！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure() {
                        Log.e("update", "onFailure !");
                        Toast.makeText(getContext(), "安装包下载失败！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void download(String url){
        DownloadUtil.get().download(url, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File str) {
                Log.e("update", "onDownloadSuccess: "+str.getAbsolutePath() );
                Utils.installApk(getContext(),str);
            }

            @Override
            public void onDownloading(int progress) {
                Log.e("update", "onDownloading: "+progress );
            }

            @Override
            public void onDownloadFailed() {
                Log.e("update", "onDownloadFailed! ");
                Toast.makeText(getContext(), "安装包下载失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
