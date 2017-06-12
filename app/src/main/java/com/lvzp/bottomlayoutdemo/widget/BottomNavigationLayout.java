package com.lvzp.bottomlayoutdemo.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.lvzp.bottomlayoutdemo.R;
import com.lvzp.bottomlayoutdemo.iml.BottomNavigationItemIml;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 860617003 on 2017/6/12.
 */
@SuppressLint("RestrictedApi")
public class BottomNavigationLayout extends LinearLayout {

    private static final int[] SELECT_STATE_SET = {android.R.attr.state_selected};//选中状态的集合颜色
    private static final int[] UNSELECT_STATE_SET = {-android.R.attr.state_selected};//未选中状态的集合颜色

    private List<BottomNavigationItem> mBottomItemList;
    private List<Fragment> mReplaceFragmentList;
    private List<MenuItemImpl> mMenuItemList;
    private ColorStateList mSelectColor;
    private boolean isGradient = true;//是否为渐变色

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    private BottomNavigationItem mSelectItemTab;

    private ViewPager.OnPageChangeListener mPageChangeListener;

    public BottomNavigationLayout(Context context) {
        this(context, null);
    }

    public BottomNavigationLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    /**
     * 获取Item是否为渐变设
     *
     * @return
     */
    public boolean isGradient() {
        return isGradient;
    }

    /**
     * 设置Item切换的时候是否支持渐变
     *
     * @param gradient
     */
    public void setGradient(boolean gradient) {
        isGradient = gradient;
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BottomNavigationLayout, defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            switch (index) {
                case R.styleable.BottomNavigationLayout_layoutMenu:
                    int menuRes = typedArray.getInt(index, -1);
                    MenuBuilder builder = new MenuBuilder(getContext());
                    new SupportMenuInflater(getContext()).inflate(menuRes, builder);
                    mMenuItemList = builder.getVisibleItems();
                    break;
                case R.styleable.BottomNavigationLayout_selectColor:
                    mSelectColor = typedArray.getColorStateList(index);
                    break;
            }
        }
        typedArray.recycle();
        createItemTabs();
    }

    private void createItemTabs() {
        mBottomItemList = new ArrayList<>();
        int size = mMenuItemList.size();
        for (int i = 0; i < size; i++) {
            BottomNavigationItem itemTab = replaceBottomNavigationItem();
            if (itemTab == null)
                itemTab = new BottomNavigationItemIml(getContext());
            MenuItemImpl menuItem = mMenuItemList.get(i);
            itemTab.setPosition(i);
            if (menuItem.getIcon() != null)
                itemTab.getIconView().setImageDrawable(menuItem.getIcon());
            if (menuItem.getItemId() != -1)
                itemTab.setId(menuItem.getItemId());
            if (menuItem.getTitle() != null)
                itemTab.getTitleView().setText(menuItem.getTitle());
            mBottomItemList.add(itemTab);
        }
    }

    /**
     * 替换自定义的Item
     *
     * @return
     */
    public BottomNavigationItem replaceBottomNavigationItem() {
        return null;
    }

    public void setSelectTab(BottomNavigationItem itemTab) {
        BottomNavigationItem currentTab = mSelectItemTab;
        if (currentTab == itemTab) {

        } else {
            if (mSelectColor != null) {
                int selectColor;
                int unSelectColor;
                selectColor = mSelectColor.getColorForState(SELECT_STATE_SET,Color.BLACK);
                unSelectColor = mSelectColor.getColorForState(UNSELECT_STATE_SET,Color.GRAY);
                itemTab.getTitleView().setTextColor(selectColor);
            }


        }
    }

    public void setupWhiteViewPager(ViewPager viewPager) {
        if (mViewPager != null && mPageChangeListener != null) {
            mViewPager.removeOnPageChangeListener(mPageChangeListener);
        }
        if (viewPager != null) {
            mViewPager = viewPager;
            if (mPageChangeListener == null)
                mPageChangeListener = new BottomNavigationPagerChangeListener(this);
            mViewPager.addOnPageChangeListener(mPageChangeListener);
        }
    }

    private static class BottomNavigationPagerChangeListener implements ViewPager.OnPageChangeListener {

        private WeakReference<BottomNavigationLayout> mBottomLayout;

        public BottomNavigationPagerChangeListener(BottomNavigationLayout bottomNavigationLayout) {
            this.mBottomLayout = new WeakReference<BottomNavigationLayout>(bottomNavigationLayout);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            BottomNavigationItem item = mBottomLayout.get().mBottomItemList.get(position);
            mBottomLayout.get().setSelectTab(item);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }

    //tintDrawable(drawable, ColorStateList.valueOf(Color))
    private Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }
}
