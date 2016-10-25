package com.think.android.samples.widget;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
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
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

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

        BadgeView.Build build = new BadgeView.Build(mTabSelectorLayout.getChildAt(pos));
        LayoutParams params = new LayoutParams(60, 60);
        params.marginLeft = 160;
        params.marginTop = 0;
        build.laytouParams(params);
        build.text(String.valueOf(9));
        build.drawable(getDrawable(R.drawable.ic_number_bg));
        build.build().show();

        pos = mTabSelectorLayout.addTab(TabSelectorLayout.newTab()
                .setNormalDrawable(resources.getDrawable(R.drawable.ic_tab_person))
                .setSelectDrawable(resources.getDrawable(R.drawable.ic_tab_person_select))
                .setTitle("person"));
        mFragmentMap.put(pos, new MyFragment(pos));

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));

        mTabSelectorLayout.bindViewPager(mViewPager);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("TabSelectorLayoutDemo Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        AppIndex.AppIndexApi.start(mClient, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mClient, getIndexApiAction());
        mClient.disconnect();
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
            return view;
        }
    }
}
