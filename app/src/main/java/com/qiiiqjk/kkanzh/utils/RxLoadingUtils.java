package com.qiiiqjk.kkanzh.utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.qiiiqjk.kkanzh.views.ReloadableFrameLayout;
import com.qiiiqjk.kkanzh.views.XReloadableRecyclerContentLayout;
import com.qiiiqjk.kkanzh.views.XReloadableStateContorller;
import com.qiiiqjk.kkanzh.event.BusProvider;
import com.qiiiqjk.kkanzh.event.MsgEvent;
import com.qiiiqjk.kkanzh.model.HotWordResults;
import com.qiiiqjk.kkanzh.model.SpecialResults;
import com.qiiiqjk.kkanzh.model.basemodel.HttpResultModel;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import java.net.UnknownHostException;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.IModel;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;
import io.reactivex.Flowable;
import io.reactivex.FlowableOperator;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;


public class RxLoadingUtils {
    public static <T> void subscribeWithReloadObserver(final XReloadableRecyclerContentLayout reloadableFrameLayout,
                                                       final Observable<T> observable, final ObservableTransformer transformer, final Consumer<T> onNext, final Consumer<NetError> onError,
                                                       final Action onComplete, final boolean showloading) {
        if (reloadableFrameLayout == null) return;

        if (showloading)
            reloadableFrameLayout.showContent();
        reloadableFrameLayout.setOnReloadListener(new XReloadableRecyclerContentLayout.OnReloadListener() {
            @Override
            public void onReload(XReloadableRecyclerContentLayout reloadableFrameLayout) {
                subscribeWithReloadObserver(reloadableFrameLayout, observable, transformer, onNext, onError, onComplete, showloading);
            }
        });
        final boolean[] finishReload = new boolean[]{false};
        observable
                .compose(transformer)
                .compose(new ObservableTransformer() {
                    @Override
                    public ObservableSource apply(Observable upstream) {
                        return upstream.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribe(new ResourceObserver<T>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onNext(T t) {
                        if (onNext != null) {
                            try {
                                onNext.accept(t);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!finishReload[0]) {
                            finishReload[0] = true;
                        }
                        reloadableFrameLayout.refreshState(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        NetError error = null;
                        if (e != null) {
                            if (!(e instanceof NetError)) {
                                if (e instanceof UnknownHostException) {
                                    error = new NetError(e, NetError.NoConnectError);
                                } else if (e instanceof JSONException
                                        || e instanceof JsonParseException
                                        || e instanceof JsonSyntaxException) {
                                    error = new NetError(e, NetError.ParseError);
                                } else {
                                    error = new NetError(e, NetError.OtherError);
                                }
                            } else {
                                error = (NetError) e;
                            }

                            if (XApi.getCommonProvider() != null) {
                                if (XApi.getCommonProvider().handleError(error)) {        //使用通用异常处理
                                    return;
                                }
                            }
                        }
                        if (onError != null) {
                            try {
                                BusProvider.getBus().post(error);
                                onError.accept(error);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                        reloadableFrameLayout.refreshState(false);
                        if (!finishReload[0]) {
                            reloadableFrameLayout.showError();
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (onComplete != null) {
                            try {
                                onComplete.run();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!finishReload[0]) {
//                            reloadableFrameLayout.finishReload();
                        }
                    }
                });
    }

    public static <T extends IModel> void subscribeWithReload(final ReloadableFrameLayout reloadableFrameLayout,
                                                              final Flowable<T> Flowable, final FlowableTransformer transformer, final Consumer<T> onNext, final Consumer<NetError> onError,
                                                              final Action onComplete, final boolean finishWhenFirstOnNext) {
        if (reloadableFrameLayout == null) return;

        reloadableFrameLayout.showLoadingView();
        reloadableFrameLayout.setOnReloadListener(new ReloadableFrameLayout.OnReloadListener() {
            @Override
            public void onReload(ReloadableFrameLayout reloadableFrameLayout) {
                subscribeWithReload(reloadableFrameLayout, Flowable, transformer, onNext, onError, onComplete,
                        finishWhenFirstOnNext);
            }
        });

        final boolean[] finishReload = new boolean[]{false};

        Flowable
                .compose(XApi.<T>getApiTransformer())
                .compose(XApi.<T>getScheduler())
                .compose(transformer)
                .subscribe(new ApiSubscriber<T>() {

                    @Override
                    protected void onStart() {
                        super.onStart();

                    }

                    @Override
                    public void onNext(T t) {
                        if (onNext != null) {
                            try {
                                onNext.accept(t);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (finishWhenFirstOnNext && !finishReload[0]) {
                            reloadableFrameLayout.finishReload();
                            finishReload[0] = true;
                        }
                    }

                    @Override
                    protected void onFail(NetError error) {
                        if (onError != null) {
                            try {
                                BusProvider.getBus().post(error);
                                onError.accept(error);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!finishReload[0]) {
                            reloadableFrameLayout.needReload(getDisplayMessage(error, true));
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (onComplete != null) {
                            try {
                                onComplete.run();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!finishReload[0]) {
                            reloadableFrameLayout.finishReload();
                        }
                    }
                });
    }

    public static <T extends IModel> void subscribeWithReload(final ReloadableFrameLayout reloadableFrameLayout,
                                                              Flowable<HttpResultModel<HotWordResults>> Flowable, final FlowableTransformer transformer, final Consumer<HttpResultModel<HotWordResults>> onNext, final Consumer<NetError> onError) {
        subscribeWithReload(reloadableFrameLayout, Flowable, transformer, onNext, onError, null, false);
    }

    public static <T extends IModel> void subscribeWithReload(final ReloadableFrameLayout reloadableFrameLayout,
                                                              Flowable<T> Flowable, final FlowableTransformer transformer, final Consumer<T> onNext, final Consumer<NetError> onError,
                                                              boolean finishWhenFirstOnNext) {
        subscribeWithReload(reloadableFrameLayout, Flowable, transformer, onNext, onError, null,
                finishWhenFirstOnNext);
    }

    public static <T extends IModel> void subscribeWithReload(final XReloadableRecyclerContentLayout reloadableFrameLayout,
                                                              Flowable<HttpResultModel<SpecialResults>> Flowable, final FlowableTransformer transformer, final Consumer<HttpResultModel<SpecialResults>> onNext) {
        subscribeWithReload(reloadableFrameLayout, Flowable, transformer, onNext, null, null, false);
    }

    public static <T extends IModel> void subscribeWithReload(final ReloadableFrameLayout reloadableFrameLayout,
                                                              Flowable<T> Flowable, final FlowableTransformer transformer, final Consumer<T> onNext,
                                                              boolean finishWhenFirstOnNext) {
        subscribeWithReload(reloadableFrameLayout, Flowable, transformer, onNext, null, null,
                finishWhenFirstOnNext);
    }

    public static <T extends IModel> void subscribeWithReload(final ReloadableFrameLayout reloadableFrameLayout,
                                                              Flowable<T> Flowable) {
        subscribeWithReload(reloadableFrameLayout, Flowable, null, null, null, false);
    }

    public static <T extends IModel> void subscribeWithReload(final ReloadableFrameLayout reloadableFrameLayout,
                                                              Flowable<T> Flowable, boolean finishWhenFirstOnNext) {
        subscribeWithReload(reloadableFrameLayout, Flowable, null, null, null, finishWhenFirstOnNext);
    }

    public static <T extends IModel> void subscribeWithDialog(final ProgressDialog progressDialog,
                                                              final Flowable<T> Flowable, FlowableTransformer transformer, final Consumer<T> onNext, final Consumer<NetError> onError,
                                                              final Action onComplete, final boolean isCancel/*是否可以中断请求*/) {
        /*bindDialog(progressDialog, Flowable, false)*/
        Flowable
                .compose(XApi.<T>getApiTransformer())
                .compose(XApi.<T>getScheduler())
                .compose(transformer)
                .subscribe(new ApiSubscriber<T>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        MainThreadPostUtils.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.show();
                            }
                        });

                        if (isCancel) {
                            progressDialog.setCancelable(true);
                            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    dispose();
                                    BusProvider.getBus().post(new MsgEvent<String>("cancel_request"));
//                                    TotoroToast.makeText(GameMarketApplication.getContext(), "cancel_request", 1).show();
                                }
                            });
                        }
//                        TotoroToast.makeText(GameMarketApplication.getContext(), "onStart", 1).show();
                    }

                    @Override
                    public void onNext(T t) {
                        if (onNext != null) {
                            try {
                                onNext.accept(t);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
//                        TotoroToast.makeText(GameMarketApplication.getContext(), "onSuccess", 1).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        MainThreadPostUtils.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        });
                    }

                    @Override
                    protected void onFail(NetError error) {
                        if (onError != null) {
                            try {
                                BusProvider.getBus().post(error);
                                onError.accept(error);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

//                        if (toastErrorMeg) {
//                            //show Toast Tips
//                        }
                        MainThreadPostUtils.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        });
//                        TotoroToast.makeText(GameMarketApplication.getContext(), "onFail", 1).show();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (onComplete != null) {
                            try {
                                onComplete.run();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        MainThreadPostUtils.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        });
//                        TotoroToast.makeText(GameMarketApplication.getContext(), "onComplete", 1).show();
                    }
                });
    }

    public static <T extends IModel> void subscribeWithDialog(Context context, Flowable<T> Flowable, FlowableTransformer transformer,
                                                              final Consumer<T> onNext, String dialogText) {
        subscribeWithDialog(getDefaultProgressDialog(context, dialogText), Flowable, transformer, onNext, null,
                null, true);
    }

    public static <T extends IModel> void subscribeWithDialog(Context context, Flowable<T> Flowable, FlowableTransformer transformer,
                                                              final Consumer<T> onNext, final Consumer<NetError> onError, final Action onComplete, String dialogText) {
        subscribeWithDialog(getDefaultProgressDialog(context, dialogText), Flowable, transformer, onNext, onError,
                onComplete, true);
    }

    public static <T extends IModel> void subscribeWithDialog(Context context, Flowable<T> Flowable, FlowableTransformer transformer,
                                                              final Consumer<T> onNext, final Consumer<NetError> onError, String dialogText) {
        subscribeWithDialog(getDefaultProgressDialog(context, dialogText), Flowable, transformer, onNext, onError,
                null, true);
    }

    public static <T extends IModel> void subscribeWithDialog(Context context, Flowable<T> Flowable, FlowableTransformer transformer,
                                                              final Consumer<T> onNext, final Consumer<NetError> onError, boolean toastErrorMsg) {
        subscribeWithDialog(getDefaultProgressDialog(context), Flowable, transformer, onNext, onError, null,
                toastErrorMsg);
    }

    public static <T extends IModel> void subscribeWithDialog(Context context, Flowable<T> Flowable, FlowableTransformer transformer,
                                                              final Consumer<T> onNext, final Consumer<NetError> onError, final Action onComplete) {
        subscribeWithDialog(getDefaultProgressDialog(context), Flowable, transformer, onNext, onError, onComplete, true);
    }

    public static <T extends IModel> void subscribeWithDialog(Context context, Flowable<T> Flowable, FlowableTransformer transformer,
                                                              final Consumer<T> onNext, final Consumer<NetError> onError) {
        subscribeWithDialog(getDefaultProgressDialog(context), Flowable, transformer, onNext, onError, null,
                false);
    }

    public static <T extends IModel> void subscribeWithDialog(Context context, Flowable<T> Flowable, FlowableTransformer transformer,
                                                              final Consumer<T> onNext, boolean toastErrorMsg) {
        subscribeWithDialog(getDefaultProgressDialog(context), Flowable, transformer, onNext, null, null,
                toastErrorMsg);
    }

    public static <T extends IModel> void subscribeWithDialog(Context context, Flowable<T> Flowable, FlowableTransformer transformer,
                                                              final Consumer<T> onNext) {
        subscribeWithDialog(getDefaultProgressDialog(context), Flowable, transformer, onNext, null, null, true);
    }

    public static <T extends IModel> void subscribeWithDialog(Context context, Flowable<T> Flowable, FlowableTransformer transformer,
                                                              boolean toastErrorMsg) {
        subscribeWithDialog(getDefaultProgressDialog(context), Flowable, transformer, null, null, null,
                toastErrorMsg);
    }

    private static ProgressDialog getDefaultProgressDialog(Context context) {
        return getDefaultProgressDialog(context, "加载中...");
    }

    private static ProgressDialog getDefaultProgressDialog(Context context, String text) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("");
        dialog.setMessage(text);
        dialog.setCancelable(false);
        return dialog;
    }

    public static <T extends IModel> void subscribe(Flowable<T> Flowable, FlowableTransformer transformer, final Consumer<T> onNext,
                                                    final Consumer<NetError> onError,
                                                    final Action onComplete, final boolean toastError) {

        Flowable.compose(XApi.<T>getApiTransformer())
                .compose(XApi.<T>getScheduler())
                .compose(transformer)
                .subscribe(new ApiSubscriber<T>() {

                    @Override
                    protected void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onNext(T t) {
                        if (onNext != null) {
                            try {
                                onNext.accept(t);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    protected void onFail(NetError error) {
                        if (onError != null) {
                            try {
                                BusProvider.getBus().post(error);
                                onError.accept(error);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (toastError) {
                            //show Toast Tips
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (onComplete != null) {
                            try {
                                onComplete.run();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                });
    }

    public static <T extends IModel> void subscribe(Flowable<T> Flowable, FlowableTransformer transformer, final Consumer<T> onNext,
                                                    final Consumer<NetError> onError) {
        subscribe(Flowable, transformer, onNext, onError, null, false);
    }

    public static <T extends IModel> void subscribe(Flowable<T> Flowable, FlowableTransformer transformer, final Consumer<T> onNext,
                                                    final Consumer<NetError> onError, boolean toastError) {
        subscribe(Flowable, transformer, onNext, onError, null, toastError);
    }

    public static <T extends IModel> void subscribe(Flowable<T> Flowable, FlowableTransformer transformer, final Consumer<T> onNext) {
        subscribe(Flowable, transformer, onNext, null, null, true);
    }

    public static <T extends IModel> void subscribe(Flowable<T> Flowable, FlowableTransformer transformer, final Consumer<T> onNext,
                                                    boolean toastError) {
        subscribe(Flowable, transformer, onNext, null, null, toastError);
    }

    public static <T extends IModel> void subscribe(Flowable<T> Flowable, FlowableTransformer transformer) {
        subscribe(Flowable, transformer, null, null, true);
    }

    public static <T extends IModel> void subscribe(Flowable<T> Flowable, FlowableTransformer transformer, boolean toastError) {
        subscribe(Flowable, transformer, null, null, toastError);
    }

    public static <T extends IModel> Flowable<T> bindDialog(final Context context, Flowable<T> Flowable, FlowableTransformer transformer) {
        return bindDialog(context, Flowable, transformer, false);
    }

    public static <T extends IModel> Flowable<T> bindDialog(final Context context, Flowable<T> Flowable, FlowableTransformer transformer,
                                                            boolean cancelable) {
        return bindDialog(getDefaultProgressDialog(context), Flowable, cancelable);
    }

    public static <T extends IModel> Flowable<T> bindDialog(final ProgressDialog progressDialog,
                                                            final Flowable<T> Flowable, final boolean cancelable) {
        if (progressDialog == null) {
            return Flowable;
        }
        return Flowable.lift(new FlowableOperator<T, T>() {
            @Override
            public Subscriber<? super T> apply(final Subscriber<? super T> observer) throws Exception {
                return new Subscriber<T>() {
                    @Override
                    public void onSubscribe(final Subscription s) {
                        MainThreadPostUtils.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.show();
                            }
                        });

                        if (cancelable) {
                            progressDialog.setCancelable(true);
                            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    s.cancel();
                                }
                            });
                        }
                    }

                    @Override
                    public void onNext(T t) {
                        observer.onNext(t);
                    }

                    @Override
                    public void onError(Throwable t) {
                        MainThreadPostUtils.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        });
                        observer.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        MainThreadPostUtils.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        });
                        observer.onComplete();
                    }
                };
            }
        });
    }

    private static String getDisplayMessage(Throwable throwable, boolean isSub) {
        return throwable instanceof RuntimeException
                ? getThrowableStackTrace(throwable, isSub)
                : throwable.getMessage();
    }

    private static final int MAX_STACK_TRACE_LENGTH = 500;

    private static String getThrowableStackTrace(Throwable throwable, boolean isSub) {
        String stackTrace = throwable.getMessage() + ":" + Log.getStackTraceString(throwable);
        if (isSub && stackTrace.length() > MAX_STACK_TRACE_LENGTH) {
            stackTrace = stackTrace.substring(0, MAX_STACK_TRACE_LENGTH);
        }
        return stackTrace;
    }


    public static <T extends IModel> void subscribeWithReload(final XReloadableStateContorller reloadableFrameLayout,
                                                              final Flowable<T> Flowable, final FlowableTransformer transformer, final Consumer<T> onNext, final Consumer<NetError> onError,
                                                              final Action onComplete, final boolean finishWhenFirstOnNext) {
        if (reloadableFrameLayout == null) return;
        if (finishWhenFirstOnNext) {
            reloadableFrameLayout.showLoading();
        }
        reloadableFrameLayout.setOnReloadListener(new XReloadableStateContorller.OnReloadListener() {
            @Override
            public void onReload(XReloadableStateContorller reloadableFrameLayout) {
                subscribeWithReload(reloadableFrameLayout, Flowable, transformer, onNext, onError, onComplete,
                        finishWhenFirstOnNext);
            }
        });

        final boolean[] finishReload = new boolean[]{false};

        Flowable
                .compose(XApi.<T>getApiTransformer())
                .compose(XApi.<T>getScheduler())
                .compose(transformer)
                .subscribe(new ApiSubscriber<T>() {

                    @Override
                    protected void onStart() {
                        super.onStart();

                    }

                    @Override
                    public void onNext(T t) {
                        if (onNext != null) {
                            try {
                                onNext.accept(t);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!finishReload[0]) {
//                            reloadableFrameLayout.finishReload();
//                            reloadableFrameLayout.showContent();
                            finishReload[0] = true;
                        }
                    }

                    @Override
                    protected void onFail(NetError error) {
                        if (onError != null) {
                            try {
                                BusProvider.getBus().post(error);
                                onError.accept(error);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!finishReload[0]) {
//                            reloadableFrameLayout.needReload(getDisplayMessage(error, true));
                            reloadableFrameLayout.showError();
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (onComplete != null) {
                            try {
                                onComplete.run();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!finishReload[0]) {
//                            reloadableFrameLayout.finishReload();
//                            reloadableFrameLayout.showError();
                        }
                    }
                });
    }

    public static <T extends IModel> void subscribeWithReload(final XReloadableRecyclerContentLayout reloadableLayout,
                                                              final Flowable<T> Flowable, final FlowableTransformer transformer, final Consumer<T> onNext, final Consumer<NetError> onError,
                                                              final Action onComplete, final boolean finishWhenFirstOnNext) {
        if (reloadableLayout == null) return;

        if (finishWhenFirstOnNext) {
            reloadableLayout.showLoading();
        }
        reloadableLayout.setOnReloadListener(new XReloadableRecyclerContentLayout.OnReloadListener() {
            @Override
            public void onReload(XReloadableRecyclerContentLayout reloadableFrameLayout) {
                subscribeWithReload(reloadableFrameLayout, Flowable, transformer, onNext, onError, onComplete,
                        finishWhenFirstOnNext);
            }
        });

        final boolean[] finishReload = new boolean[]{false};

        Flowable
                .compose(XApi.<T>getApiTransformer())
                .compose(XApi.<T>getScheduler())
                .compose(transformer)
                .subscribe(new ApiSubscriber<T>() {

                    @Override
                    protected void onStart() {
                        super.onStart();

                    }

                    @Override
                    public void onNext(T t) {
                        if (onNext != null) {
                            try {
                                onNext.accept(t);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!finishReload[0]) {
//                            reloadableFrameLayout.finishReload();
                            finishReload[0] = true;
//                            HttpResultModel resultModel = (HttpResultModel) t;
//                            if (resultModel.isSucceful()) {
//                                if (!Kits.Empty.check(resultModel.data) && resultModel.data instanceof List && ((List) resultModel.data).size() != 0) {
//                                    reloadableLayout.showEmpty();
//                                } else {
//                                    reloadableLayout.showContent();
//                                }
//                            }
                        }
                        reloadableLayout.refreshState(false);
                    }

                    @Override
                    protected void onFail(NetError error) {
                        if (onError != null) {
                            try {
                                BusProvider.getBus().post(error);
                                onError.accept(error);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!finishReload[0]) {
//                            reloadableFrameLayout.needReload(getDisplayMessage(error, true));
                            reloadableLayout.showError();
                        }
                        reloadableLayout.refreshState(false);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (onComplete != null) {
                            try {
                                onComplete.run();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!finishReload[0]) {
//                            reloadableFrameLayout.finishReload();
//                            reloadableLayout.showError();
                        }
                    }
                });
    }
}
