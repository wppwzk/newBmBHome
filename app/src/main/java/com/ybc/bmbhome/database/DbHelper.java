package com.ybc.bmbhome.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 日记数据库类
 */
public class DbHelper extends SQLiteOpenHelper {

    // 数据库名
    public final static String DATABASE_NAME = "MyData.db";
    // 表名
    public final static String TABLE_NAME = "diary";
    public final static String TABLE_STEP = "user_step";
    public final static String TABLE_MOOD = "user_mood";
    // 表中的字段
    public final static String DIARY_ID = "id";
    public final static String DIARY_DATE = "date";
    public final static String DIARY_WEEK = "week";
    public final static String DIARY_TITLE = "title";
    public final static String DIARY_CONTENT = "content";
    public final static String DIARY_MOOD = "mood";

    public final static String MOOD_DATE = "date";
    public final static String MOOD_MOOD = "mood";
    public final static String MOOD_ID = "id";


    public static final String CREATE_USER_STEP = "create table if not exists user_step(" +
            "id integer primary key autoincrement," +
            "step_date date," +
            "step_count integer);";
    public static final String CREATE_USER_MOOD = "create table if not exists user_mood(" +
            "id integer primary key autoincrement," +
            "mood_date date," +
            "mood varchar(20));";
    // 数据库版本;
    public static int DATABASE_VERSION = 4;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + DIARY_ID + " INTEGER primary key autoincrement, "
                + DIARY_DATE + " text, " + DIARY_WEEK + " text," + DIARY_TITLE
                + " text," + DIARY_CONTENT + " text," + DIARY_MOOD + " text);";

        String CREATE_USER_MOOD = "CREATE TABLE IF NOT EXISTS " + TABLE_MOOD + " ("
                + MOOD_ID + " INTEGER primary key autoincrement, "
                + MOOD_DATE + " text," + MOOD_MOOD + " varchar(20));";


        db.execSQL(sql);
        db.execSQL(CREATE_USER_MOOD);
        //db.execSQL(CREATE_USER_STEP);
    }

    /**
     * 数据库升级时调用 删除数据库中原有的表，并重新创建新表
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        String sql2 = "DROP TABLE IF EXISTS " + TABLE_MOOD;
        db.execSQL("DROP TABLE IF EXISTS user_mood");
        db.execSQL(sql2);
        onCreate(db);
    }


}
