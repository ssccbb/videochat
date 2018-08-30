//package com.feiyu.videochat.wxapi;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.game.helper.activitys.BaseActivity.XBaseActivity;
//import com.game.helper.activitys.OrderConfirmActivity;
//import com.game.helper.data.RxConstant;
//import com.game.helper.model.BaseModel.HttpResultModel;
//import com.game.helper.net.DataService;
//import com.game.helper.net.model.ConsumeRequestBody;
//import com.game.helper.utils.RxLoadingUtils;
//import com.tencent.mm.opensdk.modelbase.BaseReq;
//import com.tencent.mm.opensdk.modelbase.BaseResp;
//import com.tencent.mm.opensdk.openapi.IWXAPI;
//import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
//import com.tencent.mm.opensdk.openapi.WXAPIFactory;
//
//import cn.droidlover.xdroidmvp.net.NetError;
//import io.reactivex.Flowable;
//import io.reactivex.functions.Consumer;
//
//
//public class WXPayEntryActivity extends XBaseActivity implements
//        IWXAPIEventHandler {
//
//    private static final String TAG = "WXPayEntryActivity";
//    private IWXAPI api;
//    private Context mContext;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.d(TAG, "onCreate: WXPayEntryActivity");
//        mContext = this;
//        api = WXAPIFactory.createWXAPI(this, RxConstant.ThirdPartKey.WeixinId);
//        api.handleIntent(getIntent(), this);
//    }
//
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        Log.d(TAG, "onCreate: onNewIntent");
//        setIntent(intent);
//        api.handleIntent(intent, this);
//    }
//
//    @Override
//    public void onReq(BaseReq req) {
//        Log.e("", "BaseReq" + req.checkArgs());
//    }
//
//    @Override
//    public void onResp(BaseResp resp) {
//        Log.e("", "onResp");
//        String tipStr = "";
//        switch (resp.errCode) {
//            case 0:
//                OrderConfirmActivity.isWxPay = true;
//                if(OrderConfirmActivity.consumeRequestBody != null){
//                    doConsume(OrderConfirmActivity.consumeRequestBody);
//                }else{
//                    tipStr = "支付成功";
//                    Toast.makeText(this, tipStr, Toast.LENGTH_SHORT).show();
//                    setResult(RESULT_OK);
//                    finish();
//                }
//                break;
//            case -1:
//                tipStr = "支付遇到错误";
//                Toast.makeText(this, tipStr, Toast.LENGTH_SHORT).show();
//                break;
//            case -2:
//                tipStr = "用户取消";
//                Toast.makeText(this, tipStr, Toast.LENGTH_SHORT).show();
//                break;
//        }
////        BusProvider.getBus().post(new RedPackEvent(0, RxConstant.WX_PAY, resp.errCode));
//        //finish();
//    }
//
//    private void doConsume(ConsumeRequestBody consumeRequestBody) {
////        Log.e("nuoyan", "gameAccountId：：：" + gameAccountId + "\r\n"
////                + "gameId：：：" + gameId + "\r\n"
////                + "consumeAmount:::" + inputBalance + "\r\n"
////                + "accountAmount:::" + accountAmount + "\r\n"
////                + "marketingAmount:::" + marketingAmount + "\r\n"
////                + "rechargeAmount:::" + mNeedPay + "\r\n"
////                + "is_vip:::" + is_vip + "\r\n"
////                + "tradePassword:::" + password + "\r\n"
////                + "redpacketType:::" + mRedpackType + "\r\n"
////                + "redpacketId:::" + mRedpackId + "\r\n"
////                + "payWay:::" + payWay + "\r\n");
//
//        Flowable<HttpResultModel> fr = DataService.consume(consumeRequestBody);
//        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel>() {
//            @Override
//            public void accept(HttpResultModel resultModel) {
//                if (resultModel.isSucceful()) {
//                    Toast.makeText(WXPayEntryActivity.this, resultModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
//                } else {
//                    if (resultModel.isPayStatus()) {
//                        Toast.makeText(WXPayEntryActivity.this, resultModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(WXPayEntryActivity.this, "消费失败！", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                OrderConfirmActivity.consumeRequestBody = null;
//                setResult(RESULT_OK);
//                finish();
//            }
//        }, new Consumer<NetError>() {
//            @Override
//            public void accept(NetError netError) throws Exception {
//                Toast.makeText(WXPayEntryActivity.this, "消费失败！", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    public void initData(Bundle savedInstanceState) {
//
//    }
//
//    @Override
//    public int getLayoutId() {
//        return 0;
//    }
//
//    @Override
//    public Object newP() {
//        return null;
//    }
//}
