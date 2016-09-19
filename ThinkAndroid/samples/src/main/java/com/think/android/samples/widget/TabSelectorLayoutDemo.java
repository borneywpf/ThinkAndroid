package com.think.android.samples.widget;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.think.android.samples.R;
import com.think.android.widget.TabSelectorLayout;

/**
 * Created by borney on 9/19/16.
 */
public class TabSelectorLayoutDemo extends AppCompatActivity {
    private TabSelectorLayout mTabSelectorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabselectorlayout_demo);
        mTabSelectorLayout = (TabSelectorLayout) findViewById(R.id.tabselectorlayout);

        Resources resources = getResources();
        mTabSelectorLayout.addTab(TabSelectorLayout.newTab().setDrawable(resources.getDrawable(R.drawable.tab_contact)).setPosition(0).setTitle("contact"));
        mTabSelectorLayout.addTab(TabSelectorLayout.newTab().setDrawable(resources.getDrawable(R.drawable.tab_moment)).setPosition(1).setTitle("moment"));
        mTabSelectorLayout.addTab(TabSelectorLayout.newTab().setDrawable(resources.getDrawable(R.drawable.tab_msg)).setPosition(2).setTitle("message"));
        mTabSelectorLayout.addTab(TabSelectorLayout.newTab().setDrawable(resources.getDrawable(R.drawable.tab_person)).setPosition(3).setTitle("person"));
    }
}
