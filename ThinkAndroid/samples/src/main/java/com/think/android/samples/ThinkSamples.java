package com.think.android.samples;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.think.android.support.widget.ItemDecorationVerticalDivider;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by borney on 9/8/16.
 */
public class ThinkSamples extends AppCompatActivity {
    private static final String TAG = "TAGThinkSamples";
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thinksamples);
        Intent intent = getIntent();
        String path = intent.getStringExtra("com.think.android.samples.Path");

        if (path == null) {
            path = "";
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.thinksamples_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new ItemDecorationVerticalDivider(this));
        List<Map<String, Object>> datas = getData(path);
        Log.d(TAG, "datas = " + datas);
        mRecyclerView.setAdapter(new ViewAdapter(this, datas));
    }

    private List<Map<String, Object>> getData(String prefix) {
        Log.d(TAG, "prefix = " + prefix);
        List<Map<String, Object>> myData = new ArrayList<>();
        Intent mainIntent = new Intent();
        mainIntent.setAction(Intent.ACTION_MAIN);
        mainIntent.addCategory(ThinkAndroidApp.SAMPLES_CATEGORY);
        PackageManager pm = getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(mainIntent, 0);
        Log.d(TAG, "infos = " + infos);
        if (null == infos) {
            return myData;
        }

        String[] prefixPath;
        String prefixWithSlash = prefix;

        if (prefix.equals("")) {
            prefixPath = null;
        } else {
            prefixPath = prefix.split("/");
            prefixWithSlash = prefix + "/";
        }

        Log.d(TAG, "prefixPath = " + prefixPath + "\nprefixWithSlash = " + prefixWithSlash);

        Map<String, Boolean> entries = new HashMap<String, Boolean>();

        for (int i = 0, len = infos.size(); i < len; i++) {
            ResolveInfo info = infos.get(i);
            CharSequence labelSeq = info.loadLabel(pm);
            String label = labelSeq != null ? labelSeq.toString() : info.activityInfo.name;
            Log.d(TAG, "label = " + label);

            if (prefixWithSlash.length() == 0 || label.startsWith(prefixWithSlash)) {
                String[] labelPath = label.split("/");
                String nextLabel = prefixPath == null ? labelPath[0] : labelPath[prefixPath.length];
                Log.d(TAG, "nextLabel = " + nextLabel);

                if ((prefixPath != null ? prefixPath.length : 0) == labelPath.length - 1) {
                    addItem(myData, nextLabel, activityIntent(info.activityInfo.applicationInfo.packageName, info.activityInfo.name));
                } else {
                    if (entries.get(nextLabel) == null) {
                        addItem(myData, nextLabel, browseIntent(prefix.equals("") ? nextLabel : prefix + "/" + nextLabel));
                        entries.put(nextLabel, true);
                    }
                }
            }
        }

        Collections.sort(myData, sDisplayNameComparator);

        return myData;
    }

    private void addItem(List<Map<String, Object>> data, String name, Intent intent) {
        Map<String, Object> temp = new HashMap<>();
        temp.put("title", name);
        temp.put("intent", intent);
        data.add(temp);
    }

    private Intent browseIntent(String path) {
        Intent result = new Intent();
        result.setClass(this, ThinkSamples.class);
        result.putExtra("com.think.android.samples.Path", path);
        return result;
    }

    private Intent activityIntent(String pkg, String componentName) {
        Intent result = new Intent();
        result.setClassName(pkg, componentName);
        return result;
    }

    private Comparator<Map<String, Object>> sDisplayNameComparator = new Comparator<Map<String, Object>>() {
        private final Collator collator = Collator.getInstance();

        @Override
        public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
            return collator.compare(lhs.get("title"), rhs.get("title"));
        }
    };

    private class ViewAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final List<Map<String, Object>> datas;
        private int itemBackground = 0;

        public ViewAdapter(Context context, List<Map<String, Object>> datas) {
            this.datas = datas;
            int[] attrs = new int[]{android.R.attr.selectableItemBackground};
            TypedArray array = context.obtainStyledAttributes(attrs);
            try {
                itemBackground = array.getResourceId(0, 0);
            } finally {
                if (array != null) {
                    array.recycle();
                }
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Map<String, Object> map = datas.get(position);
            Log.d(TAG, "map = " + map);
            holder.textView.setText((CharSequence) map.get("title"));
            holder.itemView.setBackgroundResource(itemBackground);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent((Intent) map.get("intent"));
                    intent.addCategory(ThinkAndroidApp.SAMPLES_CATEGORY);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas != null ? datas.size() : 0;
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
