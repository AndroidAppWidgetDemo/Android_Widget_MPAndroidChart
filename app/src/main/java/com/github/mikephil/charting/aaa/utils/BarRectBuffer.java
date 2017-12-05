
package com.github.mikephil.charting.aaa.utils;

import com.github.mikephil.charting.aaa.model.bar.BarEntry;
import com.github.mikephil.charting.aaa.model.bar.BarEntrySet;

/**
 * 由坐标点，转化为要绘制的矩形
 */
public class BarRectBuffer {


    /**
     * index in the buffer
     */
    protected int index = 0;

    /**
     * float-buffer that holds the data points to draw, order: left,top,right,bottom
     */
    public final float[] buffer;


    protected int mDataSetIndex = 0;

    /**
     * width of the bar on the x-axis, in values (not pixels)
     * <p>
     * 矩形宽
     */
    protected float mBarWidth = 1f;


    /** animation phase x-axis */
    protected float phaseX = 1f;

    /** animation phase y-axis */
    protected float phaseY = 1f;


    /**
     * @param size BarEntrySet.getEntryCount() * 4
     */
    public BarRectBuffer(int size) {
        //
        index = 0;
        // BarEntrySet.getEntryCount() * 4
        buffer = new float[size];
    }

    /**
     * 添加一个矩形
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    protected void addBar(float left, float top, float right, float bottom) {
        buffer[index++] = left;
        buffer[index++] = top;
        buffer[index++] = right;
        buffer[index++] = bottom;
    }

    /**
     * @param data
     */
    public void feed(BarEntrySet data) {
        // 矩形的数量
        float size = data.getEntryCount();
        // 矩形 宽的一半
        float barWidthHalf = mBarWidth / 2f;
        //
        for (int i = 0; i < size; i++) {
            BarEntry e = data.getEntryForIndex(i);
            //
            if (e == null) {
                continue;
            }
            // 坐标点
            float x = e.getX();
            float y = e.getY();
            // 构建矩形的四个点
            float left = x - barWidthHalf;
            float right = x + barWidthHalf;
            float top = y >= 0 ? y : 0;
            float bottom = y <= 0 ? y : 0;

            // multiply the height of the rect with the phase
            top *= phaseY;
            bottom *= phaseY;

            // 添加一个矩形
            addBar(left, top, right, bottom);
        }
        //
        reset();
    }


    public void setBarWidth(float barWidth) {
        this.mBarWidth = barWidth;
    }

    public void setDataSet(int index) {
        this.mDataSetIndex = index;
    }

    /**
     * Resets the buffer index to 0 and makes the buffer reusable.
     */
    public void reset() {
        index = 0;
    }

    /**
     * Returns the size (length) of the buffer array.
     *
     * @return
     */
    public int size() {
        return buffer.length;
    }

    /**
     * Set the phases used for animations.
     *
     * @param phaseX
     * @param phaseY
     */
    public void setPhases(float phaseX, float phaseY) {
        this.phaseX = phaseX;
        this.phaseY = phaseY;
    }

}
