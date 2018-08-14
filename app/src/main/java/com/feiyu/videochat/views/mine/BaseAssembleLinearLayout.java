package com.feiyu.videochat.views.mine;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * Created by Sun on 15/12/2017.
 */

public abstract class BaseAssembleLinearLayout extends LinearLayout{

    private Context mContext;
    private LayoutInflater mInflater;

    public BaseAssembleLinearLayout(Context context) {
        this(context, null);
    }

    public BaseAssembleLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseAssembleLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflateView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseAssembleLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inflateView(context, attrs);
    }

    private void inflateView(Context context, AttributeSet attributeSet) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        try {
            if (getLayoutId() != -1) {
                mInflater.inflate(getLayoutId(), this, true);
                init(attributeSet);
            }
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
    }

    protected abstract int getLayoutId();

    protected abstract void init(AttributeSet attributeSet);
}
