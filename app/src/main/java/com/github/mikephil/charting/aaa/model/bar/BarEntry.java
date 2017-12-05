package com.github.mikephil.charting.aaa.model.bar;

/**
 * Entry class for the BarChart. (especially stacked bars)
 * <p>
 * 柱状图 坐标数据
 *
 * @author Philipp Jahoda
 */
public class BarEntry {

    /**
     * the x value
     */
    private float x = 0f;
    /**
     * the y value
     */
    private float y = 0f;


    /**
     * A Entry represents one single entry in the chart.
     *
     * @param x the x value
     * @param y the y value (the actual value of the entry)
     */
    public BarEntry(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-value of this Entry object.
     *
     * @return
     */
    public float getX() {
        return x;
    }

    /**
     * Sets the x-value of this Entry object.
     *
     * @param x
     */
    public void setX(float x) {
        this.x = x;
    }


    /**
     * Returns the y value of this Entry.
     *
     * @return
     */
    public float getY() {
        return y;
    }


    /**
     * Sets the y-value for the Entry.
     *
     * @param y
     */
    public void setY(float y) {
        this.y = y;
    }


    /**
     * returns a string representation of the entry containing x-index and value
     */
    @Override
    public String toString() {
        return "Entry, x: " + x + " y: " + getY();
    }
}



