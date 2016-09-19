package com.think.android.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by borney on 9/19/16.
 */
public class TabSelectorLayout extends ViewGroup {
    private static final String TAG = "TAGTabSelectorLayout";

    public TabSelectorLayout(Context context) {
        super(context);
    }

    public TabSelectorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabSelectorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addTab(Tab tab) {
        if (tab != null) {
            TabView tabView = new TabView(getContext());
            tab.tabView = tabView;
            tab.tabView.setBackgroundDrawable(tab.drawable);
            addView(tabView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }

    public static Tab newTab() {
        return new Tab();
    }

    /**
     * Ask all children to measure themselves and compute the measurement of this
     * layout based on the children.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        Log.d(TAG, "count = " + count);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        Log.d(TAG, "widthMode = " + widthMode + " width = " + width + " heightMode = " + heightMode + " height = " + height);

        int childWidth = width - getPaddingLeft() - getPaddingRight() / count;
        int maxChildHeight = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
                int h = child.getMeasuredHeight();
                int w = child.getMeasuredWidth();
                maxChildHeight = maxChildHeight > h ? maxChildHeight : h;
                Log.d(TAG, "child(" + i + ") w = " + w + " h = " + h);
            }
        }
        int actionBarHeight = getActionBarHeight();

        int h = actionBarHeight > maxChildHeight ? actionBarHeight : maxChildHeight;

        setMeasuredDimension(width, h + getPaddingTop() + getPaddingBottom());
    }

    private int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            return TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        return 0;
    }

    /**
     * Position all children within this layout.
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
    }

    public static final class Tab {
        private StateListDrawable drawable;
        private String title;
        private int position;
        TabView tabView;

        private Tab() {

        }

        public int getPosition() {
            return position;
        }

        public Tab setPosition(int position) {
            this.position = position;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public Tab setTitle(String title) {
            this.title = title;
            return this;
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public Tab setDrawable(Drawable d) {
            if (d instanceof StateListDrawable) {
                this.drawable = (StateListDrawable) d;
            }
            return this;
        }
    }

    private class TabView extends View {
        private int textSize = 12;
        private int textNormalColor;
        private int textSelectColor;
        private int viewWidth;
        private int viewHeight;
        private String text = "ABCD";
        private final Paint textNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint textSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private Bitmap bitmapNormal;
        private Bitmap bitmapSelect;
        private final Paint bitmapNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        private final Paint bitmapSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        private final Rect boundText = new Rect();

        public TabView(Context context) {
            this(context, null);
        }

        public TabView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public TabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initText();
            initView();
        }

        private void initText() {
            textNormalPaint.setColor(textNormalColor);
            textNormalPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getResources().getDisplayMetrics()));
            textNormalPaint.setAlpha(255);

            textSelectPaint.setColor(textSelectColor);
            textSelectPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getResources().getDisplayMetrics()));
            textSelectPaint.setAlpha(0);

            bitmapNormalPaint.setAlpha(255);
            bitmapSelectPaint.setAlpha(0);
        }

        private void initView() {
        }

        private void measureText() {
            textNormalPaint.getTextBounds(text, 0, text.length(), boundText);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);

            int height = MeasureSpec.getSize(heightMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int w = 0, h = 0;

            measureText();
            Log.d(TAG, "child widthMode = " + widthMode + " width = " + width + " heightMode = " + heightMode + " height = " + height);
            int contentWidth = Math.max(boundText.width(), getBackground().getIntrinsicWidth());
            int desiredWidth = getPaddingLeft() + getPaddingRight() + contentWidth;
            switch (widthMode) {
                case MeasureSpec.AT_MOST:
                    w = Math.min(width, desiredWidth);
                    break;
                case MeasureSpec.EXACTLY:
                    w = width;
                    break;
                case MeasureSpec.UNSPECIFIED:
                    w = desiredWidth;
                    break;
            }
            int contentHeight = boundText.height() + getBackground().getIntrinsicHeight();
            int desiredHeight = getPaddingTop() + getPaddingBottom() + contentHeight;
            switch (heightMode) {
                case MeasureSpec.AT_MOST:
                    h = Math.min(height, desiredHeight);
                    break;
                case MeasureSpec.EXACTLY:
                    h = height;
                    break;
                case MeasureSpec.UNSPECIFIED:
                    h = contentHeight;
                    break;
            }
            setMeasuredDimension(w, h);
            viewWidth = getMeasuredWidth();
            viewHeight = getMeasuredHeight();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }
    }

}
