package com.feiyu.videochat.ui.activitys;

import android.os.Bundle;
import com.feiyu.videochat.R;

public class SplashActivity extends XBaseActivity {

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public Object newP() {
        return null;
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
