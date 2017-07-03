package com.ybc.bmbhome.utils;

import android.graphics.Bitmap;

/**
 * Created by YBC on 2017/7/2 20:36.
 */

public class ReadList {
    private int id;
    private String createTime;
    private String imgUrl;
    private String title;
    private Bitmap img;

    public ReadList(String title, String createTime, Bitmap img, int id, String imgUrl) {
        this.title = title;
        this.createTime=createTime;
        this.img=img;
        this.id=id;
        this.imgUrl=imgUrl;
    }

    public int getId() {
        return id;
    }

    public Bitmap getBitmap(){
        return img;
    }
    public String getCreateTime() {
        return createTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getTitle() {
        return title;
    }
}
