package com.weiliao.kinnek.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiliao.kinnek.R;
import com.wang.avi.AVLoadingIndicatorView;


public class ReloadableFrameLayout extends FrameLayout {
    private FrameLayout contentContainer;

    private View customViewContainer;
    private ImageView customImageView;
    private TextView customTextView;

    //    private ProgressBar loadingProgressBar;
    private AVLoadingIndicatorView loadingProgressBar;
    private TextView errorText;
    private OnReloadListener onReloadListener;
    private boolean isChildrenHided = false;

    private Drawable reloadImage;
    private Drawable emptyImage;
    private String reloadText;
    private String emptyText;

    public static final int STATE_NORMAL = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_NEED_RELOAD = 2;
    public static final int STATE_EMPTY = 3;
    public static final int STATE_ERROR = 4;

    private int state = STATE_NORMAL;

    public interface OnReloadListener {
        /**
         * user click reload button will invoke this method
         * when you reload success, use {@link #finishReload()} to hide reload ui
         *
         * @param reloadableFrameLayout this
         */
        void onReload(ReloadableFrameLayout reloadableFrameLayout);
    }

    public ReloadableFrameLayout(Context context) {
        this(context, null);
    }

    public ReloadableFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ReloadableFrameLayout);
        reloadImage = typedArray.getDrawable(R.styleable.ReloadableFrameLayout_reloadImage);
        reloadText = typedArray.getString(R.styleable.ReloadableFrameLayout_reloadText);
        emptyImage = typedArray.getDrawable(R.styleable.ReloadableFrameLayout_emptyImage);
        emptyText = typedArray.getString(R.styleable.ReloadableFrameLayout_emptyText);
        typedArray.recycle();
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.view_common_reloadable_layout, this, true);
        contentContainer = (FrameLayout) contentView.findViewById(R.id.content_container);
//        loadingProgressBar = (AVLoadingIndicatorView) contentView.findViewById(R.id.loading_progress_bar);
        loadingProgressBar = (AVLoadingIndicatorView) contentView.findViewById(R.id.loading_avloading_bar);
        errorText = (TextView) contentView.findViewById(R.id.error_text);
        customViewContainer = contentView.findViewById(R.id.custom_view_container);
        customImageView = (ImageView) customViewContainer.findViewById(R.id.custom_image_view);
        customTextView = (TextView) customViewContainer.findViewById(R.id.custom_text_view);
        customViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ReloadableFrameLayout.this.onReloadListener != null) {
                    showLoadingView();
                    ReloadableFrameLayout.this.onReloadListener.onReload(ReloadableFrameLayout.this);
                }
            }
        });
    }

    /**
     * get current state of ReloadableFrameLayout
     *
     * @return state
     * @see #STATE_EMPTY
     * @see #STATE_ERROR
     * @see #STATE_LOADING
     * @see #STATE_NORMAL
     * @see #STATE_NEED_RELOAD
     */
    public int getState() {
        return state;
    }

    /**
     * set a onReloadListener to listener reload button click event
     *
     * @param onReloadListener see #OnReloadListener
     */
    public void setOnReloadListener(OnReloadListener onReloadListener) {
        this.onReloadListener = onReloadListener;
    }

    /**
     * show reload view and hide content view
     */
    public void needReload(String error) {
        state = STATE_NEED_RELOAD;

        customViewContainer.setVisibility(View.VISIBLE);
        customImageView.setImageDrawable(reloadImage != null ? reloadImage :
                ResourcesCompat.getDrawable(getResources(), R.mipmap.reload, null));
        customTextView.setText(reloadText != null ? reloadText : "网络出现问题，请点击重试");
        errorText.setText(error);
        hideLoadingView();
        hideChildViews();
    }

    /**
     * hide loading view and show content view
     */
    public void finishReload() {
        if (state != STATE_EMPTY) {
            state = STATE_NORMAL;

            customViewContainer.setVisibility(INVISIBLE);
            hideLoadingView();
            showChildViews();
        }
    }

    /**
     * show empty ui
     */
    public void showEmpty() {
        showEmpty(emptyImage, emptyText);
    }

    /**
     * show empty ui
     *
     * @param emptyImage the image to show
     * @param emptyText  the text to show
     */
    public void showEmpty(Drawable emptyImage, String emptyText) {
        state = STATE_EMPTY;

        removePreCustomEmptyView();

        customViewContainer.setVisibility(View.VISIBLE);
        customImageView.setImageDrawable(emptyImage);
        customTextView.setText(emptyText != null ?
                emptyText : "暂无相关数据哦");
        errorText.setText("");
        hideLoadingView(false);
        hideChildViews();
    }

    /**
     * show empty ui
     *
     * @param empty the view to show
     */
    public void showEmpty(View empty) {
        state = STATE_EMPTY;

        removePreCustomEmptyView();

        if (empty.getLayoutParams() == null) {
            empty.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        empty.setId(R.id.reloadable_frame_layout_custom_empty_view);
        customViewContainer.setVisibility(INVISIBLE);
        contentContainer.addView(empty);
    }

    private void removePreCustomEmptyView() {
        View preEmptyView = contentContainer.findViewById(R.id.reloadable_frame_layout_custom_empty_view);
        if (preEmptyView != null) {
            contentContainer.removeView(preEmptyView);
        }
    }

    /**
     * show loading view and hide children
     */
    public synchronized void showLoadingView() {
        state = STATE_LOADING;

        loadingProgressBar.setVisibility(View.VISIBLE);
        customViewContainer.setVisibility(View.INVISIBLE);
        if (!isChildrenHided) {
            hideChildViews();
        }
    }

    /**
     * show children and hide loading view
     */
    public synchronized void hideLoadingView() {
        hideLoadingView(true);
    }

    public synchronized void hideLoadingView(boolean showChildView) {
        loadingProgressBar.setVisibility(View.INVISIBLE);
        if (showChildView && isChildrenHided) {
            showChildViews();
        }
    }

    private void hideChildViews() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (isReloadContainer(child)) {
                continue;
            }
            child.setVisibility(INVISIBLE);
        }
        isChildrenHided = true;
    }

    private void showChildViews() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (isReloadContainer(child)) {
                continue;
            }
            child.setVisibility(VISIBLE);
        }
        isChildrenHided = false;
    }

    private boolean isReloadContainer(View child) {
        return child.getId() == R.id.reload_frame_layout_main_container;
    }
}
