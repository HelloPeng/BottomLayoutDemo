package com.lvzp.bottomlayoutdemo;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.StateSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView select = (TextView) findViewById(R.id.tv_select_color);
        TextView unselect = (TextView) findViewById(R.id.tv_unselect_color);
        ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.color);
        select.setTextColor(colorStateList.getColorForState(new int[]{android.R.attr.state_selected}, Color.WHITE));
        unselect.setTextColor(colorStateList.getColorForState(new int[]{-android.R.attr.state_selected}, Color.WHITE));
    }

    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity2.class);
        startActivity(intent);
    }

}
