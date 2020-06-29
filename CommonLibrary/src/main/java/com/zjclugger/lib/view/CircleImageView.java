package com.zjclugger.lib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;

import com.zjclugger.lib.R;

/**
 * <br>
 * Created by King.Zi on 2020/4/9.<br>
 * Copyright (c) 2020 zjclugger.com
 */
@SuppressLint("AppCompatCustomView")
public class CircleImageView extends ImageView {
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 2;

    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final int DEFAULT_CIRCLE_BACKGROUND_COLOR = Color.TRANSPARENT;
    private static final boolean DEFAULT_BORDER_OVERLAY = false;

    private final RectF mDrawableRect = new RectF();
    private final RectF mBorderRect = new RectF();

    private final Matrix mShaderMatrix = new Matrix();
    private final Paint mBitmapPaint = new Paint();
    private final Paint mBorderPaint = new Paint();
    private final Paint mCircleBackgroundPaint = new Paint();

    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;
    private int mCircleBackgroundColor = DEFAULT_CIRCLE_BACKGROUND_COLOR;

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private int mBitmapHeight;

    private float mDrawableRadius;
    private float mBorderRadius;

    private ColorFilter mColorFilter;

    private boolean mReady;
    private boolean mSetupPending;
    private boolean mBorderOverlay;
    private boolean mDisableCircularTransformation;

    // 内圆字体画笔
    private Paint mTextPaint;
    // 字的长度
    private float mTextWidth;
    // 字的高度
    private float mTextHeight;

    // 字体颜色
    private int mTextColor;
    private float mTextSize;
    private String mCenterText;
    private float mBackgroundRadius;
    private int mBackgroundColor;
    private int mBackgroundStrokeWidth;
    private int mBackgroundStrokeColor;
    private boolean mIsShowCircle = true;


    public CircleImageView(Context context) {
        super(context);
        init();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView,
                defStyle, 0);

        mBorderWidth = typeArray.getDimensionPixelSize(R.styleable.CircleImageView_civ_border_width,
                DEFAULT_BORDER_WIDTH);
        mBorderColor = typeArray.getColor(R.styleable.CircleImageView_civ_border_color,
                DEFAULT_BORDER_COLOR);
        mBorderOverlay = typeArray.getBoolean(R.styleable.CircleImageView_civ_border_overlay,
                DEFAULT_BORDER_OVERLAY);

        mIsShowCircle = typeArray.getBoolean(R.styleable.CircleImageView_civ_show_circle, true);
        mTextColor = typeArray.getColor(R.styleable.CircleImageView_civ_text_color, 0xFFFFFFFF);
        mTextSize = typeArray.getDimension(R.styleable.CircleImageView_civ_text_size, 14);
        mCenterText = typeArray.getString(R.styleable.CircleImageView_civ_text);
        mBackgroundRadius =
                typeArray.getDimension(R.styleable.CircleImageView_civ_background_radius, 10);
        mBackgroundStrokeWidth =
                typeArray.getDimensionPixelSize(R.styleable.CircleImageView_civ_background_stroke_width, 10);
        mBackgroundColor = typeArray.getColor(R.styleable.CircleImageView_civ_background_color,
                0xFFFFFFFF);
        mBackgroundStrokeColor =
                typeArray.getColor(R.styleable.CircleImageView_civ_background_stroke_color,
                        0xFFFFFFFF);

        // Look for deprecated civ_fill_color if civ_circle_background_color is not set
        if (typeArray.hasValue(R.styleable.CircleImageView_civ_circle_background_color)) {
            mCircleBackgroundColor =
                    typeArray.getColor(R.styleable.CircleImageView_civ_circle_background_color,
                            DEFAULT_CIRCLE_BACKGROUND_COLOR);
        } else if (typeArray.hasValue(R.styleable.CircleImageView_civ_fill_color)) {
            mCircleBackgroundColor = typeArray.getColor(R.styleable.CircleImageView_civ_fill_color,
                    DEFAULT_CIRCLE_BACKGROUND_COLOR);
        }

