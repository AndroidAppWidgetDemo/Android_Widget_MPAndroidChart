
package com.github.mikephil.charting.aaa.utils;

import android.graphics.RectF;

/**
 * Class that contains information about the charts current viewport settings, including offsets, scale & translation
 * levels, ...
 *
 * 窗口的矩形
 */
public class ViewPort {


    /**
     * this rectangle defines the area in which graph values can be drawn
     * <p>
     * View的Rect
     */
    protected RectF mContentRect = new RectF();

    // View的宽
    protected float mChartWidth = 0f;
    // View的高
    protected float mChartHeight = 0f;


    /**
     * Constructor - don't forget calling setChartDimens(...)
     */
    public ViewPort() {

    }

    /**
     * Sets the width and height of the chart.
     *
     * @param width  View的宽
     * @param height View的高
     */

    public void setChartDimens(float width, float height) {
        //
        float offsetLeft = this.offsetLeft();
        float offsetTop = this.offsetTop();
        float offsetRight = this.offsetRight();
        float offsetBottom = this.offsetBottom();
        // View的高
        mChartHeight = height;
        // View的宽
        mChartWidth = width;
        //
        restrainViewPort(offsetLeft, offsetTop, offsetRight, offsetBottom);
    }

    /**
     * @param offsetLeft
     * @param offsetTop
     * @param offsetRight
     * @param offsetBottom
     */
    public void restrainViewPort(float offsetLeft, float offsetTop, float offsetRight,
                                 float offsetBottom) {
        mContentRect.set(
                offsetLeft,
                offsetTop,
                mChartWidth - offsetRight,
                mChartHeight - offsetBottom);
    }

    public float offsetLeft() {
        return mContentRect.left;
    }

    public float offsetRight() {
        return mChartWidth - mContentRect.right;
    }

    public float offsetTop() {
        return mContentRect.top;
    }

    public float offsetBottom() {
        return mChartHeight - mContentRect.bottom;
    }

    public float contentTop() {
        return mContentRect.top;
    }

    public float contentLeft() {
        return mContentRect.left;
    }

    public float contentRight() {
        return mContentRect.right;
    }

    public float contentBottom() {
        return mContentRect.bottom;
    }

    public float contentWidth() {
        return mContentRect.width();
    }

    public float contentHeight() {
        return mContentRect.height();
    }

    public RectF getContentRect() {
        return mContentRect;
    }


    public float getChartHeight() {
        return mChartHeight;
    }

    public float getChartWidth() {
        return mChartWidth;
    }

    /**
     * BELOW METHODS FOR BOUNDS CHECK
     */

    public boolean isInBoundsX(float x) {
        return isInBoundsLeft(x) && isInBoundsRight(x);
    }

    public boolean isInBoundsY(float y) {
        return isInBoundsTop(y) && isInBoundsBottom(y);
    }

    public boolean isInBoundsLeft(float x) {
        return mContentRect.left <= x + 1;
    }

    public boolean isInBoundsRight(float x) {
        x = (float) ((int) (x * 100.f)) / 100.f;
        return mContentRect.right >= x - 1;
    }

    public boolean isInBoundsTop(float y) {
        return mContentRect.top <= y;
    }

    public boolean isInBoundsBottom(float y) {
        y = (float) ((int) (y * 100.f)) / 100.f;
        return mContentRect.bottom >= y;
    }


}
