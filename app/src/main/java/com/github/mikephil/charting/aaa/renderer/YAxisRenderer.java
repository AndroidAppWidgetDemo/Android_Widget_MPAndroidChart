package com.github.mikephil.charting.aaa.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Align;

import com.github.mikephil.charting.aaa.utils.ViewPort;
import com.github.mikephil.charting.aaa.model.axis.YAxis;
import com.github.mikephil.charting.aaa.utils.Transformer;
import com.github.mikephil.charting.aaa.utils.Utils;

public class YAxisRenderer extends AxisRenderer {

    protected YAxis mYAxis;

    /**
     * @param viewPortHandler
     * @param yAxis
     * @param trans
     */
    public YAxisRenderer(ViewPort viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, trans, yAxis);
        //
        this.mYAxis = yAxis;
        //
        if (mViewPort != null) {
            mAxisLabelPaint.setColor(Color.BLACK);
            mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10f));
        }
    }

    /**
     * draws the y-axis labels to the screen
     */
    @Override
    public void renderAxisLabels(Canvas c) {
        //
        if (!mYAxis.isEnabled() || !mYAxis.isDrawLabelsEnabled()) {
            return;
        }
        // 文字大小
        mAxisLabelPaint.setTextSize(mYAxis.getTextSize());
        // 文字颜色
        mAxisLabelPaint.setColor(mYAxis.getTextColor());
        mAxisLabelPaint.setTextAlign(Align.RIGHT);

        //
        float[] positions = getTransformedPositions();
        //
        float xoffset = mYAxis.getXOffset();
        float yoffset = Utils.calcTextHeight(mAxisLabelPaint, "A") / 2.5f + mYAxis.getYOffset();
        //
        float xPos = mViewPort.offsetLeft() - xoffset;

        drawYLabels(c, xPos, positions, yoffset);
    }

    /**
     * 绘制坐标轴
     *
     * @param c
     */
    @Override
    public void renderAxisLine(Canvas c) {

        if (!mYAxis.isEnabled()) {
            return;
        }
        // 坐标轴颜色
        mAxisLinePaint.setColor(mYAxis.getAxisLineColor());
        // 坐标轴宽度
        mAxisLinePaint.setStrokeWidth(mYAxis.getAxisLineWidth());
        // 绘制坐标轴
        c.drawLine(
                mViewPort.contentLeft(),
                mViewPort.contentTop(),
                mViewPort.contentLeft(),
                mViewPort.contentBottom(), mAxisLinePaint);
    }

    /**
     * draws the y-labels on the specified x-position
     *
     * @param fixedPosition
     * @param positions
     */
    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {

        final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        final int to = mYAxis.isDrawTopYLabelEntryEnabled()
                ? mYAxis.mEntryCount
                : (mYAxis.mEntryCount - 1);

        // draw
        for (int i = from; i < to; i++) {
            // 要绘制的文字
            String text = mYAxis.getFormattedLabel(i);
            //
            c.drawText(text, fixedPosition, positions[i * 2 + 1] + offset, mAxisLabelPaint);
        }
    }


    protected float[] mGetTransformedPositionsBuffer = new float[2];

    /**
     * Transforms the values contained in the axis entries to screen pixels and returns them in form of a float array
     * of x- and y-coordinates.
     *
     * @return
     */
    protected float[] getTransformedPositions() {

        if (mGetTransformedPositionsBuffer.length != mYAxis.mEntryCount * 2) {
            mGetTransformedPositionsBuffer = new float[mYAxis.mEntryCount * 2];
        }
        float[] positions = mGetTransformedPositionsBuffer;

        for (int i = 0; i < positions.length; i += 2) {
            // only fill y values, x values are not needed for y-labels
            positions[i + 1] = mYAxis.mEntries[i / 2];
        }

        mTrans.pointValuesToPixel(positions);
        return positions;
    }

}
