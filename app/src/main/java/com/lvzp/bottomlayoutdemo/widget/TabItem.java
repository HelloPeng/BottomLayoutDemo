package com.lvzp.bottomlayoutdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvzp.bottomlayoutdemo.R;


/**
 * 作者：吕振鹏
 * 创建时间：08月22日
 * 时间：16:56
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */
public class TabItem extends LinearLayout {

    private static final int DEFAULT_SELECT_IMAGE_SIZE = 23;
    private static float DEFAULT_SELECT_TEXT_SIZE = 11.5f;

    private ImageView mSelectedImage;
    private ImageView mUnselectedImage;
    private int mSelectImageSize;
    private TextView mTitle;


    public TabItem(Context context) {
        this(context, null);
    }

    public TabItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initViews(context);
    }

    public TabItem setSelectedImageRes(int imageRes) {
        mSelectedImage.setImageResource(imageRes);
        return this;
    }

    public TabItem setUnSelectedImageRes(int imageRes) {
        mUnselectedImage.setImageResource(imageRes);
        return this;
    }

    public TabItem setTitleText(String titleText) {
        mTitle.setText(titleText);
        return this;
    }

    public TabItem setSelectImageSize(int imageSize) {


        LayoutParams lp = (LayoutParams) mSelectedImage.getLayoutParams();
        lp.width = getDip(imageSize);
        lp.height = getDip(imageSize);

        mSelectedImage.setLayoutParams(lp);
        mUnselectedImage.setLayoutParams(lp);

        return this;
    }

    public ImageView getSelectedImage() {
        return mSelectedImage;
    }

    public ImageView getUnselectedImage() {
        return mUnselectedImage;
    }

    public TextView getTitleTextView() {
        return mTitle;
    }

    public String getTitleText() {
        return mTitle.getText().toString();
    }

    /////////////////////////////////////////内部处理方法///////////////////////////////////////////

    /**
     * 初始化内部子View的属性
     *
     * @param context
     */
    private void initViews(Context context) {

        LayoutParams lp = new LayoutParams(getDip(DEFAULT_SELECT_IMAGE_SIZE), getDip(DEFAULT_SELECT_IMAGE_SIZE));
        mSelectedImage = new ImageView(context);
        mUnselectedImage = new ImageView(context);
        mSelectedImage.setLayoutParams(lp);
        mSelectedImage.setVisibility(GONE);
        mUnselectedImage.setLayoutParams(lp);

        mTitle = new TextView(context);
        LayoutParams textLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTitle.setSingleLine();
        textLp.setMargins(0, getDip(3), 0, 0);
        mTitle.setLayoutParams(textLp);
        mTitle.setTextSize(DEFAULT_SELECT_TEXT_SIZE);

        addView(mSelectedImage);
        addView(mUnselectedImage);
        addView(mTitle);

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


    private int getDip(int px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
    }

}
