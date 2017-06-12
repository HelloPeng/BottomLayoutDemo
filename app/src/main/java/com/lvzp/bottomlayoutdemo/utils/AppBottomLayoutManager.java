package com.lvzp.bottomlayoutdemo.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lvzp.bottomlayoutdemo.widget.TabItem;


/**
 * 作者：吕振鹏
 * 创建时间：08月22日
 * 时间：15:44
 * 版本：v1.0.0
 * 类描述：用来管理底部Item和Fragment直接进行切换处理的工具类
 * 修改时间：
 */
public class AppBottomLayoutManager {

    private static final boolean DEFAULT_IS_WITH_VIEW_PAGER = false;

    private int mSelectColor;//选中时选择的颜色
    private int mUnSelectColor;//未选中时选择的颜色

    private Context mContext;
    private FragmentManager mFragmentManager;
    /**
     * 判断是否和ViewPager连用
     */
    private boolean isWithViewPager = DEFAULT_IS_WITH_VIEW_PAGER;
    private ViewPager mViewPager;

    private SparseArray<TabItem> mHomeTabItems;//用来存储底部控件的集合
    private SparseArray<Fragment> mFragments;//用来存储Fragment的集合（当和ViewPager连用的时候，就用不上了）

    private LinearLayout mBottomLayout;//底部控件
    private int mReplaceContainerViewId;//进行页面切换的内容区域id

    private TabItem mSelectedTabItem;//当前已经选中的条目对象
    private int mCurrentItemPosition = -2017;//当前选中的第几条
    private Fragment mDisplayFragment;//当前正在显示中的Fragment

    private OnItemClickChangeListener mItemClickChangeListener;
    private OnTabItemClickListener mItemClickListener;


    /**
     * 获取适配器对象
     *
     * @param context
     * @param bottomLayout
     */
    public AppBottomLayoutManager(Context context, LinearLayout bottomLayout) {
        mHomeTabItems = new SparseArray<>();
        mContext = context;
        mFragments = new SparseArray<>();
        mBottomLayout = bottomLayout;
        mFragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        mItemClickListener = new OnTabItemClickListener();
       /* mSelectColor = ContextCompat.getColor(context, R.color.colorBottomSelected);
        mUnSelectColor = ContextCompat.getColor(context, R.color.colorBottomUnSelected);*/

    }

    /**
     * 当前正在显示中的Fragment
     *
     * @return
     */
    public Fragment getDisplayFragment() {
        return mDisplayFragment;
    }

    /////////////////////////////关于和Fragment直接连用的方法//////////////////////////////////

    public int getFragmentCount() {
        return mFragments.size();
    }

    public int getCurrentPosition() {
        return mCurrentItemPosition;
    }

    /**
     * 添加具体的Fragment
     *
     * @param position 添加的具体位置
     * @param fragment 具体的Fragment实例
     */
    public void addFragment(int position, Fragment fragment) {
        mFragments.put(position, fragment);
    }

    /**
     * 设置当前选中的Item
     *
     * @param position
     */
    public void setCurrentItem(int position) {
        if (mFragments.size() == mHomeTabItems.size()) {
            if (position < mFragments.size() && position < mHomeTabItems.size()) {
                TabItem item = mHomeTabItems.get(position);
                setupItemChange(item, position);
                mCurrentItemPosition = position;
                mSelectedTabItem = item;
            }
        }
    }


    /**
     * 设置底部标签的属性
     * 这个方法适用于不结合ViewPager
     *
     * @param position        条目
     * @param selectedImage   选中时的图片
     * @param unselectedImage 为选中时的图片
     * @param title           标题
     */
    public void setTabItem(int position, int selectedImage, int unselectedImage, String title) {
        setTabItem(position, new TabItem(mContext).
                setSelectedImageRes(selectedImage).
                setUnSelectedImageRes(unselectedImage).
                setTitleText(title));
    }

    /**
     * 重写设置底部标签属性，这个方法和{@link #setTabItem(int, int, int, String)}用法一样，
     * 这里可以传入一个自己定义好的{@link TabItem}对象
     *
     * @param position
     * @param homeTabItem
     */
    public void setTabItem(int position, TabItem homeTabItem) {
        homeTabItem.setTag(position);
        homeTabItem.setOnClickListener(mItemClickListener);
        if (mBottomLayout != null) {
            mBottomLayout.addView(homeTabItem, position);
            mBottomLayout.invalidate();
        }
        mHomeTabItems.put(position, homeTabItem);
    }

    /**
     * 设置底部标签图片的尺寸
     *
     * @param itemSize
     */
    public void setTabImageSize(int itemSize) {
        for (int i = 0; i < mHomeTabItems.size(); i++) {
            TabItem item = mHomeTabItems.get(i);
            item.setSelectImageSize(itemSize);
        }
    }


    /////////////////////关于和ViewPager连用的方法///////////////////////

