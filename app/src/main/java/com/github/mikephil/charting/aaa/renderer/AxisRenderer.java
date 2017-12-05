
package com.github.mikephil.charting.aaa.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.github.mikephil.charting.aaa.model.axis.AxisBase;
import com.github.mikephil.charting.aaa.utils.Transformer;
import com.github.mikephil.charting.aaa.utils.ViewPort;

/**
 * Baseclass of all axis renderers.
 *
 */
public abstract class AxisRenderer extends Renderer {


    /**
     *
     */
    // base axis this axis renderer works with
    protected AxisBase mAxis;

    /**
     *
     */
    // paint for the x-label values
    protected Paint mAxisLabelPaint;
    // paint for the line surrounding the chart
    protected Paint mAxisLinePaint;

    /**
     * transformer to transform values to screen pixels and return
     */
    protected Transformer mTrans;


    public AxisRenderer(ViewPort viewPortHandler, Transformer trans, AxisBase axis) {
        super(viewPortHandler);

        this.mTrans = trans;
        this.mAxis = axis;

        if (mViewPort != null) {
            //
            mAxisLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            //
            mAxisLinePaint = new Paint();
            mAxisLinePaint.setColor(Color.BLACK);
            mAxisLinePaint.setStrokeWidth(1f);
            mAxisLinePaint.setStyle(Style.STROKE);
        }
    }

    /**
     * Returns the Paint object used for drawing the axis (labels).
     *
     * @return
     */
    public Paint getPaintAxisLabels() {
        return mAxisLabelPaint;
    }


    /**
     * Computes the axis values.
     *
     * @param min - the minimum value in the data object for this axis
     * @param max - the maximum value in the data object for this axis
     */
    public void computeAxis(float min, float max) {
        computeAxisValues(min, max);
    }

    /**
     * Sets up the axis values. Computes the desired number of labels between the two given extremes.
     *
     * @return
     */
    protected void computeAxisValues(float min, float max) {
        //
        float yMin = min;
        float yMax = max;
        // 默认的labelCount
        int labelCount = mAxis.getLabelCount();
        // 最大，最小之前的间距
        double range = Math.abs(yMax - yMin);
        // 错误判断
        if (labelCount == 0 || range <= 0 || Double.isInfinite(range)) {
            mAxis.mEntries = new float[]{};
            mAxis.mCenteredEntries = new float[]{};
            mAxis.mEntryCount = 0;
            return;
        }
        // 四舍五入 计算Value间隔
        double interval = Math.round(range / labelCount);
        // 计算最终Value的间隔
        int intervalSigDigit = (int) (interval);
        if (intervalSigDigit > 5) {
            interval = Math.floor(10);
        }
        //
        double first = (interval == 0.0 ? 0.0 : Math.ceil(yMin / interval) * interval);
        double last = (interval == 0.0 ? 0.0 : Math.round(Math.floor(yMax / interval) * interval));
        //
        double f;
        int i;
        int n = 0;
        if (interval != 0.0) {
            for (f = first; f <= last; f += interval) {
                ++n;
            }
        }
        // 初始化mAxis.mEntries
        mAxis.mEntryCount = n;
        if (mAxis.mEntries.length < n) {
            // Ensure stops contains at least numStops elements.
            mAxis.mEntries = new float[n];
        }
        // 数据赋值
        for (f = first, i = 0; i < n; f += interval, ++i) {
            // Fix for negative zero case (Where value == -0.0, and 0.0 == -0.0)
            if (f == 0.0) {
                f = 0.0;
            }
            mAxis.mEntries[i] = (float) f;
        }
    }

    /**
     * Draws the axis labels to the screen.
     *
     * @param c
     */
    public abstract void renderAxisLabels(Canvas c);

    /**
     * Draws the line that goes alongside the axis.
     *
     * @param c
     */
    public abstract void renderAxisLine(Canvas c);
}
