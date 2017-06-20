package com.ybc.bmbhome.utils;

import java.io.Serializable;

/**
 * 日记列表的实体类*/
public class DiaryItem implements Serializable {

    private static final long serialVersionUID = -7060210544600464481L;

    private String id;
    private String date;
    private String week;
    private String title;
    private String content;
    private String mood;


    public DiaryItem(String id, String date, String week, String title, String content, String mood) {
        this.id = id;
        this.date = date;
        this.week = week;
        this.title = title;
        this.content = content;
        this.mood = mood;

    }

    public DiaryItem() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }


}
