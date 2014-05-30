package com.andraskindler.parallaxviewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

@SuppressLint("NewApi")
public class ParallaxViewPager extends ViewPager {

    public static final int FIT_WIDTH = 0;
    public static final int FIT_HEIGHT = 1;
    public static final float OVERLAP_FULL = 1f;
    public static final float OVERLAP_HALF = 0.5f;
    public static final float OVERLAP_QUARTER = 0.25f;
    private static final float CORRECTION_PERCENTAGE = 0.01f;
    public Bitmap bitmap;
    private Rect source, destination;
    private int scaleType;
    private int chunkWidth;
    private int projectedWidth;
    private float overlap;
    private OnPageChangeListener secondOnPageChangeListener;

    public ParallaxViewPager(Context context) {
        super(context);
        init();
    }

    public ParallaxViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        source = new Rect();
        destination = new Rect();
        scaleType = FIT_HEIGHT;
        overlap = OVERLAP_HALF;

        setOnPageChangeListener(new OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (bitmap != null) {
                    source.left = (int) Math.floor((position + positionOffset - CORRECTION_PERCENTAGE) * chunkWidth);
                    source.right = (int) Math.ceil((position + positionOffset + CORRECTION_PERCENTAGE) * chunkWidth + projectedWidth);
                    destination.left = (int) Math.floor((position + positionOffset - CORRECTION_PERCENTAGE) * getWidth());
                    destination.right = (int) Math.ceil((position + positionOffset + 1 + CORRECTION_PERCENTAGE) * getWidth());
                    invalidate();
                }

                if (secondOnPageChangeListener != null) {
                    secondOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override public void onPageSelected(int position) {
                if (secondOnPageChangeListener != null) {
                    secondOnPageChangeListener.onPageSelected(position);
                }
            }

            @Override public void onPageScrollStateChanged(int state) {
                if (secondOnPageChangeListener != null) {
                    secondOnPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        destination.top = 0;
        destination.bottom = h;
        if (getAdapter() != null && bitmap != null)
            calculateParallaxParameters();
    }

    private void calculateParallaxParameters() {
        if (bitmap.getWidth() < getWidth() && bitmap.getWidth() < bitmap.getHeight() && scaleType == FIT_HEIGHT) {
            Log.w(ParallaxViewPager.class.getName(), "Invalid bitmap bounds for the current device, parallax effect will not work.");
        }

        final float ratio = (float) getHeight() / bitmap.getHeight();
        if (ratio != 1) {
            switch (scaleType) {
                case FIT_WIDTH:
                    source.top = (int) ((bitmap.getHeight() - bitmap.getHeight() / ratio) / 2);
                    source.bottom = bitmap.getHeight() - source.top;
                    chunkWidth = (int) Math.ceil((float) bitmap.getWidth() / (float) getAdapter().getCount());
                    projectedWidth = chunkWidth;
                    break;
                case FIT_HEIGHT:
                default:
                    source.top = 0;
                    source.bottom = bitmap.getHeight();
                    projectedWidth = (int) Math.ceil(getWidth() / ratio);
                    chunkWidth = (int) Math.ceil((bitmap.getWidth() - projectedWidth) / (float) getAdapter().getCount() * overlap);
                    break;
            }
        }
    }

    /**
     * Sets the background from a resource file.
     *
     * @param resid
     */
    @Override public void setBackgroundResource(int resid) {
        bitmap = BitmapFactory.decodeResource(getResources(), resid);
    }

    /**
     * Sets the background from a Drawable.
     *
     * @param background
     */
    @Override public void setBackground(Drawable background) {
        bitmap = ((BitmapDrawable) background).getBitmap();
    }

    /**
     * Deprecated.
     * Sets the background from a Drawable.
     *
     * @param background
     */
    @Override public void setBackgroundDrawable(Drawable background) {
        bitmap = ((BitmapDrawable) background).getBitmap();
    }

    /**
     * Sets the background from a bitmap.
     *
     * @param bitmap
     * @return The ParallaxViewPager object itself.
     */
    public ParallaxViewPager setBackground(Bitmap bitmap) {
        this.bitmap = bitmap;
        return this;
    }

    /**
     * Sets how the view should scale the background. The available choices are:
     * <ul>
     * <li>FIT_HEIGHT - the height of the image is resized to matched the height of the View, also stretching the width to keep the aspect ratio. The non-visible part of the bitmap is divided into equal parts, each of them sliding in at the proper position.</li>
     * <li>FIT_WIDTH - the width of the background image is divided into equal chunks, each taking up the whole width of the screen.</li>
     * </ul>
     *
     * @param scaleType
     * @return
     */
    public ParallaxViewPager setScaleType(final int scaleType) {
        if (scaleType != FIT_WIDTH && scaleType != FIT_HEIGHT)
            throw new IllegalArgumentException("Illegal argument: scaleType must be FIT_WIDTH or FIT_HEIGHT");
        this.scaleType = scaleType;
        return this;
    }

    /**
     * Sets the amount of overlapping with the setOverlapPercentage(final float percentage) method. This is a number between 0 and 1, the smaller it is, the slower is the background scrolling.
     *
     * @param percentage
     * @return The ParallaxViewPager object itself.
     */
    public ParallaxViewPager setOverlapPercentage(final float percentage) {
        if (percentage <= 0 || percentage >= 1)
            throw new IllegalArgumentException("Illegal argument: percentage must be between 0 and 1");
        overlap = percentage;
        return this;
    }

    /**
     * Recalculates the parameters of the parallax effect, useful after changes in runtime.
     *
     * @return The ParallaxViewPager object itself.
     */
    public ParallaxViewPager invalidateParallaxParameters() {
        calculateParallaxParameters();
        return this;
    }

    @Override protected void onDraw(Canvas canvas) {
        if (bitmap != null)
            canvas.drawBitmap(bitmap, source, destination, null);
    }

    public void addOnPageChangeListener(OnPageChangeListener listener) {
        secondOnPageChangeListener = listener;
    }
}
