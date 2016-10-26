package com.think.android.samples.widget;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.think.android.samples.R;
import com.think.android.widget.BadgeView;
import com.think.android.widget.TabSelectorLayout;

import java.util.HashMap;
import java.util.Map;

import static com.think.android.widget.BadgeView.LayoutParams;

/**
 * Created by borney on 9/19/16.
 */
public class TabSelectorLayoutDemo extends AppCompatActivity {
    private static final String TAG = "TAGTabSelectorLayoutDemo";
    private TabSelectorLayout mTabSelectorLayout;
    private ViewPager mViewPager;
    private Map<Integer, MyFragment> mFragmentMap = new HashMap<>();
    private BadgeView mBadgeView3;
    private int count3 = 1;

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
        mFragmentMap.put(pos, new My3Fragment(pos));

        View at = mTabSelectorLayout.getChildAt(pos);
        BadgeView.Build build = new BadgeView.Build(at);
        LayoutParams params = new LayoutParams(60, 60);
        params.marginLeft = 160;
        params.marginTop = -10;
        build.laytouParams(params);
        build.text(String.valueOf(count3));
        build.drawable(getDrawable(R.drawable.ic_number_bg));
        mBadgeView3 = build.build();
        mBadgeView3.show();

        pos = mTabSelectorLayout.addTab(TabSelectorLayout.newTab()
                .setNormalDrawable(resources.getDrawable(R.drawable.ic_tab_person))
                .setSelectDrawable(resources.getDrawable(R.drawable.ic_tab_person_select))
                .setTitle("person"));
        mFragmentMap.put(pos, new MyFragment(pos));

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
        int position;

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

    class My3Fragment extends MyFragment {

        My3Fragment(int position) {
            super(position);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_tabselectorlayout_three, container, false);
            Button addCount = (Button) view.findViewById(R.id.addcount);
            addCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int value = ++count3;
                    mBadgeView3.updateText(String.valueOf(value));
                }
            });

            Button show = (Button) view.findViewById(R.id.show);
            show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBadgeView3.show();
                }
            });

            Button hide = (Button) view.findViewById(R.id.hide);
            hide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBadgeView3.gone();
                }
            });

            Button updateDrawable = (Button) view.findViewById(R.id.updateDrawable);
            updateDrawable.setOnClickListener(new View.OnClickListener() {
                boolean drawable_l = false;

                @Override
                public void onClick(View v) {
                    if (!drawable_l) {
                        mBadgeView3.updateDrawable(getDrawable(R.drawable.ic_number_l_bg));
                        drawable_l = true;
                    } else {
                        mBadgeView3.updateDrawable(getDrawable(R.drawable.ic_number_bg));
                        drawable_l = false;
                    }
                }
            });

            Button updateParams = (Button) view.findViewById(R.id.updateParams);
            updateParams.setOnClickListener(new View.OnClickListener() {
                boolean new_params = false;
                @Override
                public void onClick(View v) {
                    if (!new_params) {
                        LayoutParams params = new LayoutParams(80, 80);
                        params.marginLeft = 160;
                        params.marginTop = -20;
                        mBadgeView3.updateLayoutParams(params);
                        new_params = true;
                    } else {
                        LayoutParams params = new LayoutParams(60, 60);
                        params.marginLeft = 160;
                        params.marginTop = -10;
                        mBadgeView3.updateLayoutParams(params);
                        new_params = false;
                    }
                }
            });
            return view;
        }
    }
}
