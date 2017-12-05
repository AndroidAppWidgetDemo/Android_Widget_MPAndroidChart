
package com.github.mikephil.charting.aaa.model.axis;

import android.graphics.Color;
import android.graphics.DashPathEffect;

import com.github.mikephil.charting.aaa.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.aaa.utils.Utils;

/**
 * Base-class of all axes (previously called labels).
 *
 */
public abstract class AxisBase {


    //###############坐标轴属性################
    private int mAxisLineColor = Color.GRAY;
    //
    private float mAxisLineWidth = 1f;
    //###############文字属性################
    // the text size of the labels 默认文字大小
    protected float mTextSize = 0;
    // the text color to use for the labels
    protected int mTextColor = Color.BLACK;

    // custom formatter that is used instead of the auto-formatter if set
    protected IAxisValueFormatter mAxisValueFormatter;

    //############################数据#################################

    // the offset in pixels this component has on the x-axis
    protected float mXOffset = 5f;
    // the offset in pixels this component has on the Y-axis
    protected float mYOffset = 5f;

    // Extra spacing for `axisMinimum` to be added to automatically calculated `axisMinimum`
    protected float mSpaceMin = 0.f;
    // Extra spacing for `axisMaximum` to be added to automatically calculated `axisMaximum`
    protected float mSpaceMax = 0.f;

    // don't touch this direclty, use setter
    public float mAxisMaximum = 0f;
    // don't touch this directly, use setter
    public float mAxisMinimum = 0f;
    // the total range of values this axis covers
    public float mAxisRange = 0f;

    // 坐标轴上显示的Values
    // the actual array of entries
    public float[] mEntries = new float[]{};
    // axis label entries only used for centered labels
    public float[] mCenteredEntries = new float[]{};
    // the number of entries the legend contains
    // 标签的数量
    public int mEntryCount;
    // the number of label entries the axis should have, default 6
    private int mLabelCount = 6;


    // flag that indicates if this axis / legend is enabled or not
    protected boolean mEnabled = true;
    // flag that indicates of the labels of this axis should be drawn or not
    protected boolean mDrawLabels = true;
    //
    protected boolean mCenterAxisLabels = false;

    //flag indicating that the axis-min value has been customized
    protected boolean mCustomAxisMin = false;
    // flag indicating that the axis-max value has been customized
    protected boolean mCustomAxisMax = false;

    /**
     * the path effect of the axis line that makes dashed lines possible
     */
    private DashPathEffect mAxisLineDashPathEffect = null;

    /**
     * default constructor
     */
    public AxisBase() {
        this.mTextSize = Utils.convertDpToPixel(10f);
        this.mXOffset = Utils.convertDpToPixel(5f);
        this.mYOffset = Utils.convertDpToPixel(5f);
    }


    /**
     * Calculates the minimum / maximum  and range values of the axis with the given
     * minimum and maximum values from the chart data.
     * <p>
     * 计算 坐标轴的最大，最小值
     *
     * @param dataMin the min value according to chart data
     * @param dataMax the max value according to chart data
     */
    public void calculate(float dataMin, float dataMax) {

        // if custom, use value as is, else use data value
        float min = mCustomAxisMin ? mAxisMinimum : (dataMin - mSpaceMin);
        float max = mCustomAxisMax ? mAxisMaximum : (dataMax + mSpaceMax);

        // temporary range (before calculations)
        float range = Math.abs(max - min);

        // in case all values are equal
        if (range == 0f) {
            max = max + 1f;
            min = min - 1f;
        }
        // 计算坐标轴的最大，最小值
        this.mAxisMinimum = min;
        this.mAxisMaximum = max;

        // actual range
        this.mAxisRange = Math.abs(max - min);
    }


    /**
     * Returns the used offset on the x-axis for drawing the axis or legend
     * labels. This offset is applied before and after the label.
     *
     * @return
     */
    public float getXOffset() {
        return mXOffset;
    }


    /**
     * Returns the used offset on the x-axis for drawing the axis labels. This
     * offset is applied before and after the label.
     *
     * @return
     */
    public float getYOffset() {
        return mYOffset;
    }


    /**
     * returns the text size that is currently set for the labels, in pixels
     *
     * @return
     */
    public float getTextSize() {
        return mTextSize;
    }


