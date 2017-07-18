package com.ybc.bmbhome.function;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ybc.bmbhome.R;
import com.ybc.bmbhome.adapter.MyDiaryItemAdapter;
import com.ybc.bmbhome.lock.LockActivity;
import com.ybc.bmbhome.lock.LockSetupActivity;
import com.ybc.bmbhome.utils.BaseActivity;
import com.ybc.bmbhome.utils.DbUtil;
import com.ybc.bmbhome.utils.DiaryItem;

import java.util.ArrayList;

/**
 * 日记本
 */
public class DiaryActivity extends BaseActivity {
    public static final String LOCK = "lock";
    public static final String LOCK_KEY = "lock_key";
    public final static String SER_KEY = "com.ybc.diary";

    ArrayList<DiaryItem> items;
    MyDiaryItemAdapter myAdapter;
    DbUtil util;

    private ListView mListView;
    private Button mBtnAdd;
    private String flag; // 标识是更新还是添加。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        SharedPreferences preferences = getSharedPreferences(LOCK, MODE_PRIVATE);
        String lockPattenString = preferences.getString(LOCK_KEY, null);
        mBtnAdd = (Button) findViewById(R.id.add_diary);
        SharedPreferences ss = getSharedPreferences("com.ybc.bmbhome_preferences", Context.MODE_PRIVATE);
        boolean ifPass = ss.getBoolean("if_diary_password", true);
        Intent intent3 = getIntent();
        boolean if_open_lock = intent3.getBooleanExtra("if_open_lock", true);
        if (ifPass && if_open_lock) {
            if (lockPattenString != null) {
                Intent intent = new Intent(this, LockActivity.class);
                startActivity(intent);

            } else {
                Intent intent = new Intent(this, LockSetupActivity.class);
                startActivity(intent);
            }
        } else {

        }
        initView();
        initData();
        addBtnOnClick();
        SystemBarTintManager localSystemBarTintManager = new SystemBarTintManager(this);
        localSystemBarTintManager.setStatusBarTintResource(R.color.colorGreen);
        localSystemBarTintManager.setStatusBarTintEnabled(true);
    }


    /**
     * 显示listview内容
     */
    private void initData() {
        util = new DbUtil(this);

        items = util.getAllData();

        myAdapter = new MyDiaryItemAdapter(items, this);

        mListView.setAdapter(myAdapter);
        // 点击查看详细内容
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long line) {
                String id = items.get(position).getId();
                String date = items.get(position).getDate();
                String week = items.get(position).getWeek();
                String title = items.get(position).getTitle();
                String content = items.get(position).getContent();
                String mood = items.get(position).getMood();
                flag = "1";

                DiaryItem mDiary = new DiaryItem();
                mDiary.setId(id);
                mDiary.setDate(date);
                mDiary.setWeek(week);
                mDiary.setTitle(title);
                mDiary.setContent(content);
                mDiary.setMood(mood);

                Intent intent = new Intent(DiaryActivity.this,
                        WriteDiaryActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(SER_KEY, mDiary);
                intent.putExtras(mBundle);
                intent.putExtra("flag", flag);
                startActivity(intent);
            }
        });
        // 长按弹出删除提示
        mListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu arg0,
                                            View arg1, ContextMenu.ContextMenuInfo arg2) {
                arg0.setHeaderTitle("是否删除");
                arg0.add(0, 0, 0, "删除");
                arg0.add(0, 1, 0, "取消");

            }
        });

    }


    /**
     * 提示菜单响应函数
     *
     * @param item 点击了菜单栏里面的第几个项目
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // item.getItemId()：点击了菜单栏里面的第几个项目");
        if (item.getItemId() == 0) {
            int selectedPosition = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;// 获取点击了第几行

            String id = items.get(selectedPosition).getId();
            // 删除日记
            util.delete(id);
            items.remove(items.get(selectedPosition));
            myAdapter.notifyDataSetChanged();

        }

        return super.onContextItemSelected(item);
    }


    /**
     * 初始化控件
     */
    private void initView() {
        // 我的日记界面
        //mLayoutList = (RelativeLayout) findViewById(R.id.layout_list);
        mListView = (ListView) findViewById(R.id.listView);
        mBtnAdd = (Button) findViewById(R.id.add_diary);
    }


    /**
     * 跳转至添加新日记
     */
    private void addBtnOnClick() {
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "0";
                Intent intent = new Intent();
                intent.setClass(DiaryActivity.this, WriteDiaryActivity.class);
                intent.putExtra("flag", flag);
                startActivity(intent);
                finish();
            }
        });
    }

    // 复写返回键功能
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent("com.ybc.DIARY_CLOSE");
            sendBroadcast(intent);
            return true;
        }
        return false;

    }


}
