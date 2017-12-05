package com.github.mikephil.charting.aaa;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.github.mikephil.charting.aaa.model.axis.XAxis;
import com.github.mikephil.charting.aaa.model.axis.YAxis;
import com.github.mikephil.charting.aaa.model.bar.BarData;
import com.github.mikephil.charting.aaa.renderer.BarChartRenderer;
import com.github.mikephil.charting.aaa.renderer.XAxisRenderer;
import com.github.mikephil.charting.aaa.renderer.YAxisRenderer;
import com.github.mikephil.charting.aaa.utils.Transformer;
import com.github.mikephil.charting.aaa.utils.Utils;

/**
 * Chart that draws bars.
 *
 */
public class BarChartView extends ChartView {


    /**
     * 组建
     */
    // the object representing the labels on the x-axis \
    // X轴
    protected XAxis mXAxis;
    // the object representing the labels on the left y-axis
    // Y轴
    protected YAxis mYAxis;

    /**
     * Runder
     */
    protected XAxisRenderer mXAxisRenderer;
    protected YAxisRenderer mYAxisRenderer;
    /**
     *
     */
    protected Transformer mAxisTransformer;

    /**
     * 数据
     */
    // the maximum number of entries to which values will be drawn (entry numbers greater than this value will cause value-labels to disappear)
    protected int mMaxVisibleCount = 100;
    // Sets the minimum offset (padding) around the chart, defaults to 15
    protected float mMinOffset = 15.f;

    // if set to true, all values are drawn above their bars, instead of below their top
    private boolean mDrawValueAboveBar = true;


    public BarChartView(Context context) {
        super(context);
    }

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void init() {
        super.init();

        //
        mAxisTransformer = new Transformer(mViewPortHandler);

        // X轴
        mXAxis = new XAxis();
        // Y轴
        mYAxis = new YAxis();
        //
        mXAxisRenderer = new XAxisRenderer(mViewPortHandler, mXAxis, mAxisTransformer);
        mYAxisRenderer = new YAxisRenderer(mViewPortHandler, mYAxis, mAxisTransformer);
        //
        mRenderer = new BarChartRenderer(this, mViewPortHandler);
        //
        mXAxis.setSpaceMin(0.5f);
        mXAxis.setSpaceMax(0.5f);
    }

    /**
     * 通知UI数据变化
     */
    @Override
    public void notifyDataSetChanged() {
        // 数据安全性判断
        if (mData == null) {
            return;
        }
        // 创建RectBuffer 矩形数组
        if (mRenderer != null) {
            mRenderer.initRectBuffers();
        }
        //
        calcMinMax();

        mYAxisRenderer.computeAxis(mYAxis.mAxisMinimum, mYAxis.mAxisMaximum);
        mXAxisRenderer.computeAxis(mXAxis.mAxisMinimum, mXAxis.mAxisMaximum);


        calculateOffsets();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //
        if (mData == null) {
            return;
        }
        //
        if (mXAxis.isEnabled()) {
            mXAxisRenderer.computeAxis(mXAxis.mAxisMinimum, mXAxis.mAxisMaximum);
        }
        //
        if (mYAxis.isEnabled()) {
            mYAxisRenderer.computeAxis(mYAxis.mAxisMinimum, mYAxis.mAxisMaximum);
        }
        // 绘制X轴
        mXAxisRenderer.renderAxisLine(canvas);
        // 绘制Y轴
        mYAxisRenderer.renderAxisLine(canvas);

        //-----绘制矩形-----
        // make sure the data cannot be drawn outside the content-rect
        int clipRestoreCount = canvas.save();
        canvas.clipRect(mViewPortHandler.getContentRect());
        //
        mRenderer.drawData(canvas);
        //
        // Removes clipping rectangle
        canvas.restoreToCount(clipRestoreCount);

        // 这个应该是绘制X Y 轴上的显示数字
        mXAxisRenderer.renderAxisLabels(canvas);
        mYAxisRenderer.renderAxisLabels(canvas);

        // 绘制柱状图上的显示文字
        mRenderer.drawValues(canvas);
    }


    @Override
    public void calculateOffsets() {

        float offsetLeft = 0f, offsetRight = 0f, offsetTop = 0f, offsetBottom = 0f;

        // offsets for y-labels
        if (mYAxis.needsOffset()) {
            offsetLeft += mYAxis.getRequiredWidthSpace(mYAxisRenderer
                    .getPaintAxisLabels());
        }


        if (mXAxis.isEnabled() && mXAxis.isDrawLabelsEnabled()) {

            float xlabelheight = mXAxis.mLabelHeight + mXAxis.getYOffset();


            offsetBottom += xlabelheight;

        }

        float minOffset = Utils.convertDpToPixel(mMinOffset);

        mViewPortHandler.restrainViewPort(
                Math.max(minOffset, offsetLeft),
                Math.max(minOffset, offsetTop),
                Math.max(minOffset, offsetRight),
                Math.max(minOffset, offsetBottom));

        prepareOffsetMatrix();
        prepareValuePxMatrix();
    }


    protected void prepareValuePxMatrix() {
        mAxisTransformer.prepareMatrixValuePx(mXAxis.mAxisMinimum,
                mXAxis.mAxisRange,
                mYAxis.mAxisRange,
                mYAxis.mAxisMinimum);
    }

    protected void prepareOffsetMatrix() {
        mAxisTransformer.prepareMatrixOffset();
    }


    protected void calcMinMax() {
        mXAxis.calculate(mData.getXMin(), mData.getXMax());
        mYAxis.calculate(mData.getYMin(), mData.getYMax());
    }


    /**
     * Returns the Transformer class that contains all matrices and is
     * responsible for transforming values into pixels on the screen and
     * backwards.
     *
     * @return
     */
    public Transformer getTransformer() {
        return mAxisTransformer;
    }


    /**
     * sets the number of maximum visible drawn values on the chart only active
     * when setDrawValues() is enabled
     *
     * @param count
     */
    public void setMaxVisibleValueCount(int count) {
        this.mMaxVisibleCount = count;
    }

    public int getMaxVisibleCount() {
        return mMaxVisibleCount;
    }


    /**
     * If set to true, all values are drawn above their bars, instead of below their top.
     *
     * @param enabled
     */
    public void setDrawValueAboveBar(boolean enabled) {
        mDrawValueAboveBar = enabled;
    }

    /**
     * returns true if drawing values above bars is enabled, false if not
     *
     * @return
     */
    public boolean isDrawValueAboveBarEnabled() {
        return mDrawValueAboveBar;
    }


    public BarData getBarData() {
        return mData;
    }


    /**
     * Returns the object representing all x-labels, this method can be used to
     * acquire the XAxis object and modify it (e.g. change the position of the
     * labels, styling, etc.)
     *
     * @return
     */
    public XAxis getXAxis() {
        return mXAxis;
    }

    /**
     * Returns the left y-axis object. In the horizontal bar-chart, this is the
     * top axis.
     *
     * @return
     */
    public YAxis getYAxis() {
        return mYAxis;
    }


}
