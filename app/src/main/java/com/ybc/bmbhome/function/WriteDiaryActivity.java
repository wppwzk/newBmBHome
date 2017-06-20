package com.ybc.bmbhome.function;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ybc.bmbhome.R;
import com.ybc.bmbhome.utils.BaseActivity;
import com.ybc.bmbhome.utils.DbUtil;
import com.ybc.bmbhome.utils.DiaryItem;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 写日记*/
public class WriteDiaryActivity extends BaseActivity {
    private Button mSaveBtn;
    private Spinner spinner;
    private EditText mTextTitle;
    private EditText mTextContent;

    private TextView mTextDate;
    private TextView mTextWeek;
    private TextView mTVTitle;

    private String flag;
    private String mId;
    private String nowMood = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);
        initView();

        Intent intent = getIntent();
        //获取标识，0代表写新日记，1代表查看并修改选中的日记
        flag = intent.getStringExtra("flag");
        if (flag.equals("0")) {
            mTVTitle.setText("写日记");
            initDate();
        } else {
            initUpdateData();
        }

        saveOnClick();

        SystemBarTintManager localSystemBarTintManager = new SystemBarTintManager(this);
        localSystemBarTintManager.setStatusBarTintResource(R.color.colorGreen);
        localSystemBarTintManager.setStatusBarTintEnabled(true);
    }


    /**
     * 初始化控件*/
    public void initView() {
        spinner = (Spinner) findViewById(R.id.spinner);
        mSaveBtn = (Button) findViewById(R.id.btn_save);
        mTextTitle = (EditText) findViewById(R.id.et_title);
        mTextContent = (EditText) findViewById(R.id.et_content);
        mTextDate = (TextView) findViewById(R.id.tv_date);
        mTextWeek = (TextView) findViewById(R.id.tv_week);
        mTVTitle = (TextView) findViewById(R.id.write_diary_text);
    }


    /**
     * 加载当天日期*/
    private void initDate() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(d);
        String week = DbUtil.getWeekOfDate(d);
        mTextDate.setText(date);
        mTextWeek.setText(week);
    }


    /**
     * 加载要更新的数据*/
    private void initUpdateData() {
        DiaryItem mDiary = (DiaryItem) getIntent().getSerializableExtra(DiaryActivity.SER_KEY);
        mId = mDiary.getId();
        mTextDate.setText(mDiary.getDate());
        mTextWeek.setText(mDiary.getWeek());
        mTextTitle.setText(mDiary.getTitle());
        mTextContent.setText(mDiary.getContent());
        String[] themood = getResources().getStringArray(R.array.diary_mood);
        for (int i = 0; i < themood.length; i++) {
            if (themood[i].equals(mDiary.getMood())) {
                spinner.setSelection(i, true);
                break;
            }
        }
        mTVTitle.setText("修改日记");
    }


    /**
     * 保存日记*/
    private void saveOnClick() {
        mSaveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DbUtil db = new DbUtil(WriteDiaryActivity.this);
                String title = mTextTitle.getText().toString();
                String content = mTextContent.getText().toString();

                if (title.equals("") || content.equals("")) {
                    Toast.makeText(WriteDiaryActivity.this, "标题和内容都不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    if (flag.equals("0")) {
                        db.insert(title, content, nowMood);
                        Toast.makeText(WriteDiaryActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();

                    } else {
                        db.update(mId, title, content, nowMood);
                        Toast.makeText(WriteDiaryActivity.this, "更新成功！", Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                //拿到被选择项的值
                // nowMood = (String) spinner.getSelectedItem();
                String[] languages = getResources().getStringArray(R.array.diary_mood);
                nowMood = languages[pos];
                //Toast.makeText(WriteDiaryActivity.this, "你点击的是:" + languages[pos], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }


    /**
     * 复写返回键功能*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            intent.setClass(WriteDiaryActivity.this, DiaryActivity.class);
            intent.putExtra("if_open_lock", false);
            startActivity(intent);
            finish();
            return true;
        }
        return false;

    }

}
