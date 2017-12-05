package com.github.mikephil.charting.aaa.formatter;

import com.github.mikephil.charting.aaa.model.axis.AxisBase;

/**
 * Custom formatter interface that allows formatting of
 * axis labels before they are being drawn.
 */
public interface IAxisValueFormatter {

    /**
     * Called when a value from an axis is to be formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param value the value to be formatted
     * @param axis  the axis the value belongs to
     * @return
     */
    String getFormattedValue(float value, AxisBase axis);
}
