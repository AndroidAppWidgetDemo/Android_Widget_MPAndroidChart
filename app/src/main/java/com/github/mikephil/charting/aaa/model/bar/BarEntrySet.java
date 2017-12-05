
package com.github.mikephil.charting.aaa.model.bar;

import android.graphics.Color;

import com.github.mikephil.charting.aaa.formatter.IValueFormatter;
import com.github.mikephil.charting.aaa.utils.MPPointF;
import com.github.mikephil.charting.aaa.utils.ViewPort;

import java.util.ArrayList;
import java.util.List;

/**
 * 柱状图数据
 */
public class BarEntrySet {


    /**
     * label that describes the DataSet or the data the DataSet represents
     */
    private String mLabel = "DataSet";


    /**
     * List representing all colors that are used for this DataSet
     * <p>
     * 柱状图的颜色数组
     */
    protected List<Integer> mColors = null;

    /**
     * List representing all colors that are used for drawing the actual values for this DataSet
     * <p>
     * 柱状图上的文字颜色
     */
    protected List<Integer> mValueColors = null;


    /**
     * the entries that this DataSet represents / holds together
     * <p>
     * X Y 轴的数据 列表
     */
    protected List<BarEntry> mValues = null;

    /**
     * 取出坐标点里边最大的值
     * maximum y-value in the value array
     */
    protected float mYMax = -Float.MAX_VALUE;

    /**
     * 取出坐标点里边最小的值
     * minimum y-value in the value array
     */
    protected float mYMin = Float.MAX_VALUE;

    /**
     * 取出坐标点里边最小的值
     * maximum x-value in the value array
     */
    protected float mXMax = -Float.MAX_VALUE;

    /**
     * minimum x-value in the value array
     * 取出坐标点里边最大的值
     */
    protected float mXMin = Float.MAX_VALUE;


    /**
     * flag that indicates if the DataSet is visible or not
     */
    protected boolean mVisible = true;

    /**
     * if true, y-values are drawn on the chart
     * <p>
     * 绘制柱状图上的Value
     */
    protected boolean mDrawValues = true;

    /**
     * the size of the value-text labels
     * 柱状图上的文字大小 dp
     */
    protected float mValueTextSize = 17f;


    /**
     * custom formatter that is used instead of the auto-formatter if set
     */
    protected transient IValueFormatter mValueFormatter;

    /**
     * the offset for drawing icons (in dp)
     */
    protected MPPointF mIconsOffset = new MPPointF();


    /**
     * Creates a new DataSet object with the given values (entries) it represents. Also, a
     * label that describes the DataSet can be specified. The label can also be
     * used to retrieve the DataSet from a ChartData object.
     *
     * @param values X Y 轴的数据
     * @param label
     */
    public BarEntrySet(List<BarEntry> values, String label) {
        this();
        this.mLabel = label;
        this.mValues = values;
        // 创建数组
        if (mValues == null) {
            mValues = new ArrayList<BarEntry>();
        }
        //
        calcMinMax();
    }

    /**
     * Default constructor.
     */
    public BarEntrySet() {
        //
        mColors = new ArrayList<Integer>();
        mValueColors = new ArrayList<Integer>();

        // 柱状图的颜色数组 default color
        mColors.add(Color.rgb(140, 234, 255));
        // 柱状图上的文字颜色 默认颜色
        mValueColors.add(Color.BLACK);
    }


    /**
     * 获取数据点的个数
     *
     * @return
     */
    public int getEntryCount() {
        return mValues.size();
    }

    /**
     * Returns the array of entries that this DataSet represents.
     * <p>
     * 获取所有的数据点
     *
     * @return
     */
    public List<BarEntry> getValues() {
        return mValues;
    }

    /**
     * Sets the array of entries that this DataSet represents, and calls notifyDataSetChanged()
     * <p>
     * 重置所有的数据点
     *
     * @return
     */
    public void setValues(List<BarEntry> values) {
        mValues = values;
        calcMinMax();
    }

    /**
     * 获取Y的最小值
     *
     * @return
     */
    public float getYMin() {
        return mYMin;
    }

    /**
     * 获取Y的最大值
     *
     * @return
     */
    public float getYMax() {
        return mYMax;
    }

