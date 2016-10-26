package com.think.android.widget;

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
    private View target; //需要添加标记的View
    private BadgeViewFactory factory; //构建标记view的工厂类
    private BadgeView.LayoutParams layoutParams; //标记view的布局参数
    private Drawable drawable; //标记view显示的Drawable
    private CharSequence text; //标记view显示的文字
    private View view; //标记View
    /**
     * 目标View在Window中的坐标
     */
    private final int[] targetLocation = new int[2];

    private Context context;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    /**
     * 目标View的宽高
     */
    private int targetWidth, targetHeight;
    /**
     * 是否显示标记View
     */
    private boolean isShow = true;

    private BadgeView(Build build) {
        this.target = build.target;
        this.factory = build.factory;
        this.layoutParams = build.layoutParams;
        this.drawable = build.drawable;
        this.text = build.text;
        context =  target.getContext();
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        initWindowParams();
        measureTarget();
    }

    /**
     * 初始化WindowParams
     */
    private void initWindowParams() {
        params = new WindowManager.LayoutParams();
        params.width = this.layoutParams.width;
        params.height = this.layoutParams.height;
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSPARENT;
    }

    /**
     * 如果在onCreate方法就添加时无法获得target的大小，通过监听OnGlobalLayoutListener获得View大小
     */
    private void measureTarget() {
        if (target.getVisibility() == View.VISIBLE) {
            targetWidth = target.getWidth();
            targetHeight = target.getHeight();
            target.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
            if (targetWidth != 0 || targetHeight != 0) { //如果系统已经回调过就自己主动dispatch回调
                target.getViewTreeObserver().dispatchOnGlobalLayout();
            }
        }
    }


    /**
     * 显示标记View
     */
    public void show() {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
        isShow = true;
    }

    /**
     * 隐藏标记View
     */
    public void gone() {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
        isShow = false;
    }

    /**
     * 更新标记View的布局参数
     * @param text
     */
    public void updateText(CharSequence text) {
        this.text = text;
        this.factory.updateText(text);
    }

    /**
     * 更新Drawable
     * @param drawable
     */
    public void updateDrawable(Drawable drawable) {
        this.drawable = drawable;
        this.factory.updateDrawable(drawable);
    }

    /**
     * 更新Drawable
     * @param layoutParams
     */
    public void updateLayoutParams(BadgeView.LayoutParams layoutParams) {
        params.width = layoutParams.width;
        params.height = layoutParams.height;
        updateTargetLocation();
        params.x = targetLocation[0] + layoutParams.marginLeft;
        params.y = targetLocation[1] - targetHeight / 2 + layoutParams.marginTop;
        windowManager.updateViewLayout(view, params);
    }

    /**
     * 获得target在Window中的坐标
     */
    public void updateTargetLocation() {
        target.getLocationOnScreen(targetLocation);
    }

    /**
     * 设置标记View到Window中
     * @param view
     * @param marginLeft
     * @param marginTop
     */
    private void setView(View view, int marginLeft, int marginTop) {
        if (view == null) {
            throw new NullPointerException("view is null!!!");
        }
        this.view = view;
        this.view.setVisibility(isShow ? View.VISIBLE : View.GONE);
        updateTargetLocation();
        //初始标记View的坐标和targetView的坐标相同
        params.x = targetLocation[0] + marginLeft;
        params.y = targetLocation[1] - targetHeight / 2 + marginTop;
        /**
         * 添加标记View到Window中
         */
        windowManager.addView(view, params);
    }

    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            targetWidth = target.getWidth();
            targetHeight = target.getHeight();
            /**
             * 通过标记View工厂产生标记View并添加
             */
            setView(factory.makeView(context, drawable, text), BadgeView.this.layoutParams.marginLeft, BadgeView.this.layoutParams.marginTop);
            /**
             * 移除对OnGlobalLayoutListener的监听
             */
            target.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    };

    /**
     * 默认的标记View工厂
     */
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

    /**
     * Build类
     */
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

    /**
     * 标记View的布局参数
     */
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

    /**
     * 标记View的工厂
     */
    public interface BadgeViewFactory {
        View makeView(Context context, Drawable drawable, CharSequence text);

        void updateText(CharSequence text);

        void updateDrawable(Drawable drawable);
    }
}
