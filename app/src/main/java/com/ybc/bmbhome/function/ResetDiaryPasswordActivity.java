package com.ybc.bmbhome.function;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.ybc.bmbhome.R;
import com.ybc.bmbhome.lock.LockActivity;

/**
 * 重置日记本密码
 */
public class ResetDiaryPasswordActivity extends AppCompatActivity {
    public static final String LOCK = "lock";
    public static final String LOCK_KEY = "lock_key";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resert_diary_password);


        SharedPreferences preferences = getSharedPreferences(LOCK, MODE_PRIVATE);
        String lockPattenString = preferences.getString(LOCK_KEY, null);
        //textView= (TextView) findViewById(R.id.resetPassText);
        SharedPreferences ss = getSharedPreferences("com.ybc.bmbhome_preferences", Context.MODE_PRIVATE);
        boolean ifPass = ss.getBoolean("if_diary_password", true);

        if (ifPass) {
            if (lockPattenString != null) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ResetDiaryPasswordActivity.this);
                dialog.setTitle("确认");
                dialog.setMessage("是否要清除密码");
                dialog.setPositiveButton("清除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getSharedPreferences(LOCK, MODE_PRIVATE).edit().clear().commit();
                        finish();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dialog.show();

            } else {
                Toast.makeText(ResetDiaryPasswordActivity.this, "未设置密码", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(ResetDiaryPasswordActivity.this, "未启用密码功能", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (LockActivity.canclear) {
            // getSharedPreferences(LOCK, MODE_PRIVATE).edit().clear().commit();
        }
        //getSharedPreferences(LOCK, MODE_PRIVATE).edit().clear().commit();
    }
}
