package com.ybc.bmbhome.function;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ybc.bmbhome.R;
import com.ybc.bmbhome.database.DbHelper;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * 我的心情*/
public class UserMoodActivity extends AppCompatActivity {
    /*========= 控件相关 =========*/
    private PieChartView mPieChartView;                 //饼状图控件
    private Toolbar toolbar;
    private TextView textView;
    /*========= 状态相关 =========*/
    private boolean isExploded = false;                 //每块之间是否分离
    private boolean isHasLabelsInside = true;          //标签在内部
    private boolean isHasLabelsOutside = false;         //标签在外部
    private boolean isHasCenterCircle = false;          //空心圆环
    private boolean isPiesHasSelected = true;          //块选中标签样式
    private boolean isHasCenterSingleText = true;      //圆环中心单行文字
    private boolean isHasCenterDoubleText = false;      //圆环中心双行文字

    /*========= 数据相关 =========*/
    private PieChartData mPieChartData;                 //饼状图数据
    private DbHelper dbHelper;
    List<String> listmood = new ArrayList<>();
    List<SliceValue> values = new ArrayList<>();
    private int MOOD_HAPPY = 0;
    private int MOOD_WUNAI = 0;
    private int MOOD_BEISHANG = 0;
    private int MOOD_YOUYU = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_mood);
        dbHelper = new DbHelper(this);
        selectDb();
        mPieChartView = (PieChartView) findViewById(R.id.mood_pie_chart);
        toolbar = (Toolbar) findViewById(R.id.toolbar_user_mood_line);
        toolbar.setTitle("心情记录");
        setSupportActionBar(toolbar);
        textView = (TextView) findViewById(R.id.textView16);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setPieDatas();
        mPieChartView.setOnValueTouchListener(new ValueTouchListener());
        showOrHideLablesByPiesSelected();
        showOrHideLabelsInside();
        changePiesAnimate();
        SystemBarTintManager localSystemBarTintManager = new SystemBarTintManager(this);
        localSystemBarTintManager.setStatusBarTintResource(R.color.colorAccent);
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

    /**
     * 设置相关数据
     */
    private void setPieDatas() {
        int numValues = 4;                //把一张饼切成4块

        /*===== 随机设置每块的颜色和数据 =====*/


        /*===== 设置相关属性 类似Line Chart =====*/
        mPieChartData = new PieChartData(values);
        mPieChartData.setHasLabels(isHasLabelsInside);
        mPieChartData.setHasLabelsOnlyForSelected(isPiesHasSelected);
        mPieChartData.setHasLabelsOutside(isHasLabelsOutside);
        mPieChartData.setHasCenterCircle(isHasCenterCircle);

        //是否分离
        if (isExploded) {
            mPieChartData.setSlicesSpacing(18);                 //分离间距为18
        }

        //是否显示单行文本
        if (isHasCenterSingleText) {
            mPieChartData.setCenterText1("Hello");             //文本内容
        }

        //是否显示双行文本
        if (isHasCenterDoubleText) {
            mPieChartData.setCenterText2("World");             //文本内容

            /*===== 设置内置字体 不建议设置 除非有特殊需求 =====*/
            /*Typeface tf = Typeface.createFromAsset(this.getAssets(), "Roboto-Italic.ttf");
            mPieChartData.setCenterText2Typeface(tf);
            mPieChartData.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_double_text_size)));*/
        }
        mPieChartView.setPieChartData(mPieChartData);         //设置控件
    }

    /**
     * 在内部显示标签
     */
    private void showOrHideLabelsInside() {
        isHasLabelsInside = !isHasLabelsInside;         //取反即可
        if (isHasLabelsInside) {
            isPiesHasSelected = false;                  //点击不显示标签
            //设置点击不显示标签
            mPieChartView.setValueSelectionEnabled(isPiesHasSelected);
            //已经在外部的话 适当变化形状
            if (isHasLabelsOutside) {
                mPieChartView.setCircleFillRatio(0.7f);
            } else {
                mPieChartView.setCircleFillRatio(1.0f);
            }
        }
        setPieDatas();                                  //重新设置
    }

    /**
     * 改变数据时的动画
     */
    private void changePiesAnimate() {

        mPieChartView.startDataAnimation();         //设置动画
    }

    /**
     * 点击每部分是否显示标签信息
     */
    private void showOrHideLablesByPiesSelected() {
        //isPiesHasSelected = !isPiesHasSelected;             //取反即可
        //点击是否显示标签
        mPieChartView.setValueSelectionEnabled(isPiesHasSelected);
        if (isPiesHasSelected) {
            isHasLabelsInside = false;                      //内外都不显示标签
            isHasLabelsOutside = false;
            //如果已经在外部 适当变形
            if (isHasLabelsOutside) {
                mPieChartView.setCircleFillRatio(0.7f);
            } else {
                mPieChartView.setCircleFillRatio(1.0f);
            }
        }
        setPieDatas();                                      //重新设置
    }

    /**
     * 每部分点击监听
     */
    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            //Toast.makeText(UserMoodActivity.this, "当前选中块约占: " + (int) value.getValue() + " %", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
        }
    }

    private void selectDb() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from user_mood", null);
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String mood = cursor.getString(cursor.getColumnIndex("mood"));
                listmood.add(mood);
            } while (cursor.moveToNext());
        }

        for (String s : listmood) {
            if (s.equals("快乐")) MOOD_HAPPY++;
            if (s.equals("无奈")) MOOD_WUNAI++;
            if (s.equals("悲伤")) MOOD_BEISHANG++;
            if (s.equals("忧郁")) MOOD_YOUYU++;
        }
        SliceValue sliceValue = new SliceValue(MOOD_HAPPY, ChartUtils.COLOR_RED);
        values.add(sliceValue);
        SliceValue sliceValue1 = new SliceValue(MOOD_WUNAI, ChartUtils.COLOR_ORANGE);
        values.add(sliceValue1);
        SliceValue sliceValue2 = new SliceValue(MOOD_BEISHANG, ChartUtils.COLOR_BLUE);
        values.add(sliceValue2);
        SliceValue sliceValue3 = new SliceValue(MOOD_YOUYU, ChartUtils.COLOR_VIOLET);
        values.add(sliceValue3);

        cursor.close();
        db.close();


    }


}
