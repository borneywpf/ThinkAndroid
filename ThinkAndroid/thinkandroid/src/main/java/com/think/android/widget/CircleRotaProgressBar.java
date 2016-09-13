package com.think.android.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.think.android.R;

/**
 * Created by borney on 9/8/16.
 */
public class CircleRotaProgressBar extends TextView {
    private static final String TAG = "CircleRotaProgressBar";
    /**
     * 进度
     */
    private int progress = 0;
    /**
     * 进度条宽度
     */
    private float progressWidth = 4.0f;
    /**
     * 进度条宽度的一半
     */
    private float halfProgressWidth = progressWidth / 2;
    /**
     * 进度条底圆的宽度
     */
    private float secondaryProgressWidth = 2.0f;
    /**
     * 进度条颜色
     */
    private int progressColor = Color.argb(255, 255, 255, 255);
    /**
     * 进度条底圆的颜色
     */
    private int secondaryProgressColor = Color.argb(150, 255, 255, 255);
    /**
     * 动画持续时间
     */
    private int duration = 1500;
    /**
     * 第二个点动画开始延时时间
     */
    private int secondaryAnimatorDelay = 200;
    /**
     * 进度条弧度
     */
    private float sweepAngle;
    /**
     * 动画差值器
     */
    private TimeInterpolator interpolator = new DecelerateInterpolator();
    /**
     * 第一个点动画通过{@link PropertyValuesHolder}的方式实现
     */
    private PropertyValuesHolder pvh;
    /**
     * 定义两个点的属性动画
     */
    private ValueAnimator valueAnimator, _valueAnimator;
    /**
     * 初始状态
     */
    private State state = State.PAUSE;

    private Paint paint = new Paint();

    /**
     * 定义进度条的两个动画状态
     */
    public enum State {
        START, //开始动画

        PAUSE; //结束动画
    }

    public CircleRotaProgressBar(Context context) {
        this(context, null);
    }

    public CircleRotaProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleRotaProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = null;

