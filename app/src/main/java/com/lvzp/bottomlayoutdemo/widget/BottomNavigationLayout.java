package com.lvzp.bottomlayoutdemo.widget;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lvzp.bottomlayoutdemo.R;
import com.lvzp.bottomlayoutdemo.iml.BottomNavigationItemIml;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 860617003 on 2017/6/12.
 */
@SuppressLint("RestrictedApi")
public class BottomNavigationLayout extends LinearLayout implements View.OnClickListener {

    private static final int[] SELECT_STATE_SET = {android.R.attr.state_selected};//选中状态的集合颜色
    private static final int[] UN_SELECT_STATE_SET = {-android.R.attr.state_selected};//未选中状态的集合颜色
    private static final int DEFAULT_VALUE_HEIGHT = 45;

    /**
     * @hide
     */
    @IntDef({WEIGHT, WRAP_CONTENT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LayoutItemWidthMode {
    }

    public static final int WEIGHT = 1;
    public static final int WRAP_CONTENT = 2;

    private List<BottomNavigationItem> mBottomItemList;
    private List<Fragment> mReplaceFragmentList;
    private List<MenuItemImpl> mMenuItemList;
    private ColorStateList mSelectColor;
    private boolean isGradient = true;//是否为渐变色

    private ViewPager mViewPager;
    private boolean isWithViewPager;
    private Fragment mDisplayFragment;
    private FragmentManager mFragmentManager;

    private BottomNavigationItem mSelectItemTab;

    private ViewPager.OnPageChangeListener mPageChangeListener;
    private OnItemClickChangeListener mOnItemClickChangeListener;

    private int mLayoutItemWidthMode = WEIGHT;
    @IdRes
    private int mReplaceLayoutId = -1;

    private boolean isClickSelect;

    public BottomNavigationLayout(Context context) {
        this(context, null);
    }

    public BottomNavigationLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (context instanceof FragmentActivity) {
            mFragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        }
        init(attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
    }

    public void bindingReplaceFragmentList(List<Fragment> replaceFragmentList) {
        this.mReplaceFragmentList = replaceFragmentList;
        updateData();
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

    /**
     * 替换自定义的Item
     *
     * @return
     */
    public BottomNavigationItem replaceBottomNavigationItem() {
        return null;
    }

    /**
     * 设置监听器
     *
     * @param onItemClickChangeListener
     */
    public void setOnItemClickChangeListener(OnItemClickChangeListener onItemClickChangeListener) {
        this.mOnItemClickChangeListener = onItemClickChangeListener;
    }


    public void setupWhiteViewPager(ViewPager viewPager) {
        if (mViewPager != null && mPageChangeListener != null) {
            mViewPager.removeOnPageChangeListener(mPageChangeListener);
        }
        if (viewPager != null) {
            isWithViewPager = true;
            mViewPager = viewPager;
            if (mPageChangeListener == null)
                mPageChangeListener = new BottomNavigationPagerChangeListener(this);
            mViewPager.addOnPageChangeListener(mPageChangeListener);
        }
    }

    public BottomNavigationItem getItemTabForPosition(int position) {
        return mBottomItemList.get(position);
    }

    public BottomNavigationItem getSelectItemTab() {
        return mSelectItemTab;
    }

    public void setSelectTab(BottomNavigationItem itemTab) {
        BottomNavigationItem currentTab = mSelectItemTab;
        int position = itemTab.getPosition();
        if (currentTab == itemTab) {
            if (mOnItemClickChangeListener != null) {
                mOnItemClickChangeListener.onItemReselected(this, itemTab.getLayoutView(), position);
            }
        } else {
            if (isWithViewPager && isClickSelect) {
                mViewPager.setCurrentItem(position);
            } else {
                if (mReplaceFragmentList != null) {
                    Fragment currentFragment = mReplaceFragmentList.get(position);
         /*   mFragmentManager
                    .beginTransaction()
                    .add(mReplaceContainerViewId, fragment, fragment.getClass().getSimpleName())
                    .hide(fragment)
                    .commit();*/
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    if (!currentFragment.isAdded()) {
                        transaction.add(mReplaceLayoutId, currentFragment, currentFragment.getClass().getSimpleName());
                    }
                    transaction.show(currentFragment);
                    if (mDisplayFragment != null) transaction.hide(mDisplayFragment);
                    transaction.commit();
                    mDisplayFragment = currentFragment;
                }

            }
            selectTab(itemTab);
            if (mOnItemClickChangeListener != null)
                mOnItemClickChangeListener.onItemClickChanger(this, mSelectItemTab.getLayoutView(), position);
        }
    }

    /**
     * 选中某个Item
     *
     * @param itemTab
     */
    private void selectTab(BottomNavigationItem itemTab) {
        if (mSelectColor != null) {
            mSelectItemTab = itemTab;
            resetItemStatus(itemTab);
            int selectColor = mSelectColor.getColorForState(SELECT_STATE_SET, Color.BLACK);
            updateItemStatus(itemTab, selectColor);
        }
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BottomNavigationLayout, defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            switch (index) {
                case R.styleable.BottomNavigationLayout_layoutMenu:
                    int menuRes = typedArray.getResourceId(index, -1);
                    MenuBuilder builder = new MenuBuilder(getContext());
                    new SupportMenuInflater(getContext()).inflate(menuRes, builder);
                    mMenuItemList = builder.getVisibleItems();
                    break;
                case R.styleable.BottomNavigationLayout_selectColor:
                    mSelectColor = typedArray.getColorStateList(index);
                    break;
                case R.styleable.BottomNavigationLayout_layoutElevation:
                    ViewCompat.setElevation(this, typedArray.getDimensionPixelSize(
                            R.styleable.BottomNavigationLayout_layoutElevation, 0));
                    break;
                case R.styleable.BottomNavigationLayout_layoutItemWidthMode:
                    mLayoutItemWidthMode = typedArray.getInt(index, mLayoutItemWidthMode);
                    break;
                case R.styleable.BottomNavigationLayout_replaceLayoutId:
                    mReplaceLayoutId = typedArray.getResourceId(index, mReplaceLayoutId);
                    break;
            }
        }
        typedArray.recycle();
        if (mMenuItemList != null)
            createItemTabs();
    }

    private void createItemTabs() {
        mBottomItemList = new ArrayList<>();
        int size = mMenuItemList.size();
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (mLayoutItemWidthMode == WEIGHT) {
            lp.weight = 1;
        }
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
            itemTab.setLayoutParams(lp);
            itemTab.setOnClickListener(this);

            addView(itemTab.getLayoutView(), i);
            mBottomItemList.add(itemTab);
        }
        resetItemStatus();
    }

    private void updateItemStatus(BottomNavigationItem itemTab, int selectColor) {
        Drawable drawable = itemTab.getIconView().getDrawable();
        if (drawable != null) {
            itemTab.getIconView().setImageDrawable(tintDrawable(drawable, ColorStateList.valueOf(selectColor)));
        }
        itemTab.getTitleView().setTextColor(selectColor);
    }

    private void updateData() {
        if (mBottomItemList != null)
            setSelectTab(mBottomItemList.get(0));
    }

    private void resetItemStatus() {
        resetItemStatus(null);
    }

    private void resetItemStatus(BottomNavigationItem selectItemTab) {
        int unSelectColor = mSelectColor.getColorForState(UN_SELECT_STATE_SET, Color.GRAY);
        for (BottomNavigationItem bottomNavigationItem : mBottomItemList) {
            if (selectItemTab != null && bottomNavigationItem.getId() == selectItemTab.getId())
                continue;
            updateItemStatus(bottomNavigationItem, unSelectColor);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_VALUE_HEIGHT, getResources().getDisplayMetrics()));
    }


