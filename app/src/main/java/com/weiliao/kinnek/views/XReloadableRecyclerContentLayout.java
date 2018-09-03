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

import cn.droidlover.xrecyclerview.XRecyclerAdapter;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;


public class XReloadableRecyclerContentLayout extends XRecyclerContentLayout {

    private OnReloadListener onReloadListener;

    public XReloadableRecyclerContentLayout(Context context) {
        this(context, null);
    }

    public XReloadableRecyclerContentLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XReloadableRecyclerContentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface OnReloadListener {
        /**
         * user click reload button will invoke this method
         * when you reload success, use {@link #} to hide reload ui
         *
         * @param reloadableFrameLayout this
         */
        void onReload(XReloadableRecyclerContentLayout reloadableFrameLayout);
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
    protected void onFinishInflate() {
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
        //        for (int index = 0; index < getChildCount(); index++) {
//            getChildAt(index).setVisibility(GONE);
//        }
//        setDisplayState(STATE_CONTENT);
        registerStateChangeListener(null);
    }

    @Override
    public void setDisplayState(int displayState) {
        super.setDisplayState(displayState);
        XRecyclerAdapter adapter = getRecyclerView().getAdapter();
        if (adapter != null && adapter.getItemCount() > 1) {
            super.setDisplayState(STATE_CONTENT);
            return;
        }
    }

    @Override
    public void showEmpty() {
        if (null != getEmptyView()) {
            getEmptyView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (XReloadableRecyclerContentLayout.this.onReloadListener != null) {
//                        if (null == getLoadingView()) {
//                            loadingView(View.inflate(getContext(), R.layout.view_loading, null));
//                        }
//                        if (null == getEmptyView()) {
//                            emptyView(View.inflate(getContext(), R.layout.view_empty_state, null));
//                        }
                        showLoading();
                        XReloadableRecyclerContentLayout.this.onReloadListener.onReload(XReloadableRecyclerContentLayout.this);
                    }
                }
            });
        }
        super.showEmpty();
    }

    @Override
    public void showError() {
        if (null != getErrorView()) {
            getErrorView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (XReloadableRecyclerContentLayout.this.onReloadListener != null) {
//                        if (null == getLoadingView()) {
//                            loadingView(View.inflate(getContext(), R.layout.view_loading, null));
//                        }
//                        if (null == getEmptyView()) {
//                            emptyView(View.inflate(getContext(), R.layout.view_empty_state, null));
//                        }
                        showLoading();
                        XReloadableRecyclerContentLayout.this.onReloadListener.onReload(XReloadableRecyclerContentLayout.this);
                    }
                }
            });
        }
        super.showError();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
//        SavedState savedState = (SavedState) state;
//        super.onRestoreInstanceState(savedState.getSuperState());
//        this.displayState = savedState.state;
//        setDisplayState(this.displayState);
//        showLoading();
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
