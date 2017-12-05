
package com.github.mikephil.charting.aaa;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.github.mikephil.charting.aaa.anima.ChartAnimator;
import com.github.mikephil.charting.aaa.model.bar.BarData;
import com.github.mikephil.charting.aaa.renderer.BarChartRenderer;
import com.github.mikephil.charting.aaa.utils.MPPointF;
import com.github.mikephil.charting.aaa.utils.Utils;
import com.github.mikephil.charting.aaa.utils.ViewPort;

/**
 * Baseclass of all Chart-Views.
 */
@SuppressLint("NewApi")
public abstract class ChartView
        extends ViewGroup {


    /**
     * object that holds all data that was originally set for the chart, before
     * it was modified or any filtering algorithms had been applied
     * <p>
     * 柱状图 数据 BarData
     */
    protected BarData mData = null;

    /**
     * paint object for drawing the information text when there are no values in
     * the chart
     * <p>
     * 空数据时，提示信息的
     */
    protected Paint mInfoPaint;


    /**
     * text that is displayed when the chart is empty
     * 没有数据时的显示文字
     */
    private String mNoDataText = "No chart data available.";


    /**
     * object responsible for rendering the data
     * <p>
     * 绘制什么用的Render呢?????????
     */
    protected BarChartRenderer mRenderer;

    //protected IHighlighter mHighlighter;

    /**
     * object that manages the bounds and drawing constraints of the chart
     * ??????????????
     */
    protected ViewPort mViewPortHandler = new ViewPort();


    /**
     * flag that indicates if offsets calculation has already been done or not
     * 是否重新计算offset
     */
    private boolean mOffsetsCalculated = false;


    /**
     * The maximum distance in dp away from an entry causing it to highlight.
     * // 500dp
     */
    protected float mMaxHighlightDistance = 0f;

    /**
     * object responsible for animations
     */
    public ChartAnimator mAnimator;


    /**
     * default constructor for initialization in code
     */
    public ChartView(Context context) {
        super(context);
        init();
    }

    /**
     * constructor for initialization in xml
     */
    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * even more awesome constructor
     */
    public ChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 50dp
        int size = (int) Utils.convertDpToPixel(50f);
        //
        setMeasuredDimension(
                Math.max(getSuggestedMinimumWidth(),
                        resolveSize(size, widthMeasureSpec)),
                Math.max(getSuggestedMinimumHeight(),
                        resolveSize(size, heightMeasureSpec)));
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).layout(left, top, right, bottom);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //
        if (w > 0 && h > 0 && w < 10000 && h < 10000) {
            mViewPortHandler.setChartDimens(w, h);
        }
        //
        notifyDataSetChanged();
        //
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        // 空数据时的显示内容
        if (mData == null) {
            boolean hasText = !TextUtils.isEmpty(mNoDataText);
            if (hasText) {
                MPPointF c = getCenter();
                canvas.drawText(mNoDataText, c.x, c.y, mInfoPaint);
            }
            return;
        }
        // 是否重新计算offset
        if (!mOffsetsCalculated) {
            calculateOffsets();
            mOffsetsCalculated = true;
        }
    }


    /**
     * initialize all paints and stuff
     * <p>
     * 初始化画笔
     */
    protected void init() {
        //
        setWillNotDraw(false);
        // 初始化速度、单位
        // initialize the utils
        Utils.init(getContext());
        // 500dp
        mMaxHighlightDistance = Utils.convertDpToPixel(500f);


        // 空数据时，提示信息的
        mInfoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInfoPaint.setColor(Color.rgb(247, 189, 51)); // orange
        mInfoPaint.setTextAlign(Align.CENTER);
        mInfoPaint.setTextSize(12f);


        if (android.os.Build.VERSION.SDK_INT < 11) {
            mAnimator = new ChartAnimator();
        } else {
            mAnimator = new ChartAnimator(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // ViewCompat.postInvalidateOnAnimation(Chart.this);
                    postInvalidate();
                }
            });
        }

    }


    /**
     * Sets a new data object for the chart. The data object contains all values
     * and information needed for displaying.
     * <p>
     * 设置数据BarData
     *
     * @param data
     */
    public void setData(BarData data) {
        // 数据
        mData = data;
        // 是否重新计算offset
        mOffsetsCalculated = false;

        // 数据是否正常的判断
        if (data == null) {
            return;
        }

        // let the chart know there is new data
        notifyDataSetChanged();
    }


    /**
     * Lets the chart know its underlying data has changed and performs all
     * necessary recalculations. It is crucial that this method is called
     * everytime data is changed dynamically. Not calling this method can lead
     * to crashes or unexpected behaviour.
     */
    public abstract void notifyDataSetChanged();

    /**
     * Calculates the offsets of the chart to the border depending on the
     * position of an eventual legend or depending on the length of the y-axis
     * and x-axis labels and their position
     */
    protected abstract void calculateOffsets();

    /**
     * Calculates the y-min and y-max value and the y-delta and x-delta value
     */
    protected abstract void calcMinMax();


    /**
     * Returns a recyclable MPPointF instance.
     * Returns the center point of the chart (the whole View) in pixels.
     *
     * @return
     */
    public MPPointF getCenter() {
        return MPPointF.getInstance(getWidth() / 2f, getHeight() / 2f);
    }


    /**
     * Returns the ChartData object that has been set for the chart.
     *
     * @return
     */
    public BarData getData() {
        return mData;
    }


}