        typeArray.recycle();

        init();
    }

    private void init() {
        super.setScaleType(SCALE_TYPE);
        mReady = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new OutlineProvider());
        }

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(@ColorInt int color) {
        mTextColor = color;
        setup();
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float size) {
        mTextSize = size;
        setup();
    }

    public String getCenterText() {
        return mCenterText;
    }

    public void setCenterText(String text) {
        mCenterText = text;
        setup();
    }

    public float getBackgroundRadius() {
        return mBackgroundRadius;
    }

    public void setBackgroundRadius(float radius) {
        mBackgroundRadius = radius;
        setup();
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(@ColorInt int color) {
        mBackgroundColor = color;
        setup();
    }

    public int getBackgroundStrokeWidth() {
        return mBackgroundStrokeWidth;
    }

    public void setBackgroundStrokeWidth(int width) {
        mBackgroundStrokeWidth = width;
        setup();
    }

    public int getBackgroundStrokeColor() {
        return mBackgroundStrokeColor;
    }

    public void setBackgroundStrokeColor(@ColorInt int color) {
        mBackgroundStrokeColor = color;
        setup();
    }

    public boolean isShowCircle() {
        return mIsShowCircle;
    }

    public void setShowCircle(boolean isShowCircle) {
        mIsShowCircle = isShowCircle;
        setup();
    }

    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.",
                    scaleType));
        }
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mIsShowCircle) {
            if (mDisableCircularTransformation) {
                super.onDraw(canvas);
                return;
            }

            if (mBitmap == null) {
                return;
            }

            if (mCircleBackgroundColor != Color.TRANSPARENT) {
                canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius,
                        mCircleBackgroundPaint);
            }
            canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius,
                    mBitmapPaint);
            if (mBorderWidth > 0) {
                canvas.drawCircle(mBorderRect.centerX(), mBorderRect.centerY(), mBorderRadius,
                        mBorderPaint);
            }
            if (!TextUtils.isEmpty(mCenterText)) {
                mTextWidth = mTextPaint.measureText(mCenterText, 0, mCenterText.length());
                canvas.drawText(mCenterText, mDrawableRect.centerX() - mTextWidth / 2,
                        mDrawableRect.centerY() + mTextHeight / 4, mTextPaint);
            }
        } else {
            super.onDraw(canvas);
        }
    }

    private void setBackgroundStyle() {
        GradientDrawable background = new GradientDrawable();
        background.setStroke(mBackgroundStrokeWidth, mBackgroundStrokeColor);
        /*view background color*/
        background.setColor(mBackgroundColor);
        background.setCornerRadius(mBackgroundRadius);
        setBackground(background);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        setup();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        setup();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(@ColorInt int borderColor) {
        if (borderColor == mBorderColor) {
            return;
        }

        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }

    /**
     * @deprecated Use {@link #setBorderColor(int)} instead
     */
    @Deprecated
    public void setBorderColorResource(@ColorRes int borderColorRes) {
        setBorderColor(getContext().getResources().getColor(borderColorRes));
    }

    public int getCircleBackgroundColor() {
        return mCircleBackgroundColor;
    }

    public void setCircleBackgroundColor(@ColorInt int circleBackgroundColor) {
        if (circleBackgroundColor == mCircleBackgroundColor) {
            return;
        }

        mCircleBackgroundColor = circleBackgroundColor;
        mCircleBackgroundPaint.setColor(circleBackgroundColor);
        invalidate();
    }

    public void setCircleBackgroundColorResource(@ColorRes int circleBackgroundRes) {
        setCircleBackgroundColor(getContext().getResources().getColor(circleBackgroundRes));
    }

    /**
     * Return the color drawn behind the circle-shaped drawable.
     *
     * @return The color drawn behind the drawable
     * @deprecated Use {@link #getCircleBackgroundColor()} instead.
     */
    @Deprecated
    public int getFillColor() {
        return getCircleBackgroundColor();
    }

    /**
     * Set a color to be drawn behind the circle-shaped drawable. Note that
     * this has no effect if the drawable is opaque or no drawable is set.
     *
     * @param fillColor The color to be drawn behind the drawable
     * @deprecated Use {@link #setCircleBackgroundColor(int)} instead.
     */
    @Deprecated
    public void setFillColor(@ColorInt int fillColor) {
        setCircleBackgroundColor(fillColor);
    }

    /**
     * Set a color to be drawn behind the circle-shaped drawable. Note that
     * this has no effect if the drawable is opaque or no drawable is set.
     *
     * @param fillColorRes The color resource to be resolved to a color and
     *                     drawn behind the drawable
     * @deprecated Use {@link #setCircleBackgroundColorResource(int)} instead.
     */
    @Deprecated
    public void setFillColorResource(@ColorRes int fillColorRes) {
        setCircleBackgroundColorResource(fillColorRes);
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        if (borderWidth == mBorderWidth) {
            return;
        }

        mBorderWidth = borderWidth;
        setup();
    }

    public boolean isBorderOverlay() {
        return mBorderOverlay;
    }

    public void setBorderOverlay(boolean borderOverlay) {
        if (borderOverlay == mBorderOverlay) {
            return;
        }

        mBorderOverlay = borderOverlay;
        setup();
    }

    public boolean isDisableCircularTransformation() {
        return mDisableCircularTransformation;
    }

    public void setDisableCircularTransformation(boolean disableCircularTransformation) {
        if (mDisableCircularTransformation == disableCircularTransformation) {
            return;
        }

        mDisableCircularTransformation = disableCircularTransformation;
        initializeBitmap();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (cf == mColorFilter) {
            return;
        }

        mColorFilter = cf;
        applyColorFilter();
        invalidate();
    }

    @Override
    public ColorFilter getColorFilter() {
        return mColorFilter;
    }

    private void applyColorFilter() {
        if (mBitmapPaint != null) {
            mBitmapPaint.setColorFilter(mColorFilter);
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION,
                        BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initializeBitmap() {
        if (mDisableCircularTransformation) {
            mBitmap = null;
        } else {
            mBitmap = getBitmapFromDrawable(getDrawable());
        }
        setup();
    }

    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (getWidth() == 0 && getHeight() == 0) {
            return;
        }

        if (mBitmap == null) {
            invalidate();
            return;
        }

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mCircleBackgroundPaint.setStyle(Paint.Style.FILL);
        mCircleBackgroundPaint.setAntiAlias(true);
        mCircleBackgroundPaint.setColor(mCircleBackgroundColor);

        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();

        mBorderRect.set(calculateBounds());
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2.0f,
                (mBorderRect.width() - mBorderWidth) / 2.0f);

        mDrawableRect.set(mBorderRect);
        if (!mBorderOverlay && mBorderWidth > 0) {
            mDrawableRect.inset(mBorderWidth - 1.0f, mBorderWidth - 1.0f);
        }
        mDrawableRadius =
                Math.min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f) - mBackgroundStrokeWidth;

        //中间字
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mTextHeight = (int) Math.ceil(fm.descent - fm.ascent);

        applyColorFilter();
        updateShaderMatrix();
        setBackgroundStyle();
        invalidate();
    }

    private RectF calculateBounds() {
        int availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        int sideLength = Math.min(availableWidth, availableHeight);

        float left = getPaddingLeft() + (availableWidth - sideLength) / 2f;
        float top = getPaddingTop() + (availableHeight - sideLength) / 2f;

        return new RectF(left, top, left + sideLength, top + sideLength);
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left,
                (int) (dy + 0.5f) + mDrawableRect.top);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class OutlineProvider extends ViewOutlineProvider {
        @Override
        public void getOutline(View view, Outline outline) {
            Rect bounds = new Rect();
            mBorderRect.roundOut(bounds);
            outline.setRoundRect(bounds, bounds.width() / 2.0f);
        }
    }
}