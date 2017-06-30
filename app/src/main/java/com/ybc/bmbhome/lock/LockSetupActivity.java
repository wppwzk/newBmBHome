package com.ybc.bmbhome.lock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ybc.bmbhome.R;
import com.ybc.bmbhome.function.DiaryActivity;
import com.ybc.bmbhome.utils.BaseActivity;
import com.ybc.bmbhome.view.LockPatternView;

import java.util.ArrayList;
import java.util.List;


/**
 * 设置手势密码
 */
public class LockSetupActivity extends BaseActivity implements
        LockPatternView.OnPatternListener, OnClickListener {

    private static final String TAG = "LockSetupActivity";
    private static final int STEP_1 = 1; // 开始
    private static final int STEP_2 = 2; // 第一次设置手势完成
    private static final int STEP_3 = 3; // 按下继续按钮
    private static final int STEP_4 = 4; // 第二次设置手势完成
    private LockPatternView lockPatternView;
    private Button leftButton;
    private Button rightButton;
    private TextView textView;
    // private static final int SETP_5 = 4; // 按确认按钮
    private int step;

    private List<LockPatternView.Cell> choosePattern;

    private boolean confirm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_setup);
        lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern_set);
        lockPatternView.setOnPatternListener(this);
        leftButton = (Button) findViewById(R.id.left_btn);
        rightButton = (Button) findViewById(R.id.right_btn);
        textView = (TextView) findViewById(R.id.text_shezhi_riji_mima);
        step = STEP_1;
        updateView();

        SystemBarTintManager localSystemBarTintManager = new SystemBarTintManager(this);
        localSystemBarTintManager.setStatusBarTintResource(R.color.colorGreen);
        localSystemBarTintManager.setStatusBarTintEnabled(true);
    }

    /**
     * 界面更新
     */
    private void updateView() {
        switch (step) {
            case STEP_1:
                leftButton.setText(R.string.cancel);
                rightButton.setText("下一步");
                rightButton.setEnabled(false);
                choosePattern = null;
                confirm = false;
                lockPatternView.clearPattern();
                lockPatternView.enableInput();
                break;
            case STEP_2:
                leftButton.setText(R.string.try_again);
                rightButton.setText("下一步");
                rightButton.setEnabled(true);
                lockPatternView.disableInput();
                break;
            case STEP_3:
                leftButton.setText(R.string.cancel);
                rightButton.setText("确认");
                textView.setText("请确认密码");
                rightButton.setEnabled(false);
                lockPatternView.clearPattern();
                lockPatternView.enableInput();
                break;
            case STEP_4:
                leftButton.setText(R.string.cancel);
                textView.setText("请确认密码");
                if (confirm) {
                    rightButton.setText(R.string.confirm);
                    rightButton.setEnabled(true);
                    lockPatternView.disableInput();
                } else {
                    rightButton.setText("确认");
                    lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                    lockPatternView.enableInput();
                    rightButton.setEnabled(false);
                }

                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.left_btn:
                if (step == STEP_1 || step == STEP_3 || step == STEP_4) {
                    Intent intent = new Intent("com.ybc.DIARY_CLOSE");
                    sendBroadcast(intent);
                } else if (step == STEP_2) {
                    step = STEP_1;
                    updateView();
                }
                break;

            case R.id.right_btn:
                if (step == STEP_2) {
                    step = STEP_3;
                    updateView();
                } else if (step == STEP_4) {

                    SharedPreferences preferences = getSharedPreferences(
                            DiaryActivity.LOCK, MODE_PRIVATE);
                    preferences.edit().putString(DiaryActivity.LOCK_KEY, LockPatternView.patternToString(choosePattern)).commit();

                    Intent intent = new Intent(this, LockActivity.class);
                    startActivity(intent);
                    finish();
                }

                break;

            default:
                break;
        }

    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

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
    }

    @Override
    public void onPatternDetected(List<LockPatternView.Cell> pattern) {
        Log.d(TAG, "onPatternDetected");

        if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE) {
            textView.setText("至少连接4个点，请重试");
            lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            return;
        }

        if (choosePattern == null) {
            choosePattern = new ArrayList<LockPatternView.Cell>(pattern);
            step = STEP_2;
            updateView();
            return;
        }

        if (choosePattern.equals(pattern)) {
            confirm = true;
        } else {
            textView.setText("错误，请重试");
            confirm = false;
        }

        step = STEP_4;
        updateView();

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

}
