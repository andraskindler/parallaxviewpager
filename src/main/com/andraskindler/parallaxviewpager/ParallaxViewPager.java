package com.andraskindler.parallaxviewpager;

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

public class ParallaxViewPager extends ViewPager {

    public Bitmap bitmap;
    private Rect source, destination;
    private int scaleType;
    private int chunkWidth;
    private int projectedWidth;
    private float overlap;

    public static final int FIT_WIDTH = 0;
    public static final int FIT_HEIGHT = 1;

    public static final float OVERLAP_FULL = 1f;
    public static final float OVERLAP_HALF = 0.5f;
    public static final float OVERLAP_QUARTER = 0.25f;

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

        setOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (bitmap != null) {
                    source.left = (int) ((position + positionOffset) * chunkWidth);
                    source.right = (int) ((position + positionOffset) * chunkWidth + projectedWidth);
                    destination.left = (int) ((position + positionOffset) * getWidth());
                    destination.right = (int) ((position + positionOffset + 1) * getWidth());
                    invalidate();
                }
            }
        });
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        destination.top = 0;
        destination.bottom = h;
        if (getAdapter() != null)
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
                    chunkWidth = (int) ((float) bitmap.getWidth() / (float) getAdapter().getCount());
                    projectedWidth = chunkWidth;
                    break;
                case FIT_HEIGHT:
                default:
                    source.top = 0;
                    source.bottom = bitmap.getHeight();
                    projectedWidth = (int) (getWidth() / ratio);
                    chunkWidth = (int) ((bitmap.getWidth() - projectedWidth) / (float) getAdapter().getCount() * overlap);
                    break;
            }
        }
        System.out.println(chunkWidth);
    }

    @Override public void setBackgroundResource(int resid) {
        bitmap = BitmapFactory.decodeResource(getResources(), resid);
    }

    @Override public void setBackground(Drawable background) {
        bitmap = ((BitmapDrawable) background).getBitmap();
    }

    @Override public void setBackgroundDrawable(Drawable background) {
        setBackground(background);
    }

    public ParallaxViewPager setBackground(Bitmap bitmap) {
        this.bitmap = bitmap;
        return this;
    }

    public ParallaxViewPager setScaleType(final int scaleType) {
        if (scaleType != FIT_WIDTH && scaleType != FIT_HEIGHT)
            throw new IllegalArgumentException("Illegal argument: scaleType must be FIT_WIDTH or FIT_HEIGHT");
        this.scaleType = scaleType;
        return this;
    }

    public ParallaxViewPager setOverlapPercentage(final float percentage) {
        if (percentage <= 0 || percentage >= 1)
            throw new IllegalArgumentException("Illegal argument: percentage must be between 0 and 1");
        overlap = percentage;
        return this;
    }

    public void invalidateParallaxParameters() {
        calculateParallaxParameters();
    }

    @Override protected void onDraw(Canvas canvas) {
        if (bitmap != null)
            canvas.drawBitmap(bitmap, source, destination, null);
    }
}
