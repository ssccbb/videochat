package com.feiyu.videochat.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseActivity;

import butterknife.BindView;

public class SplashActivity extends XBaseActivity implements View.OnClickListener{
    @BindView(R.id.btn_next)
    TextView skip;

    private Handler uiHandler = new Handler();
    private Runnable skipRunnable = new Runnable() {
        @Override
        public void run() {
            next();
        }
    };

    @Override
    public void initData(Bundle savedInstanceState) {
        skip.setOnClickListener(this);
        uiHandler.postDelayed(skipRunnable,3000);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == skip){
            next();
        }
    }

    private void next(){
        startActivity(new Intent(this,IndexActivity.class));
    }

//    private void getGameAccountInfo(final int page, boolean isrefresh) {
//        Flowable<HttpResultModel<GameAccountResultModel>> fr = DataService.getGameAccountList(new GameAccountRequestBody(page));
//        RxLoadingUtils.subscribeWithReload(mContent, fr, bindToLifecycle(), new Consumer<HttpResultModel<GameAccountResultModel>>() {
//            @Override
//            public void accept(HttpResultModel<GameAccountResultModel> gameAccountResultModelHttpResultModel) throws Exception {
//                notifyData(gameAccountResultModelHttpResultModel.data, page);
//                mContent.getRecyclerView().setPage(gameAccountResultModelHttpResultModel.current_page, gameAccountResultModelHttpResultModel.total_page);
//            }
//        }, null, null, isrefresh);
//    }
}
