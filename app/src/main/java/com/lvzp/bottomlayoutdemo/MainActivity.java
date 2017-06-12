package com.lvzp.bottomlayoutdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lvzp.bottomlayoutdemo.widget.BottomNavigationLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(BlankFragment.newInstance("页面1"));
        mFragmentList.add(BlankFragment.newInstance("页面2"));
        mFragmentList.add(BlankFragment.newInstance("页面3"));
        BottomNavigationLayout layout = (BottomNavigationLayout) findViewById(R.id.bottom_layout);
        layout.bindingReplaceFragmentList(mFragmentList);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn_viewpager) {
            Intent intent = new Intent(this, Main3Activity.class);
            startActivity(intent);
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, MainActivity2.class);
        startActivity(intent);
    }

}
