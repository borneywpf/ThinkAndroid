package com.think.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.think.android.R;

/**
 * Created by borney on 9/19/16.
 */
public class TabSelectorLayout extends ViewGroup {
    private static final String TAG = "TAGTabSelectorLayout";
    private ViewPager viewPager;
    private int textSize = 12;
    private int drawablePadding = 10;
    private int normalTextColor, selectTextColor;

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            TabView cur = (TabView) getChildAt(position);
            if (positionOffset > 0) {
                cur.setTabOffSet(1 - positionOffset);
                TabView next = (TabView) getChildAt(position + 1);
                next.setTabOffSet(positionOffset);
            } else {
                cur.setTabOffSet(1 - positionOffset);
            }
        }

        @Override
        public void onPageSelected(int position) {
            setCurrentItem(position);
        }
    };

    public TabSelectorLayout(Context context) {
        super(context);
    }

    public TabSelectorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabSelectorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = null;
        try {
            array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TabSelectorLayout, defStyleAttr, 0);
            int count = array.getIndexCount();
            for (int i = 0; i < count; i++) {
                final int index = array.getIndex(i);
                if (index == R.styleable.TabSelectorLayout_normalTextColor) {
                    normalTextColor = array.getColor(index, Color.BLACK);
                } else if (index == R.styleable.TabSelectorLayout_selectTextColor) {
                    selectTextColor = array.getColor(index, Color.BLACK);
                } else if (index == R.styleable.TabSelectorLayout_drawablePadding) {
                    drawablePadding = array.getDimensionPixelSize(index, 10);
                } else if (index == R.styleable.TabSelectorLayout_android_textSize) {
                    textSize = array.getDimensionPixelSize(index,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getResources().getDisplayMetrics()));
                }
            }

            Log.d(TAG, "normalTextColor = " + normalTextColor + " drawablePadding = " + drawablePadding + " textSize = " + textSize);
        } finally {
            if (array != null) {
                array.recycle();
            }
        }
    }

    public static Tab newTab() {
        return new Tab();
    }

    public int addTab(final Tab tab) {
        if (tab == null) {
            throw new NullPointerException("tab is null");
        }
        if (tab.title == null || tab.selectDrawable == null || tab.normalDrawable == null) {
            throw new IllegalArgumentException("some argument is null");
        }
        final TabView tabView = new TabView(getContext());
        tabView.attach(tab);
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(tab.getPosition(), false);
            }
        });
        addView(tabView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tab.setPosition(indexOfChild(tabView));
        if (tab.position == 3) {
            tabView.setBackgroundColor(Color.BLUE);
        }
        return tab.position;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof TabView || child instanceof RelativeLayout) {
            super.addView(child, index, params);
        } else {
            throw new IllegalArgumentException("child is not TabView");
        }
    }

    public void bindViewPager(ViewPager pager) {
        if (pager == null)
            throw new NullPointerException("pager is null");

        if (viewPager == pager)
            return;

        if (viewPager != null)
            viewPager.removeOnPageChangeListener(pageChangeListener);

        PagerAdapter adapter = pager.getAdapter();
        if (adapter == null)
            throw new IllegalArgumentException("pager not set adapter");
        if (adapter.getCount() != getChildCount())
            throw new IllegalArgumentException("pager count is not equeals tab count");

        pager.addOnPageChangeListener(pageChangeListener);

        viewPager = pager;

        setCurrentItem(viewPager.getCurrentItem());
    }

    public void setCurrentItem(int item) {
        final int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            child.setSelected(item == i);
        }
    }

    /**
     * Ask all children to measure themselves and compute the measurement of this
     * layout based on the children.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        final int count = getChildCount();

        int childWidth = (width - getPaddingLeft() - getPaddingRight()) / count;
        int maxChildHeight = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
                int h = child.getMeasuredHeight();
                int w = child.getMeasuredWidth();
                maxChildHeight = maxChildHeight > h ? maxChildHeight : h;
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
        int lefPos = getPaddingLeft();

        final int parentTop = getPaddingTop();
        final int parentBottom = bottom - top - getPaddingBottom();
        final int parentHeight = parentBottom - parentTop;

        final Rect childRect = new Rect();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();

                lefPos += lp.leftMargin;

                childRect.left = lefPos;
                childRect.top = (parentHeight - height) / 2;
                childRect.bottom = childRect.top + height;
                childRect.right = childRect.left + width;

                // Use the child's gravity and size to determine its final
                // frame within its container.
                //Gravity.apply(lp.gravity, width, height, mTmpContainerRect, mTmpChildRect);

                child.layout(childRect.left, childRect.top, childRect.right, childRect.bottom);

                lefPos += width;
                lefPos += lp.rightMargin;
            }
        }
    }

    /**
     * Custom per-child layout information.
     */
    public static class LayoutParams extends MarginLayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    public static final class Tab {
        Drawable normalDrawable;
        Drawable selectDrawable;
        String title;
        int position;

        private Tab() {
        }

        Tab setPosition(int position) {
            this.position = position;
            return this;
        }

        public int getPosition() {
            return position;
        }

        public Tab setTitle(String title) {
            this.title = title;
            return this;
        }

        public Tab setNormalDrawable(Drawable d) {
            normalDrawable = d;
            return this;
        }

        public Tab setSelectDrawable(Drawable d) {
            selectDrawable = d;
            return this;
        }
    }

    private class TabView extends View {
        private int normalAlpha = 255;
        private int viewWidth;
        private int viewHeight;
        private final Paint textNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
        private final Paint textSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
        private final Rect boundText = new Rect();
        private Tab tab;

        public TabView(Context context) {
            this(context, null);
        }

        public TabView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public TabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initText();
        }

        private void initText() {
            textNormalPaint.setColor(normalTextColor);
            textNormalPaint.setTextSize(textSize);

            textSelectPaint.setColor(selectTextColor);
            textSelectPaint.setTextSize(textSize);
        }

        private void measureText() {
            textNormalPaint.getTextBounds(tab.title, 0, tab.title.length(), boundText);
        }

        void attach(Tab tab) {
            this.tab = tab;
        }

        void setTabOffSet(float offSet) {
            normalAlpha = (int) (255 - offSet * 255);
            invalidate();
        }

        @Override
        public void setSelected(boolean selected) {
            normalAlpha = selected ? 0 : 255;
            super.setSelected(selected);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);

            int height = MeasureSpec.getSize(heightMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int w = 0, h = 0;

            measureText();

            int contentWidth = Math.max(boundText.width(), Math.max(tab.normalDrawable.getIntrinsicWidth(), tab.selectDrawable.getIntrinsicWidth()));
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
            int contentHeight = (int) (textNormalPaint.getFontSpacing() + Math.max(tab.normalDrawable.getIntrinsicHeight(), tab.selectDrawable.getIntrinsicHeight()) + drawablePadding);
            int desiredHeight = getPaddingTop() + getPaddingBottom() + contentHeight;
            switch (heightMode) {
                case MeasureSpec.AT_MOST:
                    h = Math.min(height, desiredHeight);
                    break;
                case MeasureSpec.EXACTLY:
                    h = height;
                    break;
                case MeasureSpec.UNSPECIFIED:
                    h = height;
                    break;
            }
            setMeasuredDimension(w, h);
            viewWidth = getMeasuredWidth();
            viewHeight = getMeasuredHeight();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawNoramalBitmap(canvas);
            drawSelectBitmap(canvas);
            drawText(canvas);
        }

        private void drawText(Canvas canvas) {
            int drawableHeight = Math.max(tab.normalDrawable.getIntrinsicHeight(), tab.selectDrawable.getIntrinsicHeight());
            float x = (viewWidth - boundText.width()) / 2.0f;
            float y = (viewHeight + drawableHeight + boundText.height() + drawablePadding) >> 1;
            textNormalPaint.setAlpha(normalAlpha);
            canvas.drawText(tab.title, x, y, textNormalPaint);
            textSelectPaint.setAlpha(255 - normalAlpha);
            canvas.drawText(tab.title, x, y, textSelectPaint);
        }

        private void drawNoramalBitmap(Canvas canvas) {
            int width = tab.normalDrawable.getIntrinsicWidth();
            int height = tab.normalDrawable.getIntrinsicHeight();
            int left = (viewWidth - width) / 2;
            int top = (viewHeight - height - boundText.height() - drawablePadding) >> 1;
            tab.normalDrawable.setBounds(left, top, left + width, top + height);
            tab.normalDrawable.setAlpha(normalAlpha);
            tab.normalDrawable.draw(canvas);
        }

        private void drawSelectBitmap(Canvas canvas) {
            int width = tab.selectDrawable.getIntrinsicWidth();
            int height = tab.selectDrawable.getIntrinsicHeight();
            int left = (viewWidth - width) / 2;
            int top = (viewHeight - height - boundText.height() - drawablePadding) >> 1;
            tab.selectDrawable.setBounds(left, top, left + width, top + height);
            tab.selectDrawable.setAlpha(255 - normalAlpha);
            tab.selectDrawable.draw(canvas);
        }
    }

}
