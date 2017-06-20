package com.ybc.bmbhome.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.ybc.bmbhome.database.DbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 数据库操作的工具类*/
public class DbUtil {

    private SQLiteDatabase db;
    private DbHelper dbHelper;

    public DbUtil(Context context) {
        dbHelper = new DbHelper(context);
    }

    /**
     * 获取星期数
     * @param  dt 日期
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 开启数据库*/
    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 关闭数据库*/
    public void close() {
        dbHelper.close();
    }


    /**
     * 日记的插入数据操作
     * @param title 日记题目
     * @param content 日记内容
     * @param mood 心情*/
    public void insert(String title, String content, String mood) {

        this.open();
        /* ContentValues */
        ContentValues cv = new ContentValues();

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(d);

        String week = getWeekOfDate(d);

        cv.put(DbHelper.DIARY_DATE, date);
        cv.put(DbHelper.DIARY_WEEK, week);

        cv.put(DbHelper.DIARY_TITLE, title);
        cv.put(DbHelper.DIARY_CONTENT, content);
        cv.put(DbHelper.DIARY_MOOD, mood);

        db.insert(DbHelper.TABLE_NAME, null, cv);
        this.close();
    }

    /**
     * 心情的插入数据库*/
    public void insertMood(String mood) {
        this.open();
        /* ContentValues */
        ContentValues cv = new ContentValues();

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(d);
        cv.put(DbHelper.MOOD_DATE, date);
        cv.put(DbHelper.MOOD_MOOD, mood);
        db.insert(DbHelper.TABLE_MOOD, null, cv);
        this.close();
    }

    /**
     * 步数的插入数据看操作*/
    public void insertStep(int step) {
        this.open();
        ContentValues cs = new ContentValues();
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(d);
        String week = getWeekOfDate(d);
        db.insert(DbHelper.TABLE_STEP, null, cs);
        this.close();

    }


    /**
     * 删除数据操作*/
    public boolean delete(String id) {
        this.open();
        String where = DbHelper.DIARY_ID + " = ?";
        String[] whereValue = {id};
        if (db.delete(DbHelper.TABLE_NAME, where, whereValue) > 0) {
            this.close();
            return true;
        }
        return false;
    }


    /**
     * 更新数据操作*/
    public boolean update(String id, String title, String content, String mood) {
        this.open();
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.DIARY_TITLE, title);
        cv.put(DbHelper.DIARY_CONTENT, content);
        cv.put(DbHelper.DIARY_MOOD, mood);
        if (db.update(DbHelper.TABLE_NAME, cv, DbHelper.DIARY_ID + "=?", new String[]{id}) > 0) {
            this.close();
            return true;
        }
        db.close();
        return false;
    }

    /**
     * 关闭数据库*/
    public void closeDB() {
        db.close();
    }


    /**
     * 获取所有数据*/
    public ArrayList<DiaryItem> getAllData() {
        this.open();
        ArrayList<DiaryItem> items = new ArrayList<DiaryItem>();
        Cursor cursor = db.rawQuery("select * from diary", null);

        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor
                        .getColumnIndex(DbHelper.DIARY_ID));
                String date = cursor.getString(cursor
                        .getColumnIndex(DbHelper.DIARY_DATE));
                String week = cursor.getString(cursor
                        .getColumnIndex(DbHelper.DIARY_WEEK));
                String title = cursor.getString(cursor
                        .getColumnIndex(DbHelper.DIARY_TITLE));
                String content = cursor.getString(cursor
                        .getColumnIndex(DbHelper.DIARY_CONTENT));
                String mood = cursor.getString(cursor
                        .getColumnIndex(DbHelper.DIARY_MOOD));
                DiaryItem item = new DiaryItem(id, date, week, title, content, mood);
                items.add(item);
                cursor.moveToNext();
            }
        }
        this.close();
        return items;
    }
}
