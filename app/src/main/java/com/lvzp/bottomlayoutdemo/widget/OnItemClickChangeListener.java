package com.lvzp.bottomlayoutdemo.widget;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 860617003 on 2017/6/12.
 */

public interface OnItemClickChangeListener {

    /**
     * 当用户在刚刚点击条目时促发的点击事件，这个时候如果返回 true 就说明事件已经被拦截，
     * 就不会在继续下面的操作，也就是说，页面不会切换底部的控件也不会切换
     *
     * @param bottomLayout 底部的布局控件
     * @param item         具体点击的条目
     * @param position     点击的位置
     * @return 是否要拦截点击事件，如果返回真，就会被拦截下面进行切换的操作，也就是说无论你怎么点都不会执行切换操作
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
