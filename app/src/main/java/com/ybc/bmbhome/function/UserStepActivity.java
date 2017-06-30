package com.ybc.bmbhome.function;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ybc.bmbhome.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * 我的步数
 */
public class UserStepActivity extends AppCompatActivity {

    /*========== 标志位相关 ==========*/
    private static final int DEFAULT_DATA = 0;              //默认数据标志位
    private static final int SUBCOLUMNS_DATA = 1;           //多子列数据标志位
    private static final int NEGATIVE_SUBCOLUMNS_DATA = 2;  //反向多子列标志位
    private static final int STACKED_DATA = 3;              //堆放数据标志位
    private static final int NEGATIVE_STACKED_DATA = 4;     //反向堆放数据标志位
    private static boolean IS_NEGATIVE = false;             //是否需要反向标志位
    /*========== 控件相关 ==========*/
    private ColumnChartView mColumnCharView;                //柱形图控件
    private Toolbar toolbar;
    /*========== 状态相关 ==========*/
    private boolean isHasAxes = true;                       //是否显示坐标轴
    private boolean isHasAxesNames = true;                  //是否显示坐标轴
    private boolean isHasColumnLabels = true;              //是否显示列标签
    private boolean isColumnsHasSelected = false;           //设置列点击后效果(消失/显示标签)
    /*========== 数据相关 ==========*/
    private ColumnChartData mColumnChartData;               //柱状图数据
    private int dataType = DEFAULT_DATA;                    //默认数据状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_step);
        mColumnCharView = (ColumnChartView) findViewById(R.id.step_Column_chart);
        toolbar = (Toolbar) findViewById(R.id.toolbar_user_step_line);
        toolbar.setTitle("运动记录");
        setSupportActionBar(toolbar);
        // toolbar.setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initData();
        initListener();

        SystemBarTintManager localSystemBarTintManager = new SystemBarTintManager(this);
        localSystemBarTintManager.setStatusBarTintResource(R.color.blue);
        localSystemBarTintManager.setStatusBarTintEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //点击back键finish当前activity
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public void initData() {
        setColumnDatas();        //根据状态设置相关数据
    }


    public void initListener() {
        //子列触摸监听
        mColumnCharView.setOnValueTouchListener(new ValueTouchListener());
    }

    /**
     * 根据不同的数据类型 绘制不同的柱状图
     */
    private void setColumnDatas() {
        switch (dataType) {
            case DEFAULT_DATA:
                IS_NEGATIVE = false;                                            //设置反向标志位：不反向
                setColumnDatasByParams(1, 7, false, IS_NEGATIVE);               //设置数据：单子列 总八列 不堆叠 不反向
                break;
            case SUBCOLUMNS_DATA:
                IS_NEGATIVE = false;                                            //设置反向标志位：不反向
                setColumnDatasByParams(4, 8, false, IS_NEGATIVE);               //设置数据：四子列 总八列 不堆叠 不反向
                break;
            case NEGATIVE_SUBCOLUMNS_DATA:
                IS_NEGATIVE = true;                                             //设置反向标志位：反向
                setColumnDatasByParams(4, 8, false, IS_NEGATIVE);               //设置数据：四子列 总八列 不堆叠 反向
                break;
            case STACKED_DATA:
                IS_NEGATIVE = false;                                            //设置反向标志位：不反向
                setColumnDatasByParams(4, 8, true, IS_NEGATIVE);                //设置数据：四子列 总八列 堆叠 不反向
                break;
            case NEGATIVE_STACKED_DATA:
                IS_NEGATIVE = true;                                             //设置反向标志位：反向
                setColumnDatasByParams(4, 8, true, IS_NEGATIVE);                //设置数据：四子列 总八列 堆叠 反向
                break;
            default:
                IS_NEGATIVE = false;                                            //设置反向标志位：不反向
                setColumnDatasByParams(1, 8, false, IS_NEGATIVE);               //设置数据：单列 总八列 不堆叠 不反向
                break;
        }
    }

    /**
     * 根据不同的参数 决定绘制什么样的柱状图
     *
     * @param numSubcolumns 子列数
     * @param numColumns    总列数
     * @param isStack       是否堆叠
     * @param isNegative    是否反向
     */
    private void setColumnDatasByParams(int numSubcolumns, int numColumns, boolean isStack, boolean isNegative) {
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        //双重for循环给每个子列设置随机的值和随机的颜色
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; ++j) {
                //确定是否反向
                int negativeSign = getNegativeSign(isNegative);
                //根据反向值 设置列的值
                values.add(new SubcolumnValue((float) Math.random() * 50f * negativeSign + 5 * negativeSign, ChartUtils.pickColor()));
            }

            /*===== 柱状图相关设置 =====*/
            Column column = new Column(values);
            column.setHasLabels(isHasColumnLabels);                    //没有标签
            column.setHasLabelsOnlyForSelected(isColumnsHasSelected);  //点击只放大
            columns.add(column);

        }
        mColumnChartData = new ColumnChartData(columns);               //设置数据
        mColumnChartData.setStacked(isStack);                          //设置是否堆叠
        mColumnChartData.setFillRatio(0.4F);                           //设置参数即为柱子宽度的倍数，范围只能在0到1之间
        /*===== 坐标轴相关设置 类似于Line Charts =====*/
        if (isHasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (isHasAxesNames) {
                axisX.setName("星期");
                //axisY.setName("Axis Y");
            }
            mColumnChartData.setAxisXBottom(axisX);
            mColumnChartData.setAxisYLeft(axisY);
        } else {
            mColumnChartData.setAxisXBottom(null);
            mColumnChartData.setAxisYLeft(null);
        }
        mColumnCharView.setColumnChartData(mColumnChartData);
    }

    /**
     * 根据是否反向标记返回对应的反向标志
     *
     * @param isNegative 是否反向
     * @return 反向标志 -1：反向 1：正向
     */
    private int getNegativeSign(boolean isNegative) {
        if (isNegative) {
            int[] sign = new int[]{-1, 1};                      //-1：反向 1：正向
            return sign[Math.round((float) Math.random())];     //随机确定子列正反
        }
        return 1;                                               //默认全为正向
    }

    /**
     * 柱形图改变时的动画(需要判断当前是否是反向)
     *
     * @param isNegative 反向标记位
     */
    private void changeColumnsAnimate(boolean isNegative) {
        //增强for循环改变列数据
        for (Column column : mColumnChartData.getColumns()) {
            for (SubcolumnValue value : column.getValues()) {
                //根据当前反向标志位来重新绘制
                value.setTarget((float) Math.random() * 100 * getNegativeSign(isNegative));
            }
        }
        mColumnCharView.startDataAnimation();               //开始动画
    }

    /**
     * 点击显示列标签
     */
    private void showOrHideLablesByColumnsSelected() {
        isColumnsHasSelected = !isColumnsHasSelected;                     //取反即可
        mColumnCharView.setValueSelectionEnabled(isColumnsHasSelected);   //设置选中状态
        if (isColumnsHasSelected) {
            isHasColumnLabels = false;                                    //如果点击才显示标签 则标签开始不可见
        }
        setColumnDatas();                                                 //重新设置
    }

    /**
     * 子列触摸监听
     */
    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            Toast.makeText(UserStepActivity.this, "" + (int) value.getValue(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {

        }
    }

   /* private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("运动记录");
        }
    }*/
}