        try {
            array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleRotaProgressBar, defStyleAttr, 0);
            int count = array.getIndexCount();
            for (int i = 0; i < count; i++) {
                final int index = array.getIndex(i);
                if (index == R.styleable.CircleRotaProgressBar_progressWidth) {
                    progressWidth = array.getDimensionPixelSize(index, 0);
                    halfProgressWidth = progressWidth / 2;
                } else if (index == R.styleable.CircleRotaProgressBar_secondaryProgressWidth) {
                    secondaryProgressWidth = array.getDimensionPixelSize(index, 0);
                } else if (index == R.styleable.CircleRotaProgressBar_progressColor) {
                    progressColor = array.getColor(index, progressColor);
                } else if (index == R.styleable.CircleRotaProgressBar_secondaryProgressColor) {
                    secondaryProgressColor = array.getColor(index, secondaryProgressColor);
                } else if (index == R.styleable.CircleRotaProgressBar_duration) {
                    duration = array.getInt(index, duration);
                } else if (index == R.styleable.CircleRotaProgressBar_secondaryAnimatorDelay) {
                    secondaryAnimatorDelay = array.getInt(index, secondaryAnimatorDelay);
                }
            }
            if (secondaryProgressWidth >= progressWidth) {
                throw new RuntimeException("secondaryProgressWidth(" + secondaryProgressWidth + ") must less than progressWidth(" + progressWidth + ")");
            }
        } finally {
            if (array != null)
                array.recycle();
        }
        setGravity(Gravity.CENTER);
        init();
    }

    private void init() {
        sweepAngle = getSweepAngleByProgress(progress);
        pvh = PropertyValuesHolder.ofFloat("sweepAngle", sweepAngle, -360.0f);
        valueAnimator = ValueAnimator.ofPropertyValuesHolder(pvh);
        _valueAnimator = ValueAnimator.ofFloat(sweepAngle, -360.0f);

        paint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        paint.setStrokeCap(Paint.Cap.ROUND); //画笔是圆形模式
        paint.setDither(true);

        valueAnimator.setInterpolator(interpolator);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });

        _valueAnimator.setInterpolator(interpolator);
        _valueAnimator.setDuration(duration);
        _valueAnimator.setStartDelay(secondaryAnimatorDelay);//第二个点动画延时
        _valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!valueAnimator.isRunning()) {
                    invalidate();
                }
            }
        });
        _valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!valueAnimator.isStarted() && state == State.START) { //在第二个点结束动画后，才继续开始第一个动画和第二个动画
                    startAnimation();
                }
            }
        });
    }

    /**
     * 设置进度
     * @param p
     */
    public void setProgress(int p) {
        if (p < 0 || p > 100) {
            throw new RuntimeException("progress(" + p + ") must be betain 0 and 100");
        }
        if (p == progress) {
            return;
        }
        progress = p;
        sweepAngle = getSweepAngleByProgress(progress);
        pvh.setFloatValues(sweepAngle, -360.0f);
        _valueAnimator.setFloatValues(sweepAngle, -360.0f);
        setText(String.valueOf(p) + "%");
    }

    /**
     * 设置running动画时间差值器
     * @param interpolator
     */
    public void setInterpolator(TimeInterpolator interpolator) {
        valueAnimator.setInterpolator(interpolator);
        _valueAnimator.setInterpolator(interpolator);
    }

    /**
     * 设置进度运行状态
     * @param s
     */
    public void setState(State s) {
        if (s == null) {
            throw new NullPointerException("s is null");
        }
        if (s == state) {
            return;
        }
        state = s;
        if (state == State.START) {
            startAnimation();
        } else {
            endAnimation();
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility != getVisibility()) {
            super.setVisibility(visibility);
            if (visibility == GONE || visibility == INVISIBLE) {
                endAnimation();
            } else {
                if (state == State.START) {
                    startAnimation();
                } else {
                    endAnimation();
                }
            }
        }
    }

    /**
     * 重写onMeasure方法，保证是一个正方形
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = 200;
        int w = MeasureSpec.getSize(widthMeasureSpec);
        w = (w > 0) ? w : size;
        int h = MeasureSpec.getSize(heightMeasureSpec);
        h = (h > 0) ? w : size;

        Log.d(TAG, "w = " + w + " h = " + h);

        w += getPaddingLeft() + getPaddingRight();
        h += getPaddingTop() + getPaddingBottom();

        Log.d(TAG, "p w = " + w + " h = " + h);

        final int measuredWidth = resolveSizeAndState(Math.min(w, h), widthMeasureSpec, 0);
        final int measuredHeight = resolveSizeAndState(Math.min(w, h), heightMeasureSpec, 0);

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        /**
         * 进度圆的中心坐标
         */
        float cx = paddingLeft + (width - paddingLeft - paddingRight) / 2.0f;
        float py = paddingTop + (height - paddingTop - paddingBottom) / 2.0f;

        paint.setStyle(Paint.Style.STROKE);

        /**
         * 绘制进度条底圆
         */
        paint.setColor(secondaryProgressColor);
        paint.setStrokeWidth(secondaryProgressWidth);
        canvas.drawCircle(cx, py, (width - paddingLeft - paddingRight) / 2.0f - halfProgressWidth, paint);

        /**
         * 绘制进度条
         */
        paint.setColor(progressColor);
        RectF rectF = new RectF(halfProgressWidth + paddingLeft,
                halfProgressWidth + paddingTop,
                width - halfProgressWidth - paddingRight,
                height - halfProgressWidth - paddingBottom);
        paint.setStrokeWidth(progressWidth);
        canvas.drawArc(rectF, -90.0f, sweepAngle, false, paint);

        paint.setStyle(Paint.Style.FILL);

        /**
         * 绘制第一个动画点
         */
        canvas.save();
        canvas.rotate(((Float) valueAnimator.getAnimatedValue("sweepAngle")).floatValue(), cx, py);
        canvas.drawPoint(cx, halfProgressWidth + paddingTop, paint);
        canvas.restore();

        /**
         * 绘制第二个动画点
         */
        canvas.save();
        canvas.rotate(((Float) _valueAnimator.getAnimatedValue()).floatValue(), cx, py);
        canvas.drawPoint(cx, halfProgressWidth + paddingTop, paint);
        canvas.restore();
    }

    /**
     * 开始动画
     */
    private void startAnimation() {
        valueAnimator.start();
        _valueAnimator.start();
    }

    /**
     * 结束动画
     */
    private void endAnimation() {
        valueAnimator.end();
        _valueAnimator.end();
    }

    /**
     * 根据进度获得绘制进度的弧度
     * @param progress
     * @return
     */
    private float getSweepAngleByProgress(int progress) {
        return (progress / 100.0f) * 360 * -1;
    }
}
