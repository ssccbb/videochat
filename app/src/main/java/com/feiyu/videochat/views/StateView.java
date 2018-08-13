package com.feiyu.videochat.views;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feiyu.videochat.R;
import com.feiyu.videochat.event.BusProvider;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.kit.KnifeKit;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.functions.Consumer;

import static android.content.ContentValues.TAG;

public class StateView extends LinearLayout {

    @BindView(R.id.tv_msg)
    TextView tvMsg;

    /**
     * 定义错误类型为了处理网络retry
     */
    public static final int REFRESH = 0;//刷新
    public static final int LOADMORE = 1;//加载更多

    private int loadDataType = Integer.MAX_VALUE;//当前数据错误属于哪个加载类型
    private int curPage = Integer.MAX_VALUE;//当前数据错误性情属于哪个加载页面
    XRecyclerView.OnRefreshAndLoadMoreListener onRefreshAndLoadMoreListener;

    public void setCustomClickListener(StateViewClickListener clickListener) {//不设置刷新监听时自定义错误处理事件
        this.viewClickListener = clickListener;
    }

    StateViewClickListener viewClickListener;

    public XRecyclerView.OnRefreshAndLoadMoreListener getOnRefreshAndLoadMoreListener() {
        return onRefreshAndLoadMoreListener;
    }

    public void setLoadDataType(int loadDataType, int curPage) {
        this.loadDataType = loadDataType;
        this.curPage = curPage;
    }

    public void setOnRefreshAndLoadMoreListener(XRecyclerView.OnRefreshAndLoadMoreListener onRefreshAndLoadMoreListener) {
        this.onRefreshAndLoadMoreListener = onRefreshAndLoadMoreListener;
    }

    public StateView(Context context) {
        super(context);
        setupView(context);
    }

    public StateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public StateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
    }

    private void setupView(Context context) {
        inflate(context, R.layout.view_common_state, this);
        KnifeKit.bind(this);
        tvMsg = findViewById(R.id.tv_msg);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onRefreshAndLoadMoreListener) {
                    if (loadDataType == REFRESH) {
                        onRefreshAndLoadMoreListener.onRefresh();
                    } else if (loadDataType == LOADMORE) {
                        onRefreshAndLoadMoreListener.onLoadMore(curPage);
                    }
                } else {
                    if (null != viewClickListener) {
                        viewClickListener.doAction();
                    }
                }
            }
        });

        BusProvider.getBus().receive(NetError.class).subscribe(new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
            }
        });
        BusProvider.getBus().receive(NetworkInfo.class).subscribe(new Consumer<NetworkInfo>() {
            @Override
            public void accept(NetworkInfo activeNetwork) throws Exception {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    Log.e(TAG, "当前WiFi连接可用 ");
                    performClick();
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    Log.e(TAG, "当前移动网络连接可用 ");
                    performClick();
                }
            }
        }/*new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                if(integer == ConnectivityManager.TYPE_MOBILE||integer==ConnectivityManager.TYPE_WIFI){
                    performClick();
                }
            }
        }*/);
    }

    public void setMsg(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (null != tvMsg) {
                tvMsg.setText(msg);
            }
        }
    }

    public void showError(NetError error) {
        if (error != null) {
            switch (error.getType()) {
                case NetError.ParseError:
                    setMsg("数据解析异常:"/*.concat(Kits.Empty.check(error.getMessage()) ? "" : error.getMessage())*/);
                    break;

                case NetError.AuthError:
                    setMsg("身份验证异常:"/*.concat(Kits.Empty.check(error.getMessage()) ? "" : error.getMessage())*/);
                    break;

                case NetError.BusinessError:
                    setMsg("业务异常:"/*.concat(Kits.Empty.check(error.getMessage()) ? "" : error.getMessage())*/);
                    break;

                case NetError.NoConnectError:
                    setMsg("网络无连接:"/*.concat(Kits.Empty.check(error.getMessage()) ? "" : error.getMessage())*/);
                    break;

                case NetError.NoDataError:
                    setMsg("数据为空:"/*.concat(Kits.Empty.check(error.getMessage()) ? "" : error.getMessage())*/);
                    break;

                case NetError.OtherError:
                    setMsg("其他异常:"/*.concat(Kits.Empty.check(error.getMessage()) ? "" : error.getMessage())*/);
                    break;
            }
        }
    }

    public interface StateViewClickListener {
        public void doAction();
    }
}
