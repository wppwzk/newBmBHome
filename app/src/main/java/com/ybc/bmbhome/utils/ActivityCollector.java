package com.ybc.bmbhome.utils;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YBC on 2017/5/10.
 * 用于控制activity的类
 * 将需要同时结束的activity加入list中
 */

public class ActivityCollector {
    public static List<AppCompatActivity> activities = new ArrayList<>();

    /**
     * 添加activity
     */
    public static void addActivity(AppCompatActivity activity) {
        activities.add(activity);
    }

    /**
     * 删除activity
     */
    public static void removeActivity(AppCompatActivity activity) {
        activities.remove(activity);
    }

    /**
     * 结束所有list中的activity
     */
    public static void finishAll() {
        for (AppCompatActivity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
