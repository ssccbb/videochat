package com.weiliao.kinnek.views.mine;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.weiliao.kinnek.R;

/**
 * Created by Sun on 15/12/2017.
 *
 * sung/27/03/2018 mTVNumber更改为可编辑EditText 拓展编辑功能
 */

public class SelectItemView extends BaseAssembleLinearLayout {

    private ImageView mIVIcon;
    private TextView mTVNotice;
    private TextView mTVNumber;

    public SelectItemView(Context context) {
        super(context);
    }

    public SelectItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SelectItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_select_item;
    }

    @Override
    protected void init(AttributeSet attributeSet) {
        mIVIcon = V.get(this, R.id.iv_icon);
        mTVNotice = V.get(this, R.id.tv_notice);
        mTVNumber = V.get(this, R.id.tv_number);

        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.SelectItemView);
        if (typedArray != null) {
            boolean visible = typedArray.getBoolean(R.styleable.SelectItemView_head_icon_visible, true);
            int icon = typedArray.getResourceId(R.styleable.SelectItemView_icon, R.mipmap.ic_launcher);
            String notice = typedArray.getString(R.styleable.SelectItemView_notice);
            initData(icon, notice, visible);
            typedArray.recycle();
        }
    }

    /**
     * @param iconResId 左边大图标的Icon
     * @param notice    左边主要的提示
     */
    public void initData(int iconResId, String notice, boolean visible) {
        if (!visible){
            mIVIcon.setVisibility(GONE);
        }
        mIVIcon.setImageResource(iconResId);
        mTVNotice.setText(notice);
    }

    /**
     * 不显示左边图标
     * */
    public void setLeftImgGone(){
        mIVIcon.setVisibility(GONE);
    }

    /********************              右边可编辑text拓展             ******************/

    /**
     * 获取右边文字
     * */
    public String getDataNumber() {
        return mTVNumber.getText().toString().isEmpty() ? "" : mTVNumber.getText().toString();
    }

    /**
     * @param number 右边的小提示，默认为空则不出现
     */

    public void setDataNumber(String number) {
        mTVNumber.setVisibility(VISIBLE);
        if (number != null && number.length() > 0) {
            mTVNumber.setText(number);
        } else {
            mTVNumber.setText("  ");
            //mTVNumber.setVisibility(GONE);
        }
    }

    /********************              右边可编辑text拓展             ******************/
}
