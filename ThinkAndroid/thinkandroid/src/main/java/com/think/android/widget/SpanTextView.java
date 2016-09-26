package com.think.android.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by borney on 9/26/16.
 */
public class SpanTextView extends TextView {
    public SpanTextView(Context context) {
        super(context);
    }

    public SpanTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SpanTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void append(CharSequence text, int start, int end) {
        super.append(text, start, end);
    }

    public SpanTextView span(CharSequence text) {
        append(text);
        return this;
    }

    public Spanedable spanedable(CharSequence text) {
        return new Spanedable(text);
    }


    public final class Spanedable {
        private CharSequence text;
        private Map<Object, Integer> spans = new HashMap<>();

        Spanedable(CharSequence text) {
            this.text = text;
        }

        /**
         * 设置绝对大小
         *
         * @param size
         * @return
         */
        public Spanedable absoluteSize(int size) {
            return absoluteSize(size, false);
        }

        /**
         * @param size
         * @param dip
         * @return
         */
        public Spanedable absoluteSize(int size, boolean dip) {
            return absoluteSize(size, dip, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        /**
         *
         * @param size
         * @param dip
         * @param flags
         * @return
         */
        public Spanedable absoluteSize(int size, boolean dip, int flags) {
            spans.put(new AbsoluteSizeSpan(size, dip), flags);
            return this;
        }

        /**
         * 设置相对大小
         *
         * @param proportion
         * @return
         */
        public Spanedable relativeSize(float proportion) {
            return relativeSize(proportion, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        /**
         *
         * @param proportion
         * @param flags
         * @return
         */
        public Spanedable relativeSize(float proportion, int flags) {
            spans.put(new RelativeSizeSpan(proportion), flags);
            return this;
        }

        /**
         * 设置颜色
         *
         * @param color
         * @return
         */
        public Spanedable color(int color) {
            return color(color, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        /**
         *
         * @param color
         * @param flags
         * @return
         */
        public Spanedable color(int color, int flags) {
            spans.put(new ForegroundColorSpan(color), flags);
            return this;
        }

        /**
         * 设置背景
         *
         * @param drawable
         * @return
         */
        public Spanedable background(Drawable drawable) {
            return this;
        }

        /**
         *
         * @param color
         * @return
         */
        public Spanedable backgroundColor(int color) {
            return backgroundColor(color, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        /**
         *
         * @param color
         * @param flags
         * @return
         */
        public Spanedable backgroundColor(int color, int flags) {
            spans.put(new BackgroundColorSpan(color), flags);
            return this;
        }

        /**
         * 设置链接
         *
         * @param url
         * @return
         */
        public Spanedable autoLink(String url) {
            return autoLink(url, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        /**
         *
         * @param url
         * @param flags
         * @return
         */
        public Spanedable autoLink(String url, int flags) {
            spans.put(new URLSpan(url), flags);
            return this;
        }

        /**
         * 设置字体
         *
         * @param family
         * @return
         */
        public Spanedable typeface(String family) {
            return typeface(family, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        /**
         *
         * @param family
         * @param flags
         * @return
         */
        public Spanedable typeface(String family, int flags) {
            spans.put(new TypefaceSpan(family), flags);
            return this;
        }

        /**
         * 字体样式
         *
         * @param style
         * @return
         */
        public Spanedable type(int style) {
            return type(style, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        /**
         *
         * @param style
         * @param flags
         * @return
         */
        public Spanedable type(int style, int flags) {
            spans.put(new StyleSpan(style), flags);
            return this;
        }

        /**
         * 下划线
         *
         * @return
         */
        public Spanedable underline() {
            return underline(Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        /**
         *
         * @param flags
         * @return
         */
        public Spanedable underline(int flags) {
            spans.put(new UnderlineSpan(), flags);
            return this;
        }

        /**
         * 删除线
         *
         * @return
         */
        public Spanedable strikethrough() {
            return strikethrough(Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        /**
         *
         * @param flags
         * @return
         */
        public Spanedable strikethrough(int flags) {
            spans.put(new StrikethroughSpan(), flags);
            return this;
        }

        /**
         * 下标
         *
         * @return
         */
        public Spanedable subscript() {
            return subscript(Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        /**
         *
         * @param flags
         * @return
         */
        public Spanedable subscript(int flags) {
            spans.put(new SubscriptSpan(), flags);
            return this;
        }

        /**
         * 上标
         *
         * @return
         */
        public Spanedable superscript() {
            return superscript(Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        /**
         *
         * @param flags
         * @return
         */
        public Spanedable superscript(int flags) {
            spans.put(new SuperscriptSpan(), flags);
            return this;
        }

        /**
         * x缩放
         *
         * @param proportion
         * @return
         */
        public Spanedable scaleX(float proportion) {
            return scaleX(proportion, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        /**
         *
         * @param proportion
         * @param flags
         * @return
         */
        public Spanedable scaleX(float proportion, int flags) {
            spans.put(new ScaleXSpan(proportion), flags);
            return this;
        }

        /**
         *
         * @return
         */
        public TextView commit() {
            SpannableStringBuilder ssb = new SpannableStringBuilder(text);
            Iterator<Map.Entry<Object, Integer>> iterator = spans.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Integer> next = iterator.next();
                ssb.setSpan(next.getKey(), 0, ssb.length(), next.getValue());
            }
            SpanTextView.this.append(ssb);
            spans.clear();
            return SpanTextView.this;
        }
    }
}
