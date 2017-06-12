package com.lvzp.bottomlayoutdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.lvzp.bottomlayoutdemo.widget.BottomNavigationLayout;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {

    private List<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        BottomNavigationLayout layout = (BottomNavigationLayout) findViewById(R.id.bottom_layout);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(BlankFragment.newInstance("页面1"));
        mFragmentList.add(BlankFragment.newInstance("页面2"));
        mFragmentList.add(BlankFragment.newInstance("页面3"));
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        layout.setupWhiteViewPager(viewPager);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }

}
