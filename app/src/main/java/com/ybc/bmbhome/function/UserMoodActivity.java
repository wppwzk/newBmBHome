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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * 我的心情
 */
public class UserMoodActivity extends AppCompatActivity {
    /*========= 控件相关 =========*/
    private PieChartView mPieChartView;                 //饼状图控件
    private Toolbar toolbar;
    private TextView textViewjianyi;
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
    private TextView textView;
    List<String> listmood = new ArrayList<>();
    List<SliceValue> values = new ArrayList<>();
    private int MOOD_HAPPY = 0;
    private int MOOD_WUNAI = 0;
    private int MOOD_BEISHANG = 0;
    private int MOOD_YOUYU = 0;
    private int allmood = 0;
    private int HAPPY_NUM = 0;
    private int WUNAI_NUM = 0;
    private int BETSHANG_NUM = 0;
    private int YOUYU_NUM = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_mood);
        dbHelper = new DbHelper(this);
        selectDb();
        mPieChartView = (PieChartView) findViewById(R.id.mood_pie_chart);
        toolbar = (Toolbar) findViewById(R.id.toolbar_user_mood_line);
        toolbar.setTitle("心情记录");
        textViewjianyi= (TextView) findViewById(R.id.textViewmoodjianyi);
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
        Tip();
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

        /*===== 设置相关属性 类似Line Chart =====*/
        mPieChartData = new PieChartData(values);
        mPieChartData.setHasLabels(isHasLabelsInside);
        mPieChartData.setHasLabelsOnlyForSelected(isPiesHasSelected);
        mPieChartData.setHasLabelsOutside(isHasLabelsOutside);
        mPieChartData.setHasCenterCircle(isHasCenterCircle);
        mPieChartData.setSlicesSpacing(2);                 //分离间距
        mPieChartData.setCenterText1("心情");             //显示单行文本文本内容
        mPieChartData.setHasCenterCircle(true);//设置饼图中间是否有第二个圈
        mPieChartData.setCenterText2("百分比");             //显示双行文本文本内容
        mPieChartData.setCenterText1FontSize(40);//设置文本大小1
        mPieChartData.setCenterText2FontSize(20);//设置文本大小2
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
            mPieChartView.setValueSelectionEnabled(true);
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
            //Toast.makeText(UserMoodActivity.this, values.get(arcIndex)+"", Toast.LENGTH_SHORT).show();
            String m = "心情";

            int num1 = (int) value.getValue();
            int num2 = allmood;
            // 创建一个数值格式化对象
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(2);
            String result = numberFormat.format((float) num1 / (float) num2 * 100);

            if (arcIndex == 0) {
                m = "快乐";
                HAPPY_NUM = num1 / num2;
            }
            if (arcIndex == 1) {
                m = "无奈";
                WUNAI_NUM = num1 / num2;
            }
            if (arcIndex == 2) {
                m = "悲伤";
                BETSHANG_NUM = num1 / num2;
            }
            if (arcIndex == 3) {
                m = "忧郁";
                YOUYU_NUM = num1 / num2;
            }


            mPieChartData.setCenterText1(m);             //显示单行文本文本内容
            mPieChartData.setCenterText2(result + " %");             //显示双行文本文本内容

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
        allmood = MOOD_HAPPY + MOOD_WUNAI + MOOD_BEISHANG + MOOD_YOUYU;
        SliceValue sliceValue = new SliceValue(MOOD_HAPPY, ChartUtils.COLOR_GREEN);
        values.add(sliceValue);
        SliceValue sliceValue1 = new SliceValue(MOOD_WUNAI, ChartUtils.COLOR_ORANGE);
        values.add(sliceValue1);
        SliceValue sliceValue2 = new SliceValue(MOOD_BEISHANG, ChartUtils.COLOR_RED);
        values.add(sliceValue2);
        SliceValue sliceValue3 = new SliceValue(MOOD_YOUYU, ChartUtils.COLOR_VIOLET);
        values.add(sliceValue3);

        cursor.close();
        db.close();


    }

    private void Tip(){
        int[] s = {MOOD_HAPPY, MOOD_WUNAI, MOOD_BEISHANG, MOOD_YOUYU};
        Arrays.sort(s);

        if (s[3] == MOOD_HAPPY) {
            textView.setText("最近的心情是：快乐");
            textViewjianyi.setText("你最近的心情不错，继续保持。人生真正的快乐，在于能对一个事业有所贡献，而自己认识到这是个伟大的事业，人生最大的快乐不在于占有什么，而在于追求什么的过程中。");
        }
        if (s[3] == MOOD_WUNAI) {
            textView.setText("最近的心情是：无奈");
            textViewjianyi.setText("生活尽管不完美，而我却依然微笑。人生可以走的路很多，有些是我们自己可以选择的，有些却是我们必须去接受的。能认识自己的失去和欠缺，勇敢的面对和承担，并能继续向前走，欣赏自己的生活，也享受生活的过程，这就是我们应有的人生态度。");
        }
        if (s[3] == MOOD_BEISHANG) {
            textView.setText("最近的心情是：悲伤");
            textViewjianyi.setText("人的一生中最重要的不是名利，不是富足的生活，而是得到真爱。有一个人爱上你的所有，你的苦难与欢愉，眼泪和微笑，每一寸肌肤，身上每一处洁净或肮脏的部分。真爱是最伟大的财富，也是唯一货真价实的财富。如果在你活了一回，未曾拥有过一个人对你的真爱，这是多么遗憾的人生啊！");
        }
        if (s[3] == MOOD_YOUYU) {
            textView.setText("最近的心情是：忧郁");
            textViewjianyi.setText("人生有境界，就是能以平和的心态面对、坚韧的方式应对，很多老人见多识广，人生达到一定的境界后，变得慈祥、宽厚，与人相处温和、平和、协调。遇到事情的时候，不会有特别强烈的表现，能坚守自己固有的这种东西，面对人生各种各样的问题。");
        }
    }

}
