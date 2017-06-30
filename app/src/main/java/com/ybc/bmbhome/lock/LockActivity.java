package com.ybc.bmbhome.lock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ybc.bmbhome.R;
import com.ybc.bmbhome.function.DiaryActivity;
import com.ybc.bmbhome.utils.BaseActivity;
import com.ybc.bmbhome.view.LockPatternView;

import java.util.List;

/**
 * 解锁界面
 */
public class LockActivity extends BaseActivity implements
        LockPatternView.OnPatternListener {
    private static final String TAG = "LockActivity";
    public static boolean canclear = false;
    private Toolbar toolbar;
    private TextView textView;
    private List<LockPatternView.Cell> lockPattern;
    private LockPatternView lockPatternView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(DiaryActivity.LOCK, MODE_PRIVATE);
        String patternString = preferences.getString(DiaryActivity.LOCK_KEY, null);
        if (patternString == null) {
            finish();
            return;
        }


        lockPattern = LockPatternView.stringToPattern(patternString);
        setContentView(R.layout.activity_lock);
        lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
        lockPatternView.setOnPatternListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar_jiesuo_mima);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        textView = (TextView) findViewById(R.id.text_jiesuo_mima);
        SystemBarTintManager localSystemBarTintManager = new SystemBarTintManager(this);
        localSystemBarTintManager.setStatusBarTintResource(R.color.colorGreen);
        localSystemBarTintManager.setStatusBarTintEnabled(true);
    }

    /**
     * 点击返回键将此activity和DiaryActivity一同关闭
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // disable back key
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent("com.ybc.DIARY_CLOSE");
            sendBroadcast(intent);
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPatternStart() {
    }

    @Override
    public void onPatternCleared() {
    }

    @Override
    public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {
    }

    @Override
    public void onPatternDetected(List<LockPatternView.Cell> pattern) {

        if (pattern.equals(lockPattern)) {
            canclear = true;
            finish();
        } else {
            lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            textView.setText("密码错误");
        }

    }

}