    @Override
    public void onClick(View v) {
        if (v instanceof BottomNavigationItem) {
            isClickSelect = true;
            if (mOnItemClickChangeListener != null && !mOnItemClickChangeListener.onItemClick(this, v, ((BottomNavigationItem) v).getPosition()))
                setSelectTab((BottomNavigationItem) v);
            else
                setSelectTab((BottomNavigationItem) v);
        }
    }

    private static class BottomNavigationPagerChangeListener implements ViewPager.OnPageChangeListener {

        private WeakReference<BottomNavigationLayout> mBottomLayout;

        public BottomNavigationPagerChangeListener(BottomNavigationLayout bottomNavigationLayout) {
            this.mBottomLayout = new WeakReference<BottomNavigationLayout>(bottomNavigationLayout);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mBottomLayout.get().isGradient) {
                int endColor = mBottomLayout.get().mSelectColor.getColorForState(BottomNavigationLayout.SELECT_STATE_SET, Color.BLACK);
                int startColor = mBottomLayout.get().mSelectColor.getColorForState(BottomNavigationLayout.UN_SELECT_STATE_SET, Color.GRAY);
                ArgbEvaluator evaluator = new ArgbEvaluator();
                int startColors = (int) evaluator.evaluate(positionOffset, endColor, startColor);
                int endColors = (int) evaluator.evaluate(1 - positionOffset, startColor, endColor);
                List<MenuItemImpl> mMenuItemList = mBottomLayout.get().mMenuItemList;
                if (mMenuItemList != null) {
                    BottomNavigationItem startItem = mBottomLayout.get().mBottomItemList.get(position);
                    BottomNavigationItem endItem;
                    if (position + 1 == mMenuItemList.size()) {
                        endItem = mBottomLayout.get().mBottomItemList.get(position - 1);
                    } else {
                        endItem = mBottomLayout.get().mBottomItemList.get(position + 1);
                    }
                    mBottomLayout.get().updateItemStatus(startItem, startColors);
                    mBottomLayout.get().updateItemStatus(endItem, endColors);
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            mBottomLayout.get().isClickSelect = false;
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
