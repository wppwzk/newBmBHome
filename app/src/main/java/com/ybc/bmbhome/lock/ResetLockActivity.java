package com.ybc.bmbhome.lock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ybc.bmbhome.R;
import com.ybc.bmbhome.function.DiaryActivity;
import com.ybc.bmbhome.utils.BaseActivity;
import com.ybc.bmbhome.view.LockPatternView;

import java.util.List;

/**
 * 重置密码
 */
public class ResetLockActivity extends BaseActivity implements
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

        SharedPreferences preferences = getSharedPreferences(DiaryActivity.LOCK,
                MODE_PRIVATE);
        String patternString = preferences.getString(DiaryActivity.LOCK_KEY,
                null);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // disable back key
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent("com.ybc.DIARY_CLOSE");
            sendBroadcast(intent);
            //finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPatternStart() {
        Log.d(TAG, "onPatternStart");
    }

    @Override
    public void onPatternCleared() {
        Log.d(TAG, "onPatternCleared");
    }

    @Override
    public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {
        Log.d(TAG, "onPatternCellAdded");
        Log.e(TAG, LockPatternView.patternToString(pattern));
        // Toast.makeText(this, LockPatternView.patternToString(pattern),
        // Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPatternDetected(List<LockPatternView.Cell> pattern) {
        Log.d(TAG, "onPatternDetected");

        if (pattern.equals(lockPattern)) {
            canclear = true;
            finish();
        } else {
            lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            /*Toast.makeText(this, R.string.lockpattern_error, Toast.LENGTH_LONG)
                    .show();*/
            textView.setText("密码错误");
        }

    }

}
