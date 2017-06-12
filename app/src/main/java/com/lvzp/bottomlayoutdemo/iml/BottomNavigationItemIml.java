package com.lvzp.bottomlayoutdemo.iml;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvzp.bottomlayoutdemo.R;
import com.lvzp.bottomlayoutdemo.widget.BottomNavigationItem;

/**
 * Created by 860617003 on 2017/6/12.
 */

public class BottomNavigationItemIml extends LinearLayout implements BottomNavigationItem {

    private static final int DEFAULT_SELECT_IMAGE_SIZE = 23;
    private static float DEFAULT_SELECT_TEXT_SIZE = 11.5f;

    private int mPosition;

    private ImageView mIvIcon;
    private TextView mTvTitle;

    public BottomNavigationItemIml(Context context) {
        this(context, null);
    }

    public BottomNavigationItemIml(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationItemIml(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initViews(context);
    }

    /**
     * 初始化一些控件自身的属性
     */
    private void init() {
        setOrientation(VERTICAL);
        setBackgroundResource(R.drawable.item_click);
        LayoutParams lp = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.weight = 1;
        setPadding(0, getDip(5), 0, 0);
        setGravity(Gravity.CENTER);
        setLayoutParams(lp);
    }

    /**
     * 初始化内部子View的属性
     *
     * @param context
     */
    private void initViews(Context context) {

        LayoutParams lp = new LayoutParams(getDip(DEFAULT_SELECT_IMAGE_SIZE), getDip(DEFAULT_SELECT_IMAGE_SIZE));
        mIvIcon = new ImageView(context);
        mIvIcon.setLayoutParams(lp);

        mTvTitle = new TextView(context);
        LayoutParams textLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTvTitle.setSingleLine();
        textLp.setMargins(0, getDip(3), 0, 0);
        mTvTitle.setLayoutParams(textLp);
        mTvTitle.setTextSize(DEFAULT_SELECT_TEXT_SIZE);

        addView(mIvIcon);
        addView(mTvTitle);

    }

    /**
     * 获取显示图标的View
     *
     * @return
     */
    @Override
    public ImageView getIconView() {
        return mIvIcon;
    }

    /**
     * 获取标题文字的View
     *
     * @return
     */
    @Override
    public TextView getTitleView() {
        return mTvTitle;
    }

    @Override
    public int getPosition() {
        return mPosition;
    }

    @Override
    public void setPosition(int position) {
        mPosition = position;
    }

    private int getDip(int px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
    }
}
