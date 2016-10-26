package com.think.android.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by borney on 10/24/16.
 */

public class BadgeView {
    private static final String TAG = "BadgeView";
    private View target;
    private BadgeViewFactory factory;
    private BadgeView.LayoutParams layoutParams;
    private Drawable drawable;
    private CharSequence text;
    private View view;
    private final int[] targetLocation = new int[2];

    private Activity activity;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private int targetWidth, targetHeight;
    private boolean isShow = true;

    private BadgeView(Build build) {
        this.target = build.target;
        this.factory = build.factory;
        this.layoutParams = build.layoutParams;
        this.drawable = build.drawable;
        this.text = build.text;
        activity = (Activity) target.getContext();
        windowManager = activity.getWindow().getWindowManager();
        initWindowParams();
        measureTarget();
    }

    private void initWindowParams() {
        params = new WindowManager.LayoutParams();
        params.width = this.layoutParams.width;
        params.height = this.layoutParams.height;
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSPARENT;
    }

    private void measureTarget() {
        if (target.getVisibility() == View.VISIBLE) {
            targetWidth = target.getWidth();
            targetHeight = target.getHeight();
            target.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
            if (targetWidth != 0 || targetHeight != 0) {
                target.getViewTreeObserver().dispatchOnGlobalLayout();
            }
        }
    }

    public void show() {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
        isShow = true;
    }

    public void gone() {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
        isShow = false;
    }

    public void updateText(CharSequence text) {
        this.text = text;
        this.factory.updateText(text);
    }

    public void updateDrawable(Drawable drawable) {
        this.drawable = drawable;
        this.factory.updateDrawable(drawable);
    }

    public void updateLayoutParams(BadgeView.LayoutParams layoutParams) {
        params.width = layoutParams.width;
        params.height = layoutParams.height;
        updateTargetLocation();
        params.x = targetLocation[0] + layoutParams.marginLeft;
        params.y = targetLocation[1] - targetHeight / 2 + layoutParams.marginTop;
        windowManager.updateViewLayout(view, params);
    }

    public void updateTargetLocation() {
        target.getLocationOnScreen(targetLocation);
    }

    private void setView(View view, int marginLeft, int marginTop) {
        if (view == null) {
            throw new NullPointerException("view is null!!!");
        }
        this.view = view;
        this.view.setVisibility(isShow ? View.VISIBLE : View.GONE);
        updateTargetLocation();
        params.x = targetLocation[0] + marginLeft;
        params.y = targetLocation[1] - targetHeight / 2 + marginTop;
        windowManager.addView(view, params);
    }

    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            targetWidth = target.getWidth();
            targetHeight = target.getHeight();
            setView(factory.makeView(activity, drawable, text), BadgeView.this.layoutParams.marginLeft, BadgeView.this.layoutParams.marginTop);
            target.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    };

    public static final BadgeViewFactory DEFAULT_BADGEVIEW_FACTORY = new BadgeViewFactory() {
        TextView textView;

        @Override
        public View makeView(Context context, Drawable drawable, CharSequence text) {
            textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            GradientDrawable d = new GradientDrawable();
            d.setColor(Color.RED);
            d.setStroke(2, Color.GRAY);
            d.setShape(GradientDrawable.OVAL);
            textView.setBackground(drawable);
            textView.setPadding(3, 3, 3, 3);
            textView.setTextColor(Color.WHITE);
            textView.setText(text);
            return textView;
        }

        @Override
        public void updateText(CharSequence text) {
            textView.setText(text);
        }

        @Override
        public void updateDrawable(Drawable drawable) {
            textView.setBackground(drawable);
        }
    };

    public final static class Build {
        View target;
        BadgeViewFactory factory = DEFAULT_BADGEVIEW_FACTORY;
        BadgeView.LayoutParams layoutParams = new BadgeView.LayoutParams();
        Drawable drawable;
        CharSequence text;

        public Build(View target) {
            this.target = target;
        }

        public Build factory(BadgeViewFactory factory) {
            this.factory = factory;
            return this;
        }

        public Build laytouParams(BadgeView.LayoutParams layoutParams) {
            this.layoutParams = layoutParams;
            return this;
        }

        public Build drawable(Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        public Build text(CharSequence text) {
            this.text = text;
            return this;
        }

        public BadgeView build() {
            return new BadgeView(this);
        }
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

        public int marginLeft;

        public int marginTop;

        public LayoutParams() {
            super(WRAP_CONTENT, WRAP_CONTENT);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }

    public interface BadgeViewFactory {
        View makeView(Context context, Drawable drawable, CharSequence text);

        void updateText(CharSequence text);

        void updateDrawable(Drawable drawable);
    }
}
