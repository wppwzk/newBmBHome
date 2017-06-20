package com.ybc.bmbhome.utils;

import android.graphics.drawable.Drawable;

/**
 * Created by ybc
 * MobileActivity的实体类
 */
public class App {
    public String name;
    public String time_long;
    public String time_last;
    public Drawable icon;

    public App() {
    }

    public App(String name, String time_long, String time_last, Drawable icon) {
        this.name = name;
        this.time_last = time_last;
        this.time_long = time_long;
        this.icon = icon;
    }

}
