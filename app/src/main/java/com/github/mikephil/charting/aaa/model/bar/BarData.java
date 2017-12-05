
package com.github.mikephil.charting.aaa.model.bar;

import java.util.List;

/**
 * Data object that represents all data for the BarChart.
 * <p>
 * 全部柱状图数据 由 N组柱状图数组组成
 */
public class BarData {

    /**
     * maximum y-value in the value array across all axes
     */
    protected float mYMax = -Float.MAX_VALUE;

    /**
     * the minimum y-value in the value array across all axes
     */
    protected float mYMin = Float.MAX_VALUE;

    /**
     * maximum x-value in the value array
     */
    protected float mXMax = -Float.MAX_VALUE;

    /**
     * minimum x-value in the value array
     */
    protected float mXMin = Float.MAX_VALUE;

    // 计算坐标轴的最大值，最小值
    protected float mYAxisMax = -Float.MAX_VALUE;
    protected float mYAxisMin = Float.MAX_VALUE;

    /**
     * array that holds all DataSets the ChartData object represents
     * <p>
     * BardataSet的数组
     */
    protected List<BarEntrySet> mDataSets;


    /**
     * the width of the bars on the x-axis, in values (not pixels)
     */
    private float mBarWidth = 0.85f;


    /**
     * 柱状图组数据
     *
     * @param dataSets 柱状图组数据
     */
    public BarData(List<BarEntrySet> dataSets) {
        this.mDataSets = dataSets;
        calcMinMax();
    }


    /**
     * Calc minimum and maximum values (both x and y) over all DataSets.
     * <p>
     * 计算 X Y YAxis的最大、最小值
     */
    protected void calcMinMax() {
        if (mDataSets == null) {
            return;
        }
        // init
        mXMax = -Float.MAX_VALUE;
        mXMin = Float.MAX_VALUE;
        mYMax = -Float.MAX_VALUE;
        mYMin = Float.MAX_VALUE;
        // 计算最大值与最小值
        for (BarEntrySet set : mDataSets) {
            calcMinMax(set);
        }
        // YAxisYAxisYAxisYAxisYAxisYAxisYAxis
        // init
        mYAxisMin = Float.MAX_VALUE;
        mYAxisMax = -Float.MAX_VALUE;
        // 返回第一组数据
        BarEntrySet firstLeft = getFirstLeft(mDataSets);
        if (firstLeft != null) {
            // 第一组数据的最大值 与 最小值
            mYAxisMax = firstLeft.getYMax();
            mYAxisMin = firstLeft.getYMin();
            // 循环其他组
            for (BarEntrySet dataSet : mDataSets) {
                if (dataSet.getYMin() < mYAxisMin)
                    mYAxisMin = dataSet.getYMin();
                if (dataSet.getYMax() > mYAxisMax)
                    mYAxisMax = dataSet.getYMax();
            }
        }
    }

    /**
     * Adjusts the minimum and maximum values based on the given DataSet.
     * <p>
     * 计算最大值与最小值
     *
     * @param d
     */
    protected void calcMinMax(BarEntrySet d) {
        // 从BarEntrySet中取最大值与最小值
        // XXXXXXXXXXXXXXXXXXXXXXXXXXX
        if (mXMax < d.getXMax()) {
            mXMax = d.getXMax();
        }
        if (mXMin > d.getXMin()) {
            mXMin = d.getXMin();
        }
        // YYYYYYYYYYYYYYYYYYYYYYYYYYY
        if (mYMax < d.getYMax()) {
            mYMax = d.getYMax();
        }
        if (mYMin > d.getYMin()) {
            mYMin = d.getYMin();
        }
    }


    /**
     * Sets the width each bar should have on the x-axis (in values, not pixels).
     * Default 0.85f
     *
     * @param mBarWidth 柱状图宽度
     */
    public void setBarWidth(float mBarWidth) {
        this.mBarWidth = mBarWidth;
    }

    public float getBarWidth() {
        return mBarWidth;
    }


    /**
     * returns the number of LineDataSets this object contains
     *
     * @return
     */
    public int getDataSetCount() {
        if (mDataSets == null)
            return 0;
        return mDataSets.size();
    }


    /**
     * Returns the minimum y-value for the specified axis.
     *
     * @return
     */
    public float getYMin() {
        return mYAxisMin;
    }


    /**
     * Returns the maximum y-value for the specified axis.
     *
     * @return
     */
    public float getYMax() {
        return mYAxisMax;
    }

    /**
     * Returns the minimum x-value this data object contains.
     *
     * @return
     */
    public float getXMin() {
        return mXMin;
    }

    /**
     * Returns the maximum x-value this data object contains.
     *
     * @return
     */
    public float getXMax() {
        return mXMax;
    }

    /**
     * Returns all DataSet objects this ChartData object holds.
     *
     * @return
     */
    public List<BarEntrySet> getDataSets() {
        return mDataSets;
    }


    public BarEntrySet getDataSetByIndex(int index) {

        if (mDataSets == null || index < 0 || index >= mDataSets.size())
            return null;

        return mDataSets.get(index);
    }


    /**
     * Returns the first DataSet from the datasets-array that has it's dependency on the left axis.
     * Returns null if no DataSet with left dependency could be found.
     *
     * @return
     */
    protected BarEntrySet getFirstLeft(List<BarEntrySet> sets) {
        for (BarEntrySet dataSet : sets) {
            return dataSet;
        }
        return null;
    }


    /**
     * Returns the total entry count across all DataSet objects this data object contains.
     *
     * @return
     */
    public int getEntryCount() {

        int count = 0;

        for (BarEntrySet set : mDataSets) {
            count += set.getEntryCount();
        }

        return count;
    }

}
