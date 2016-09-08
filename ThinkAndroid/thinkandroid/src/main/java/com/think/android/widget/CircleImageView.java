package com.think.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import com.think.android.R;

/**
 * Created by borney on 8/26/16.
 */
public class CircleImageView extends ImageView {
    private static final String TAG = "CircleImageView";

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 半径
     */
    private int radius = 0;

    /**
     * 是否是圆形裁剪
     */
    private boolean isCircle = true;

    /**
     * 裁剪类型
     */
    private int cropType = CropType.CENTER;

    /**
     * 绘制类型
     */
    private int drawType = DrawType.XFERMODE;

    /**
     * 圈形图片的裁剪位置
     */
    public static final class CropType {
        public static final int LEFT_TOP = 1;
        public static final int LEFT_BOTTOM = 2;
        public static final int RIGHT_TOP = 3;
        public static final int RIGHT_BOTTOM = 4;
        public static final int LEFT_CENTER = 5;
        public static final int RIGHT_CENTER = 6;
        public static final int TOP_CENTER = 7;
        public static final int BOTTOM_CENTER = 8;
        public static final int CENTER = 9;
    }

    /**
     * 使用BitmapShaper方式绘制还是Xfermode的方式绘制
     */
    public static final class DrawType {
        public static final int SHADER = 1;
        public static final int XFERMODE = 2;
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = null;
        try {
            array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleImageView, 0, 0);
            int count = array.getIndexCount();
            Log.d(TAG, "count = " + count);
            for (int i = 0; i < count; i++) {
                final int index = array.getIndex(i);
                if (index == R.styleable.CircleImageView_radius) {
                    radius = array.getDimensionPixelSize(index, 0);
                } else if (index == R.styleable.CircleImageView_cropType) {
                    cropType = array.getInteger(index, CropType.CENTER);
                } else if (index == R.styleable.CircleImageView_drawType) {
                    drawType = array.getInteger(index, DrawType.XFERMODE);
                } else if (index == R.styleable.CircleImageView_iscircle) {
                    isCircle = array.getBoolean(index, true);
                }
            }
            Log.d(TAG, "radius = " + radius + " cropType = " + cropType + " drawType = " + drawType + " isCircle = " + isCircle);
        } finally {
            if (array != null) {
                array.recycle();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int intrinsicWidth = getDrawable().getIntrinsicWidth();
        int intrinsicHeight = getDrawable().getIntrinsicHeight();
        Log.d(TAG, "intrinsicWidth = " + intrinsicWidth + " intrinsicHeight = " + intrinsicHeight);
        if (isCircle) {
            int width = resolveAdjustedSize(radius == 0 ? intrinsicWidth : Math.min(intrinsicWidth, radius * 2), Integer.MAX_VALUE, widthMeasureSpec);
            int height = resolveAdjustedSize(radius == 0 ? intrinsicHeight : Math.min(intrinsicHeight, radius * 2), Integer.MAX_VALUE, heightMeasureSpec);

            int border = Math.min(width, height);

            radius = border / 2;

            Log.d(TAG, "isCircle border = " + border + " radius = " + radius);
            setMeasuredDimension(border, border);
        } else {
            int width = resolveAdjustedSize(intrinsicWidth, Integer.MAX_VALUE, widthMeasureSpec);
            int height = resolveAdjustedSize(intrinsicHeight, Integer.MAX_VALUE, heightMeasureSpec);
            radius = Math.min(Math.min(width, height), radius);
            Log.d(TAG, "isCircle not border = " + Math.min(width, height) + " radius = " + radius);
            setMeasuredDimension(width, height);
        }
    }

    public void setRadius(int radius) {
        this.radius = radius;
        requestLayout();
    }

    public void setCropType(int cropType) {
        this.cropType = cropType;
        invalidate();
    }

    public void setDrawType(int drawType) {
        this.drawType = drawType;
        invalidate();
    }

    private int resolveAdjustedSize(int desiredSize, int maxSize,
                                    int measureSpec) {
        int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                /* Parent says we can be as big as we want. Just don't be larger
                   than max size imposed on ourselves.
                */
                result = Math.min(desiredSize, maxSize);
                break;
            case MeasureSpec.AT_MOST:
                // Parent says we can be as big as we want, up to specSize.
                // Don't be larger than specSize, and don't be larger than
                // the max size imposed on ourselves.
                result = Math.min(Math.min(desiredSize, specSize), maxSize);
                break;
            case MeasureSpec.EXACTLY:
                // No choice. Do what we are told.
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        if (getDrawable() == null) {
            return;
        }
        if (drawType == DrawType.XFERMODE) {
            drawByXfermode(canvas);
        } else {
            drawByShader(canvas);
        }
    }

    private void drawByXfermode(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int restore = canvas.saveLayer(0, 0, width, height, null,
            Canvas.ALL_SAVE_FLAG);
        if (isCircle) {
            canvas.drawCircle(radius, radius, radius, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.saveLayer(0, 0, width, height, paint, Canvas.ALL_SAVE_FLAG);
            Bitmap bitmap = drawableToBitmap(getDrawable());
            int[] xy = getCropTypeCircleXY(bitmap);
            Rect src = new Rect();
            src.left = xy[0] - radius;
            src.right = xy[0] + radius;
            src.top = xy[1] - radius;
            src.bottom = xy[1] + radius;
            canvas.drawBitmap(bitmap, src, new Rect(0, 0, width, height), null);
        } else {
            RectF rect = new RectF(0, 0, width, height);
            canvas.drawRoundRect(rect, radius, radius, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.saveLayer(0, 0, width, height, paint, Canvas.ALL_SAVE_FLAG);
            Bitmap bitmap = drawableToBitmap(getDrawable());
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
        canvas.restoreToCount(restore);
        paint.setXfermode(null);
    }

    private void drawByShader(Canvas canvas) {
        Bitmap src = drawableToBitmap(getDrawable());
        if (isCircle) {
            int[] cropTypeCircleXY = getCropTypeCircleXY(src);
            Bitmap bitmap = Bitmap.createBitmap(src, cropTypeCircleXY[0] - radius, cropTypeCircleXY[1] - radius, getWidth(), getHeight());
            BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(bitmapShader);
            canvas.drawCircle(radius, radius, radius, paint);
        } else {
            BitmapShader bitmapShader = new BitmapShader(src, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(bitmapShader);
            int width = getWidth();
            int height = getHeight();
            RectF rect = new RectF(0, 0, width, height);
            canvas.drawRoundRect(rect, radius, radius, paint);
        }
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return bitmap;
    }


    private int[] getCropTypeCircleXY(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] xy;
        switch (cropType) {
            case CropType.LEFT_TOP:
                xy = new int[]{radius, radius};
                break;
            case CropType.LEFT_BOTTOM:
                xy = new int[]{radius, height - radius};
                break;
            case CropType.RIGHT_TOP:
                xy = new int[]{width - radius, radius};
                break;
            case CropType.RIGHT_BOTTOM:
                xy = new int[]{width - radius, height - radius};
                break;
            case CropType.LEFT_CENTER:
                xy = new int[]{radius, height / 2};
                break;
            case CropType.RIGHT_CENTER:
                xy = new int[]{width - radius, height / 2};
                break;
            case CropType.TOP_CENTER:
                xy = new int[]{width / 2, radius};
                break;
            case CropType.BOTTOM_CENTER:
                xy = new int[]{width / 2, height - radius};
                break;
            case CropType.CENTER:
                xy = new int[]{width / 2, height / 2};
                break;
            default:
                xy = new int[]{width / 2, height / 2};
                break;
        }
        return xy;
    }
}
