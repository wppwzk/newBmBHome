package com.ybc.bmbhome.function;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ybc.bmbhome.R;
import com.ybc.bmbhome.adapter.MyMobileAdapter;
import com.ybc.bmbhome.utils.App;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ybc on 2016/7/6.
 * App使用情况
 */
public class MobileActivity extends Activity {
    String TAG = "sdaf";
    private AlertDialog alertDialog;

    public static String getTimeFromInt(long time) {
        if (time <= 0) {
            return "0:00";
        }
        long secondnd = (time / 1000) / 60;
        long million = (time / 1000) % 60;
        long a = secondnd / 60;
        StringBuilder sb = new StringBuilder();
        if (a > 0)
            sb.append(a + "小时");
        sb.append(secondnd % 60 + "分钟");
        sb.append(million + "秒");
        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_one);
        int VERSION_CODES = Build.VERSION.SDK_INT;
        if (VERSION_CODES >= 21) {
            init5();
        } else {
            Toast.makeText(this, "系统版本不支持", Toast.LENGTH_LONG).show();
            init();
        }
        SystemBarTintManager localSystemBarTintManager = new SystemBarTintManager(this);
        localSystemBarTintManager.setStatusBarTintResource(R.color.colorGreen);
        localSystemBarTintManager.setStatusBarTintEnabled(true);
    }


    public void init() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            Log.i("sd", appProcess.processName);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void init5() {
        PackageManager pm = getApplicationContext().getPackageManager();

        if (ContextCompat.checkSelfPermission(MobileActivity.this,
                Manifest.permission.PACKAGE_USAGE_STATS) != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MobileActivity.this);
            alertDialog = builder.setTitle("系统提示")
                    .setMessage("需要打开应用的查看权限")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                            finish();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create();
        }

        Calendar beginCal = Calendar.getInstance();
        beginCal.add(Calendar.HOUR_OF_DAY, -1);
        Calendar endCal = Calendar.getInstance();
        UsageStatsManager manager = (UsageStatsManager) getApplicationContext().getSystemService(USAGE_STATS_SERVICE);
        List<UsageStats> stats = manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginCal.getTimeInMillis(), endCal.getTimeInMillis());
        if (stats.size() == 0)
            alertDialog.show();
        ArrayList<App> apps = new ArrayList<>();
        for (UsageStats us : stats) {
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(us.getPackageName(), PackageManager.GET_META_DATA);
                if ((applicationInfo.flags & applicationInfo.FLAG_SYSTEM) <= 0) {
                    Drawable icon = pm.getApplicationIcon(applicationInfo);
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    String t = format.format(new Date(us.getLastTimeUsed()));
                    String name = pm.getApplicationLabel(applicationInfo).toString();
                    String time = getTimeFromInt(us.getTotalTimeInForeground());
                    apps.add(new App(name, time, t, icon));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(new MyMobileAdapter(apps, this));

    }
}