    public boolean isWithViewPager() {
        return isWithViewPager;
    }

    public int getSelectItemPosition() {
        return mCurrentItemPosition;
    }

    /**
     * 设置进行页面切换的内容区域
     *
     * @param containerViewId 内容区域的的ViewId
     */
    public void setContainerViewId(int containerViewId) {
        mReplaceContainerViewId = containerViewId;
    }

    public void setOnItemClickChangeListener(OnItemClickChangeListener listener) {
        mItemClickChangeListener = listener;
    }

    public void setWithViewPager(boolean isWithViewPager) {
        this.isWithViewPager = isWithViewPager;
    }

    public void setupWithViewPager(ViewPager pager) {
        mViewPager = pager;
        if (!isWithViewPager) {
            isWithViewPager = true;
        }

    }


    //////////////////////////点击监听事件/////////////////////////////////

    private class OnTabItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();

            if (mSelectedTabItem == view) {
                if (mItemClickChangeListener != null) {
                    mItemClickChangeListener.onItemReselected(mBottomLayout, view, position);
                }

            } else {

                boolean isClickOver = false;
                if (mItemClickChangeListener != null) {
                    isClickOver = mItemClickChangeListener.onItemClick(mBottomLayout, view, position);
                }
                if (isClickOver) {
                    return;
                }

                if (mFragments.size() == mHomeTabItems.size()) {

                    setCurrentItem(position);

                }

            }
        }


    }


    //////////////////////////////////内部处理方法/////////////////////////////////////


    /**
     * 当底部控件被点击时，执行的具体操作
     * 1.改变底部控件的颜色
     * 2.Fragment页面切换
     *
     * @param view
     * @param position
     */
    private void setupItemChange(View view, int position) {

        selectedTab(view);

        if (isWithViewPager) {
            mViewPager.setCurrentItem(position, false);
        } else {
            Fragment currentFragment = mFragments.get(position);
         /*   mFragmentManager
                    .beginTransaction()
                    .add(mReplaceContainerViewId, fragment, fragment.getClass().getSimpleName())
                    .hide(fragment)
                    .commit();*/
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if (!currentFragment.isAdded()) {
                transaction.add(mReplaceContainerViewId, currentFragment, currentFragment.getClass().getSimpleName());
            }
            transaction.show(currentFragment);
            if (mDisplayFragment != null) transaction.hide(mDisplayFragment);
            transaction.commit();
            mDisplayFragment = currentFragment;
        }

        if (mItemClickChangeListener != null) {
            mItemClickChangeListener.onItemClickChanger(mBottomLayout, view, position);
        }

    }

    /**
     * 设置底部控件的颜色进行改变
     *
     * @param view
     */
    private void selectedTab(View view) {

        for (int i = 0; i < mHomeTabItems.size(); i++) {
            TabItem homeTabItem = mHomeTabItems.get(i);
            homeTabItem.getSelectedImage().setVisibility(View.GONE);
            homeTabItem.getUnselectedImage().setVisibility(View.VISIBLE);
            homeTabItem.setSelected(false);
            setTextColor(homeTabItem, false);
        }

        if (view instanceof TabItem) {
            ((TabItem) view).getSelectedImage().setVisibility(View.VISIBLE);
            ((TabItem) view).getUnselectedImage().setVisibility(View.GONE);
            view.setSelected(true);
            setTextColor((TabItem) view, true);
        }


    }

    /**
     * 设置底部文字进行改变
     *
     * @param homeTabItem
     * @param isSelect
     */
    private void setTextColor(TabItem homeTabItem, boolean isSelect) {
        int textColor;
        if (isSelect) {
            textColor = mSelectColor;
        } else {
            textColor = mUnSelectColor;
        }
        homeTabItem.getTitleTextView().setTextColor(textColor);
    }


    public interface OnItemClickChangeListener {
        /**
         * 当用户在刚刚点击条目时促发的点击事件，这个时候如果返回 true 就说明事件已经被拦截，
         * 就不会在继续下面的操作，也就是说，页面不会切换底部的控件也不会切换
         *
         * @param bottomLayout 底部的布局控件
         * @param item         具体点击的条目
         * @param position     点击的位置
         * @return 如果返回真，就会被拦截下面进行切换的操作，也就是说无论你怎么点都不会执行切换操作
         */
        boolean onItemClick(ViewGroup bottomLayout, View item, int position);

        /**
         * 当页面已经完成条目的点击切换时，进行的回掉。
         *
         * @param bottomLayout
         * @param item
         * @param position
         */
        void onItemClickChanger(ViewGroup bottomLayout, View item, int position);

        /**
         * 当用户再次点击已被选中的Item时，被回掉的方法，
         * 当用户再次点击的时候并不会做切换处理
         *
         * @param bottomLayout
         * @param item
         * @param position
         */
        void onItemReselected(ViewGroup bottomLayout, View item, int position);

    }


}
