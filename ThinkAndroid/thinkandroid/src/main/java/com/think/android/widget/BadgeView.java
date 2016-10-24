package com.think.android.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by borney on 10/24/16.
 */

public class BadgeView {
    private View target;
    private BadgeViewFactory factory = DEFAULT_BADGEVIEW_FACTORY;
    private int targetWidth, targetHeight;
    private Activity activity;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private View view;

    public BadgeView(View target) {
        this(target, null);
    }

    public BadgeView(View target, BadgeViewFactory factory) {
        this.target = target;
        this.factory = factory;
        activity = (Activity) target.getContext();
        Window window = activity.getWindow();
        windowManager = window.getWindowManager();
        initWindowParams();
        measureTarget();
    }

    private void initWindowParams() {
        params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSPARENT;
    }

    public void measureTarget() {
        if (target.getVisibility() == View.VISIBLE) {
            targetWidth = target.getWidth();
            targetHeight = target.getHeight();
            if (targetWidth == 0 && targetHeight == 0) {
                ViewTreeObserver observer = target.getViewTreeObserver();
                observer.addOnGlobalLayoutListener(onGlobalLayoutListener);
            }
        }
    }

    private void setView(View view, int left, int top) {
        if (view == null) {
            throw new NullPointerException("view is null!!!");
        }
        this.view = view;
        params.x = left;
        params.y = top - targetHeight / 2;
        windowManager.addView(view, params);
    }

    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        int count = 0;

        @Override
        public void onGlobalLayout() {
            if (count != 0) {
                return;
            } else {
                count++;
            }
            targetWidth = target.getWidth();
            targetHeight = target.getHeight();
            final int[] location = new int[2];
            target.getLocationInWindow(location);
            setView(factory.makeView(activity), location[0], location[1]);
        }
    };

    public static final BadgeViewFactory DEFAULT_BADGEVIEW_FACTORY = new BadgeViewFactory() {
        @Override
        public View makeView(Context context) {
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            return textView;
        }
    };

    public interface BadgeViewFactory {
        View makeView(Context context);
    }
}