    /**
     * Returns the text color that is set for the labels.
     *
     * @return
     */
    public int getTextColor() {
        return mTextColor;
    }

    /**
     * Returns true if this comonent is enabled (should be drawn), false if not.
     *
     * @return
     */
    public boolean isEnabled() {
        return mEnabled;
    }


    public boolean isCenterAxisLabelsEnabled() {
        return mCenterAxisLabels && mEntryCount > 0;
    }


    /**
     * Sets the width of the border surrounding the chart in dp.
     *
     * @param width
     */
    public void setAxisLineWidth(float width) {
        mAxisLineWidth = width;
    }

    /**
     * Returns the width of the axis line (line alongside the axis).
     *
     * @return
     */
    public float getAxisLineWidth() {
        return mAxisLineWidth;
    }


    /**
     * Sets the color of the border surrounding the chart.
     *
     * @param color
     */
    public void setAxisLineColor(int color) {
        mAxisLineColor = color;
    }

    /**
     * Returns the color of the axis line (line alongside the axis).
     *
     * @return
     */
    public int getAxisLineColor() {
        return mAxisLineColor;
    }


    /**
     * Returns true if drawing the labels is enabled for this axis.
     *
     * @return
     */
    public boolean isDrawLabelsEnabled() {
        return mDrawLabels;
    }


    /**
     * Returns the number of label entries the y-axis should have
     *
     * @return
     */
    public int getLabelCount() {
        return mLabelCount;
    }

    /**
     * Returns the longest formatted label (in terms of characters), this axis
     * contains.
     * 获取坐标轴上最长文字
     *
     * @return
     */
    public String getLongestLabel() {

        String longest = "";
        //
        for (int i = 0; i < mEntries.length; i++) {
            String text = getFormattedLabel(i);

            if (text != null && longest.length() < text.length())
                longest = text;
        }

        return longest;
    }

    /**
     * 获取坐标轴显示文字
     *
     * @param index
     * @return
     */
    public String getFormattedLabel(int index) {
        if (index < 0 || index >= mEntries.length) {
            return "";
        } else {
            return getValueFormatter().getFormattedValue(mEntries[index], this);
        }
    }

    /**
     * Sets the formatter to be used for formatting the axis labels. If no formatter is set, the
     * chart will
     * automatically determine a reasonable formatting (concerning decimals) for all the values
     * that are drawn inside
     * the chart. Use chart.getDefaultValueFormatter() to use the formatter calculated by the chart.
     *
     * @param f
     */
    public void setValueFormatter(IAxisValueFormatter f) {

        if (f == null) {
            mAxisValueFormatter = new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return value + "";
                }
            };
        } else {
            mAxisValueFormatter = f;
        }

    }

    /**
     * Returns the formatter used for formatting the axis labels.
     *
     * @return
     */
    public IAxisValueFormatter getValueFormatter() {

        if (mAxisValueFormatter == null) {
            mAxisValueFormatter = new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return value + "";
                }
            };
        }
        return mAxisValueFormatter;
    }


    /**
     * returns the DashPathEffect that is set for axis line
     *
     * @return
     */
    public DashPathEffect getAxisLineDashPathEffect() {
        return mAxisLineDashPathEffect;
    }


    /**
     * Set a custom minimum value for this axis. If set, this value will not be calculated
     * automatically depending on
     * the provided data. Use resetAxisMinValue() to undo this. Do not forget to call
     * setStartAtZero(false) if you use
     * this method. Otherwise, the axis-minimum value will still be forced to 0.
     *
     * @param min
     */
    public void setAxisMinimum(float min) {
        mCustomAxisMin = true;
        mAxisMinimum = min;
        this.mAxisRange = Math.abs(mAxisMaximum - min);
    }


    /**
     * Sets extra spacing for `axisMinimum` to be added to automatically calculated `axisMinimum`
     */
    public void setSpaceMin(float mSpaceMin) {
        this.mSpaceMin = mSpaceMin;
    }


    /**
     * Sets extra spacing for `axisMaximum` to be added to automatically calculated `axisMaximum`
     */
    public void setSpaceMax(float mSpaceMax) {
        this.mSpaceMax = mSpaceMax;
    }
}
