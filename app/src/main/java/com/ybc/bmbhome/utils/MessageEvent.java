package com.ybc.bmbhome.utils;

/**
 * Created by YBC on 2017/4/24.
 * EventBus所需的类
 */

public class MessageEvent {
    private String message;


    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
