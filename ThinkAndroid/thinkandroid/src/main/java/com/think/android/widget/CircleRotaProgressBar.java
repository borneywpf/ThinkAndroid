package com.think.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.think.android.R;

/**
 * Created by borney on 9/8/16.
 */
public class CircleRotaProgressBar extends View {
    private static final String TAG = "TAGCircleRotaProgressBar";
    private int progress = 0;
    private int progressWidth = 4;
    private State state = State.PAUSE;

    public CircleRotaProgressBar(Context context) {
        this(context, null);
    }

    public CircleRotaProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleRotaProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleRotaProgressBar, defStyleAttr, 0);
        progressWidth = array.getDimensionPixelSize(R.styleable.CircleRotaProgressBar_progressWidth, 0);
        Log.d(TAG, "progressWidth = " + progressWidth);
        array.recycle();
    }

    public enum State {
        START,

        PAUSE;
    }

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    {
        paint.setStyle(Paint.Style.STROKE);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public void setState(State s) {
        state = s;
        invalidate();
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility != getVisibility()) {
            super.setVisibility(visibility);
            if (visibility == GONE || visibility == INVISIBLE) {

            } else {

            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(progressWidth / 2.0f);
        paint.setAlpha(100);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, paint);
        RectF rectF = new RectF(0, 0, getWidth(), getHeight());
        float sweepAngle = (progress / 100.0f) * 360 * -1;
        paint.setStrokeWidth(progressWidth);
        paint.setAlpha(255);
        canvas.drawArc(rectF, -90, sweepAngle, false, paint);
    }
}
