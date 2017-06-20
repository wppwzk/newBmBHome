package com.ybc.bmbhome.utils;

/**
 * Created by YBC on 2017/4/23.
 * Recyclerview的实体类
 */

public class UserItem {
    private String userItemtxt;
    private int imageId;

    public UserItem(String userItem, int imageId) {
        this.userItemtxt = userItem;
        this.imageId = imageId;
    }

    public String getUserItemtxt() {
        return userItemtxt;
    }

    public int getImageId() {
        return imageId;
    }
}
