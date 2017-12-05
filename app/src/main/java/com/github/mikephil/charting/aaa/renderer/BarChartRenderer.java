
package com.github.mikephil.charting.aaa.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.github.mikephil.charting.aaa.BarChartView;
import com.github.mikephil.charting.aaa.formatter.IValueFormatter;
import com.github.mikephil.charting.aaa.model.bar.BarData;
import com.github.mikephil.charting.aaa.model.bar.BarEntry;
import com.github.mikephil.charting.aaa.model.bar.BarEntrySet;
import com.github.mikephil.charting.aaa.utils.BarRectBuffer;
import com.github.mikephil.charting.aaa.utils.Transformer;
import com.github.mikephil.charting.aaa.utils.Utils;
import com.github.mikephil.charting.aaa.utils.ViewPort;

import java.util.List;

public class BarChartRenderer extends Renderer {

    protected BarChartView mChart;

    // BarRectBuffer 数组长度
    protected BarRectBuffer[] mBarRectBuffers;


    /**
     * main paint object used for rendering
     */
    protected Paint mRenderPaint;

    /**
     * paint object for drawing values (text representing values of chart
     * entries)
     */
    protected Paint mValuePaint;


    public BarChartRenderer(BarChartView chart,
                            ViewPort viewPort) {
        super(viewPort);
        //
        this.mChart = chart;
        //
        mRenderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRenderPaint.setStyle(Paint.Style.FILL);
        //
        mValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mValuePaint.setColor(Color.rgb(63, 63, 63));
        mValuePaint.setTextAlign(Paint.Align.CENTER);
        mValuePaint.setTextSize(Utils.convertDpToPixel(9f));
    }

    /**
     * 创建RectBuffer 矩形数组
     */
    public void initRectBuffers() {
        // 获取数据
        BarData barData = mChart.getBarData();
        // BarRectBuffer 数组长度
        mBarRectBuffers = new BarRectBuffer[barData.getDataSetCount()];
        //
        for (int i = 0; i < mBarRectBuffers.length; i++) {
            BarEntrySet set = barData.getDataSetByIndex(i);
            mBarRectBuffers[i] = new BarRectBuffer(set.getEntryCount() * 4);
        }
    }

    public void drawData(Canvas c) {

        BarData barData = mChart.getBarData();

        for (int i = 0; i < barData.getDataSetCount(); i++) {

            BarEntrySet set = barData.getDataSetByIndex(i);

            if (set.isVisible()) {
                drawDataSet(c, set, i);
            }
        }
    }


    protected void drawDataSet(Canvas c, BarEntrySet dataSet, int index) {


        Transformer trans = mChart.getTransformer();

        float phaseX = mChart.mAnimator.getPhaseX();
        float phaseY = mChart.mAnimator.getPhaseY();

        // initialize the buffer
        BarRectBuffer buffer = mBarRectBuffers[index];
        buffer.setPhases(phaseX, phaseY);
        buffer.setDataSet(index);
        buffer.setBarWidth(mChart.getBarData().getBarWidth());

        buffer.feed(dataSet);

        trans.pointValuesToPixel(buffer.buffer);

        final boolean isSingleColor = dataSet.getColors().size() == 1;

        if (isSingleColor) {
            mRenderPaint.setColor(dataSet.getColor());
        }

        for (int j = 0; j < buffer.size(); j += 4) {

            if (!mViewPort.isInBoundsLeft(buffer.buffer[j + 2]))
                continue;

            if (!mViewPort.isInBoundsRight(buffer.buffer[j]))
                break;

            if (!isSingleColor) {
                // Set the color for the currently drawn value. If the index
                // is out of bounds, reuse colors.
                mRenderPaint.setColor(dataSet.getColor(j / 4));
            }

            c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                    buffer.buffer[j + 3], mRenderPaint);

        }
    }

    public void drawValues(Canvas c) {

        // if values are drawn
        if (isDrawingValuesAllowed(mChart)) {

            List<BarEntrySet> dataSets = mChart.getBarData().getDataSets();

            final float valueOffsetPlus = Utils.convertDpToPixel(4.5f);
            float posOffset = 0f;
            float negOffset = 0f;
            boolean drawValueAboveBar = mChart.isDrawValueAboveBarEnabled();

            for (int i = 0; i < mChart.getBarData().getDataSetCount(); i++) {

                BarEntrySet dataSet = dataSets.get(i);

                if (!shouldDrawValues(dataSet))
                    continue;

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);

                // calculate the correct offset depending on the draw position of
                // the value
                float valueTextHeight = Utils.calcTextHeight(mValuePaint, "8");
                posOffset = (drawValueAboveBar ? -valueOffsetPlus : valueTextHeight + valueOffsetPlus);
                negOffset = (drawValueAboveBar ? valueTextHeight + valueOffsetPlus : -valueOffsetPlus);

                // get the buffer
                BarRectBuffer buffer = mBarRectBuffers[i];


                //
                for (int j = 0; j < buffer.buffer.length; j += 4) {

                    float x = (buffer.buffer[j] + buffer.buffer[j + 2]) / 2f;

                    if (!mViewPort.isInBoundsRight(x))
                        break;

                    if (!mViewPort.isInBoundsY(buffer.buffer[j + 1])
                            || !mViewPort.isInBoundsLeft(x))
                        continue;

                    BarEntry entry = dataSet.getEntryForIndex(j / 4);
                    float val = entry.getY();

                    if (dataSet.isDrawValuesEnabled()) {
                        drawValue(c, dataSet.getValueFormatter(), val, entry, i, x,
                                val >= 0 ?
                                        (buffer.buffer[j + 1] + posOffset) :
                                        (buffer.buffer[j + 3] + negOffset),
                                dataSet.getValueTextColor(j / 4));
                    }
                }
            }
        }
    }

    /**
     * Returns true if the DataSet values should be drawn, false if not.
     *
     * @param set
     * @return
     */
    protected boolean shouldDrawValues(BarEntrySet set) {
        return set.isVisible() && set.isDrawValuesEnabled();
    }


    protected boolean isDrawingValuesAllowed(BarChartView chart) {
        return chart.getData().getEntryCount() < chart.getMaxVisibleCount();
    }


    /**
     * Applies the required styling (provided by the DataSet) to the value-paint
     * object.
     *
     * @param set
     */
    protected void applyValueTextStyle(BarEntrySet set) {
        mValuePaint.setTextSize(set.getValueTextSize());
    }


    /**
     * Draws the value of the given entry by using the provided IValueFormatter.
     *
     * @param c            canvas
     * @param formatter    formatter for custom value-formatting
     * @param value        the value to be drawn
     * @param entry        the entry the value belongs to
     * @param dataSetIndex the index of the DataSet the drawn Entry belongs to
     * @param x            position
     * @param y            position
     * @param color
     */
    public void drawValue(Canvas c, IValueFormatter formatter, float value, BarEntry entry, int dataSetIndex, float x, float y, int color) {
        mValuePaint.setColor(color);
        c.drawText(formatter.getFormattedValue(value, entry, dataSetIndex, mViewPort), x, y, mValuePaint);
    }

}
