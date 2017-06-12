package com.lvzp.bottomlayoutdemo.widget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 860617003 on 2017/6/12.
 */

public interface BottomNavigationItem {
    /**
     * 获取显示图标的View
     *
     * @return
     */
    ImageView getIconView();

    /**
     * 获取标题文字的View
     *
     * @return
     */
    TextView getTitleView();

    int getPosition();

    void setPosition(int position);

    void setId(int id);

    int getId();

    void setLayoutParams(ViewGroup.LayoutParams lp);

    View getLayoutView();

    void setOnClickListener(View.OnClickListener listener);

}
