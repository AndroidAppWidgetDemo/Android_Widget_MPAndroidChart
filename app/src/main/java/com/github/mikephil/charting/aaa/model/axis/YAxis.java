package com.github.mikephil.charting.aaa.model.axis;

import android.graphics.Paint;

import com.github.mikephil.charting.aaa.utils.Utils;

/**
 * Class representing the y-axis labels settings and its entries. Only use the setter methods to
 * modify it. Do not
 * access public variables directly. Be aware that not all features the YLabels class provides
 * are suitable for the
 * RadarChart. Customizations that affect the value range of the axis need to be applied before
 * setting data for the
 * chart.
 *
 */
public class YAxis extends AxisBase {

    // indicates if the bottom y-label entry is drawn or not
    private boolean mDrawBottomYLabelEntry = true;

    // indicates if the top y-label entry is drawn or not
    private boolean mDrawTopYLabelEntry = true;


    // axis space from the largest value to the top in percent of the total axis range
    protected float mSpacePercentTop = 10f;
    // axis space from the smallest value to the bottom in percent of the total axis range
    protected float mSpacePercentBottom = 10f;


    // the minimum width that the axis should take (in dp). default: 0.0
    protected float mMinWidth = 0.f;
    // the maximum width that the axis can take (in dp). use Inifinity for disabling the maximum default: Float.POSITIVE_INFINITY (no maximum specified)
    protected float mMaxWidth = Float.POSITIVE_INFINITY;


    public YAxis() {
        super();
        this.mYOffset = 0f;
    }

    @Override
    public void calculate(float dataMin, float dataMax) {

        // 有自定义的最小值，取自定义的最小值
        float min = mCustomAxisMin ? mAxisMinimum : dataMin;
        // 有自定义的最大值，取自定义的最大值
        float max = mCustomAxisMax ? mAxisMaximum : dataMax;

        // 计算 range
        // temporary range (before calculations)
        float range = Math.abs(max - min);

        // in case all values are equal
        if (range == 0f) {
            max = max + 1f;
            min = min - 1f;
        }
        // ???????????????????????????
        // bottom-space only effects non-custom min
        if (!mCustomAxisMin) {
            float bottomSpace = range / 100f * getSpaceBottom();
            this.mAxisMinimum = (min - bottomSpace);
        }
        // ??????????????????????????????
        // top-space only effects non-custom max
        if (!mCustomAxisMax) {
            float topSpace = range / 100f * getSpaceTop();
            this.mAxisMaximum = (max + topSpace);
        }

        // calc actual range
        this.mAxisRange = Math.abs(this.mAxisMaximum - this.mAxisMinimum);
    }

    /**
     * This is for normal (not horizontal) charts horizontal spacing.
     *
     * @param p
     * @return
     */
    public float getRequiredWidthSpace(Paint p) {
        // 文字大小
        p.setTextSize(mTextSize);
        //
        String label = getLongestLabel();
        float width = (float) Utils.calcTextWidth(p, label) + getXOffset() * 2f;

        float minWidth = getMinWidth();
        float maxWidth = getMaxWidth();

        if (minWidth > 0.f)
            minWidth = Utils.convertDpToPixel(minWidth);

        if (maxWidth > 0.f && maxWidth != Float.POSITIVE_INFINITY)
            maxWidth = Utils.convertDpToPixel(maxWidth);

        width = Math.max(minWidth, Math.min(width, maxWidth > 0.0 ? maxWidth : width));

        return width;
    }


    /**
     * @return the minimum width that the axis should take (in dp).
     */
    public float getMinWidth() {
        return mMinWidth;
    }


    /**
     * @return the maximum width that the axis can take (in dp).
     */
    public float getMaxWidth() {
        return mMaxWidth;
    }

    /**
     * returns true if drawing the top y-axis label entry is enabled
     *
     * @return
     */
    public boolean isDrawTopYLabelEntryEnabled() {
        return mDrawTopYLabelEntry;
    }

    /**
     * returns true if drawing the bottom y-axis label entry is enabled
     *
     * @return
     */
    public boolean isDrawBottomYLabelEntryEnabled() {
        return mDrawBottomYLabelEntry;
    }


    /**
     * Sets the top axis space in percent of the full range. Default 10f
     *
     * @param percent
     */
    public void setSpaceTop(float percent) {
        mSpacePercentTop = percent;
    }

    /**
     * Returns the top axis space in percent of the full range. Default 10f
     *
     * @return
     */
    public float getSpaceTop() {
        return mSpacePercentTop;
    }


    /**
     * Returns the bottom axis space in percent of the full range. Default 10f
     *
     * @return
     */
    public float getSpaceBottom() {
        return mSpacePercentBottom;
    }

    /**
     * Returns true if this axis needs horizontal offset, false if no offset is needed.
     *
     * @return
     */
    public boolean needsOffset() {
        if (isEnabled() && isDrawLabelsEnabled())
            return true;
        else
            return false;
    }


}