    /**
     * 获取X的最小值
     *
     * @return
     */
    public float getXMin() {
        return mXMin;
    }

    /**
     * 获取X的最大值
     *
     * @return
     */
    public float getXMax() {
        return mXMax;
    }


    public BarEntry getEntryForIndex(int index) {
        return mValues.get(index);
    }


    /**
     * 根据数据计算X、Y最大与最小值
     */
    public void calcMinMax() {

        if (mValues == null || mValues.isEmpty()) {
            return;
        }
        // 最大数是一个无比小的值
        mYMax = -Float.MAX_VALUE;
        mYMin = Float.MAX_VALUE;
        // 最小值是一个无比大的值
        mXMax = -Float.MAX_VALUE;
        mXMin = Float.MAX_VALUE;
        //
        for (BarEntry e : mValues) {
            calcMinMax(e);
        }
    }

    /**
     * 取出坐标点里边最小的值
     * 取除坐标点里边最大的值
     *
     * @param e
     */
    protected void calcMinMax(BarEntry e) {
        if (e != null && !Float.isNaN(e.getY())) {

            //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
            // 取出坐标点里边最小的值
            if (e.getX() < mXMin) {
                mXMin = e.getX();
            }
            // 取出坐标点里边最大的值
            if (e.getX() > mXMax) {
                mXMax = e.getX();
            }

            //YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY
            // 取出坐标点里边最小的值
            if (e.getY() < mYMin) {
                mYMin = e.getY();
            }
            // 取除坐标点里边最大的值
            if (e.getY() > mYMax) {
                mYMax = e.getY();
            }
        }
    }


    /**
     * ###### ###### COLOR GETTING RELATED METHODS ##### ######
     */

    public List<Integer> getColors() {
        return mColors;
    }


    public int getColor() {
        return mColors.get(0);
    }

    public int getColor(int index) {
        return mColors.get(index % mColors.size());
    }


    /**
     * Sets the one and ONLY color that should be used for this DataSet.
     * Internally, this recreates the colors array and adds the specified color.
     * <p>
     * 设置柱状图的颜色
     *
     * @param color
     */
    public void setColor(int color) {
        resetColors();
        mColors.add(color);
    }

    /**
     * Resets all colors of this DataSet and recreates the colors array.
     */
    public void resetColors() {
        if (mColors == null) {
            mColors = new ArrayList<Integer>();
        }
        mColors.clear();
    }

    /**
     * ###### ###### 柱状图上的文字颜色 ##### ######
     */

    /**
     * 柱状图上的文字颜色
     *
     * @param color
     */
    public void setValueTextColor(int color) {
        mValueColors.clear();
        mValueColors.add(color);
    }

    public int getValueTextColor(int index) {
        return mValueColors.get(index % mValueColors.size());
    }

    /**
     * ###### ###### 绘制柱状图的Value ##### ######
     */

    /**
     * // 绘制柱状图的Value
     *
     * @param enabled
     */
    public void setDrawValues(boolean enabled) {
        this.mDrawValues = enabled;
    }

    public boolean isDrawValuesEnabled() {
        return mDrawValues;
    }


    /**
     * ###### ###### setValueFormatter ##### ######
     */

    public void setValueFormatter(IValueFormatter f) {
        if (f != null) {
            mValueFormatter = f;
        }
    }

    public IValueFormatter getValueFormatter() {
        if (needsFormatter()) {
            mValueFormatter = new IValueFormatter() {
                @Override
                public String getFormattedValue(float v, BarEntry entry, int i, ViewPort viewPortHandler) {
                    return entry.getY() + "";
                }
            };
            return mValueFormatter;
        }
        return mValueFormatter;
    }

    public boolean needsFormatter() {
        return mValueFormatter == null;
    }

    /**
     * ###### ###### 柱状图上的文字大小 px ##### ######
     */

    /**
     * 柱状图上的文字大小 px
     *
     * @param size
     */
    public void setValueTextSize(float size) {
        mValueTextSize = size;
    }

    public float getValueTextSize() {
        return mValueTextSize;
    }


    public boolean isVisible() {
        return mVisible;
    }


}
