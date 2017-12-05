package com.github.mikephil.charting.aaa;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.aaa.model.axis.AxisBase;
import com.github.mikephil.charting.aaa.model.axis.XAxis;
import com.github.mikephil.charting.aaa.model.axis.YAxis;
import com.github.mikephil.charting.aaa.model.bar.BarData;
import com.github.mikephil.charting.aaa.model.bar.BarEntrySet;
import com.github.mikephil.charting.aaa.model.bar.BarEntry;
import com.github.mikephil.charting.aaa.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.aaa.formatter.IValueFormatter;
import com.github.mikephil.charting.aaa.utils.ViewPort;
import com.pyz.myproject.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    //
    protected BarChartView mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // 获取对象
        mChart = (BarChartView) findViewById(R.id.BarChart01);


        // value在上方
        mChart.setDrawValueAboveBar(true);
        // Y轴的最大值
        mChart.setMaxVisibleValueCount(60);

        // ######## X轴 ########
        // 得到X轴，设置X轴的样式
        XAxis xAxis = mChart.getXAxis();
        xAxis.setAxisLineColor(Color.parseColor("#d5b45c"));
        xAxis.setAxisLineWidth(3);
        //X轴的数据显示格式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value + "";
            }
        });

        // ######## 左侧Y轴 ########
        // 得到Y轴，设置Y轴的样式
        YAxis leftAxis = mChart.getYAxis();
        leftAxis.setAxisLineColor(Color.parseColor("#d5b45c"));
        leftAxis.setAxisLineWidth(3);
        // Y轴的数据显示格式
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return ((int) value) + "";
            }
        });
        leftAxis.setSpaceTop(15f);
        //设置Y轴最小的值
        leftAxis.setAxisMinimum(0f);


        int count = 6;
        float range = 50;
        float start = 0;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int x = (int) start; x < start + count; x++) {
            float y = (float) (Math.random() * (range + 1));
            Log.e("xiaxl: ", "x: " + x);
            Log.e("xiaxl: ", "y: " + y);
            //
            yVals1.add(new BarEntry(x, y));
        }

        // ######## 柱状图的样式 ########
        //设置柱状图的样式
        BarEntrySet dataSet = new BarEntrySet(yVals1, "The year 2017");
        // 柱状图颜色
        dataSet.setColor(Color.parseColor("#ffe9ae"));
        // 绘制柱状图的Value
        dataSet.setDrawValues(true);
        // 柱状图上文字颜色
        dataSet.setValueTextColor(Color.parseColor("#d5b45c"));
        // 柱状图上的字体大小 px
        dataSet.setValueTextSize(20);
        // 更改柱状图上的文字显示
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float v, BarEntry entry, int i, ViewPort viewPortHandler) {
                return ((int) entry.getY()) + "元";
            }
        });
        //
        ArrayList<BarEntrySet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);


        // ######## 柱状图的样式 ########
        //构建一个barData  将dataSets放入
        BarData barData = new BarData(dataSets);
        barData.setBarWidth(0.6f);


        //将数据插入
        mChart.setData(barData);



        mChart.mAnimator.animateXY(1200, 1200);

    }

}