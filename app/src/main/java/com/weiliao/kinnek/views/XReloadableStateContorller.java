package com.weiliao.kinnek.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.weiliao.kinnek.R;

import cn.droidlover.xstatecontroller.XStateController;


public class XReloadableStateContorller extends XStateController {

    private OnReloadListener onReloadListener;

    public interface OnReloadListener {
        /**
         * user click reload button will invoke this method
         * when you reload success, use {@link #} to hide reload ui
         *
         * @param reloadableFrameLayout this
         */
        void onReload(XReloadableStateContorller reloadableFrameLayout);
    }

    public XReloadableStateContorller(Context context) {
        this(context, null);
    }

    public XReloadableStateContorller(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        if (null == getLoadingView()) {
            loadingView(View.inflate(getContext(), R.layout.view_loading, null));
        }
        if (null == getErrorView()) {
//            addView(View.inflate(getContext(), R.layout.view_error_state,null));
            errorView(View.inflate(getContext(), R.layout.view_error_state, null));
        }
        if (null == getEmptyView()) {
//            addView(View.inflate(getContext(), R.layout.view_empty_state,null));
            emptyView(View.inflate(getContext(), R.layout.view_empty_state, null));
        }

//        if (getLoadingView() != null) {
//            setDisplayState(STATE_LOADING);
//        } else {
//        setDisplayState(STATE_CONTENT);
//        }
        registerStateChangeListener(null);
    }


    @Override
    public void showError() {
        if (null != getErrorView()) {
            getErrorView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (XReloadableStateContorller.this.onReloadListener != null) {
                        if (null == getLoadingView()) {
                            loadingView(View.inflate(getContext(), R.layout.view_loading, null));
                        }
//                        if (null == getEmptyView()) {
//                            emptyView(View.inflate(getContext(), R.layout.view_empty_state, null));
//                        }
                        showLoading();
                        XReloadableStateContorller.this.onReloadListener.onReload(XReloadableStateContorller.this);
                    }
                }
            });
        }
        super.showError();
    }

    @Override
    public void showEmpty() {
        if (null != getEmptyView()) {
            getEmptyView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (XReloadableStateContorller.this.onReloadListener != null) {
                        if (null == getLoadingView()) {
                            loadingView(View.inflate(getContext(), R.layout.view_loading, null));
                        }
//                        if (null == getEmptyView()) {
//                            emptyView(View.inflate(getContext(), R.layout.view_empty_state, null));
//                        }
                        showLoading();
                        XReloadableStateContorller.this.onReloadListener.onReload(XReloadableStateContorller.this);
                    }
                }
            });
        } else {
            return;
        }
        super.showEmpty();
    }

    /**
     * set a onReloadListener to listener reload button click event
     *
     * @param onReloadListener see #OnReloadListener
     */
    public void setOnReloadListener(OnReloadListener onReloadListener) {
        this.onReloadListener = onReloadListener;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
//        SavedState savedState = (SavedState) state;
//        super.onRestoreInstanceState(savedState.getSuperState());
//        this.displayState = savedState.state;
//        setDisplayState(this.displayState);
        showLoading();
    }

    @Override
    public OnStateChangeListener getStateChangeListener() {
        return super.getStateChangeListener();
    }

    @Override
    public void registerStateChangeListener(OnStateChangeListener stateChangeListener) {
        super.registerStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(int oldState, int newState) {

            }

            @Override
            public void animationState(final View exitView, final View enterView) {
                AnimatorSet set = new AnimatorSet();
                final ObjectAnimator enter = ObjectAnimator.ofFloat(enterView, View.ALPHA, 1f);
                ObjectAnimator exit = ObjectAnimator.ofFloat(exitView, View.ALPHA, 0f);
                set.playTogether(enter, exit);
                set.setDuration(300);
                set.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        enterView.setVisibility(VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        getLoadingView().setVisibility(GONE);
                        exitView.setAlpha(1);
                        exitView.setVisibility(GONE);
                        checkView(enterView);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                set.start();
            }

            private void checkView(View enterView) {
                int visibleChild = 0;
                FrameLayout parent = (FrameLayout) enterView.getParent();
                int childCount = parent.getChildCount();
                for (int index = 0; index < childCount; index++) {
                    if (parent.getChildAt(index).getVisibility() == VISIBLE) {
                        visibleChild++;
                    }
                }
                if (visibleChild < 1) {
                    enterView.setVisibility(VISIBLE);
                    enterView.setAlpha(1);
                }
            }
        });

    }
}
