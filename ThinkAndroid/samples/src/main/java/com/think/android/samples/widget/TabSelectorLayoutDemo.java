package com.think.android.samples.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.TextView;

import com.think.android.samples.R;
import com.think.android.widget.TabSelectorLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by borney on 9/19/16.
 */
public class TabSelectorLayoutDemo extends AppCompatActivity {
    private static final String TAG = "TAGTabSelectorLayoutDemo";
    private TabSelectorLayout mTabSelectorLayout;
    private ViewPager mViewPager;
    private Map<Integer, MyFragment> mFragmentMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabselectorlayout_demo);
        mTabSelectorLayout = (TabSelectorLayout) findViewById(R.id.tabselectorlayout);

        Resources resources = getResources();
        int pos = mTabSelectorLayout.addTab(TabSelectorLayout.newTab()
                .setNormalDrawable(resources.getDrawable(R.drawable.ic_tab_contact))
                .setSelectDrawable(resources.getDrawable(R.drawable.ic_tab_contact_select))
                .setTitle("contact"));
        mFragmentMap.put(pos, new MyFragment(pos));

        pos = mTabSelectorLayout.addTab(TabSelectorLayout.newTab()
                .setNormalDrawable(resources.getDrawable(R.drawable.ic_tab_moment))
                .setSelectDrawable(resources.getDrawable(R.drawable.ic_tab_moment_select))
                .setTitle("moment"));
        mFragmentMap.put(pos, new MyFragment(pos));

        pos = mTabSelectorLayout.addTab(TabSelectorLayout.newTab()
                .setNormalDrawable(resources.getDrawable(R.drawable.ic_tab_msg))
                .setSelectDrawable(resources.getDrawable(R.drawable.ic_tab_msg_select))
                .setTitle("message"));
        mFragmentMap.put(pos, new MyFragment(pos));

        final int[] location = new int[2];
        pos = mTabSelectorLayout.addTab(TabSelectorLayout.newTab()
                .setNormalDrawable(resources.getDrawable(R.drawable.ic_tab_person))
                .setSelectDrawable(resources.getDrawable(R.drawable.ic_tab_person_select))
                .setTitle("person"));
        mFragmentMap.put(pos, new MyFragment(pos));

        final View child = mTabSelectorLayout.getChildAt(pos);
        child.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int count = 0;
            @Override
            public void onGlobalLayout() {
                if (count != 0) return;
                count++;
                child.getLocationInWindow(location);
                Log.d(TAG, "location[0] = " + location[0] + " location[1] = " + location[1]);
                TextView textView = new TextView(TabSelectorLayoutDemo.this);
                textView.setBackgroundResource(R.drawable.ic_number_bg);
                textView.setGravity(Gravity.CENTER);
                textView.setText("10");
                textView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.d(TAG, "x = " + event.getRawX() + " y = " + event.getRawY());
                        return false;
                    }
                });
                WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                params.gravity = Gravity.TOP | Gravity.LEFT;
                int max = Math.max(textView.getBackground().getIntrinsicWidth(), textView.getBackground().getIntrinsicHeight());
                params.width = 60;
                params.height = 60;
                params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                params.format = PixelFormat.TRANSPARENT;
                params.x = location[0];
                params.y = location[1] - child.getHeight() / 2;
                getWindow().getWindowManager().addView(textView, params);
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));

        mTabSelectorLayout.bindViewPager(mViewPager);
    }


    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentMap.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentMap.size();
        }
    }

    class MyFragment extends Fragment {
        private int position;

        MyFragment(int position) {
            this.position = position;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Context context = container.getContext();
            TextView textView = new TextView(context);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, context.getResources().getDisplayMetrics()));
            textView.setText(String.valueOf(position));
            return textView;
        }
    }
}
