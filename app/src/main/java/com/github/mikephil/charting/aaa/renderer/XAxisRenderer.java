
package com.github.mikephil.charting.aaa.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;

import com.github.mikephil.charting.aaa.model.axis.XAxis;
import com.github.mikephil.charting.aaa.utils.Transformer;
import com.github.mikephil.charting.aaa.utils.Utils;
import com.github.mikephil.charting.aaa.utils.ViewPort;

public class XAxisRenderer extends AxisRenderer {

    protected XAxis mXAxis;

    public XAxisRenderer(ViewPort viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, trans, xAxis);
        //
        this.mXAxis = xAxis;
        //
        mAxisLabelPaint.setColor(Color.BLACK);
        mAxisLabelPaint.setTextAlign(Align.CENTER);
        mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10f));
    }


    @Override
    protected void computeAxisValues(float min, float max) {
        super.computeAxisValues(min, max);
        computeSize();
    }

    protected void computeSize() {
        // 坐标轴上最长的文字
        String longest = mXAxis.getLongestLabel();

        // Paint 设置
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());
        // 字符的宽度，字符的高度
        final float labelWidth = Utils.calcTextWidth(mAxisLabelPaint, longest);
        final float labelHeight = Utils.calcTextHeight(mAxisLabelPaint, "Q");
        // 字符 宽 高
        mXAxis.mLabelWidth = Math.round(labelWidth);
        mXAxis.mLabelHeight = Math.round(labelHeight);
    }

    @Override
    public void renderAxisLabels(Canvas c) {
        //
        if (!mXAxis.isEnabled() || !mXAxis.isDrawLabelsEnabled()) {
            return;
        }
        //
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());
        mAxisLabelPaint.setColor(mXAxis.getTextColor());
        //
        float yoffset = mXAxis.getYOffset();
        drawLabels(c, mViewPort.contentBottom() + yoffset);
    }

    /**
     * 绘制X轴
     *
     * @param c
     */
    @Override
    public void renderAxisLine(Canvas c) {
        // 是否绘制的判断
        if (!mXAxis.isEnabled()) {
            return;
        }
        // 坐标轴的颜色
        mAxisLinePaint.setColor(mXAxis.getAxisLineColor());
        // 坐标轴的宽度
        mAxisLinePaint.setStrokeWidth(mXAxis.getAxisLineWidth());
        // 画线
        c.drawLine(
                mViewPort.contentLeft(),
                mViewPort.contentBottom(),
                mViewPort.contentRight(),
                mViewPort.contentBottom(), mAxisLinePaint);
    }

    /**
     * draws the x-labels on the specified y-position
     * <p>
     * 绘制坐标轴上的标签
     *
     * @param pos
     */
    protected void drawLabels(Canvas c, float pos) {
        // value转化为坐标点
        float[] positions = new float[mXAxis.mEntryCount * 2];
        for (int i = 0; i < positions.length; i += 2) {
            positions[i] = mXAxis.mEntries[i / 2];
        }
        mTrans.pointValuesToPixel(positions);
        // 绘制坐标点
        for (int i = 0; i < positions.length; i += 2) {
            float x = positions[i];
            if (mViewPort.isInBoundsX(x)) {
                //
                String label = mXAxis.getValueFormatter().getFormattedValue(mXAxis.mEntries[i / 2], mXAxis);
                drawLabel(c, label, x, pos);
            }
        }
    }

    /**
     * 绘制标签
     *
     * @param c
     * @param formattedLabel
     * @param x
     * @param y
     */
    protected void drawLabel(Canvas c, String formattedLabel, float x, float y) {
        drawXAxisValue(c, formattedLabel, x, y, mAxisLabelPaint);
    }


    private Rect mDrawTextRectBuffer = new Rect();
    private Paint.FontMetrics mFontMetricsBuffer = new Paint.FontMetrics();
    public void drawXAxisValue(Canvas c,
                                      String text,
                                      float x, float y,
                                      Paint paint) {

        float drawOffsetX = 0.f;
        float drawOffsetY = 0.f;

        paint.getFontMetrics(mFontMetricsBuffer);
        paint.getTextBounds(text, 0, text.length(), mDrawTextRectBuffer);
        // To have a consistent point of reference, we always draw left-aligned
        paint.setTextAlign(Paint.Align.LEFT);


        // Android does not snap the bounds to line boundaries,
        //  and draws from bottom to top.
        // And we want to normalize it.
        drawOffsetY += -mFontMetricsBuffer.ascent;
        //
        drawOffsetX -= mDrawTextRectBuffer.width() * 0.5f;
        //
        drawOffsetX += x;
        drawOffsetY += y;
        //
        c.drawText(text, drawOffsetX, drawOffsetY, paint);
    }
}
