
package com.github.mikephil.charting.aaa.utils;

import android.graphics.Matrix;

/**
 * Transformer class that contains all matrices and is responsible for
 * transforming values into pixels on the screen and backwards.
 *
 */
public class Transformer {

    /**
     * matrix to map the values to the screen pixels
     */
    protected Matrix mMatrixValueToPx = new Matrix();

    /**
     * matrix for handling the different offsets of the chart
     */
    protected Matrix mMatrixOffset = new Matrix();

    protected ViewPort mViewPortHandler;

    public Transformer(ViewPort viewPortHandler) {
        this.mViewPortHandler = viewPortHandler;
    }

    /**
     * Prepares the matrix that transforms values to pixels. Calculates the
     * scale factors from the charts size and offsets.
     *
     * @param xChartMin
     * @param deltaX
     * @param deltaY
     * @param yChartMin
     */
    public void prepareMatrixValuePx(float xChartMin, float deltaX, float deltaY, float yChartMin) {

        float scaleX = mViewPortHandler.contentWidth() / deltaX;
        float scaleY = mViewPortHandler.contentHeight() / deltaY;

        if (Float.isInfinite(scaleX)) {
            scaleX = 0;
        }
        if (Float.isInfinite(scaleY)) {
            scaleY = 0;
        }

        // setup all matrices
        mMatrixValueToPx.reset();
        // 平移
        mMatrixValueToPx.postTranslate(-xChartMin, -yChartMin);
        // 缩放
        mMatrixValueToPx.postScale(scaleX, -scaleY);
    }

    /**
     * Prepares the matrix that contains all offsets.
     */
    public void prepareMatrixOffset() {
        //
        mMatrixOffset.reset();
        // 平移
        mMatrixOffset.postTranslate(
                mViewPortHandler.offsetLeft(),
                mViewPortHandler.getChartHeight() - mViewPortHandler.offsetBottom());
    }


    /**
     * Transform an array of points with all matrices. VERY IMPORTANT: Keep
     * matrix order "value-touch-offset" when transforming.
     *
     * @param pts
     */
    public void pointValuesToPixel(float[] pts) {
        mMatrixValueToPx.mapPoints(pts);
        mMatrixOffset.mapPoints(pts);
    }
}
