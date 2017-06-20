package com.ybc.bmbhome.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.ybc.bmbhome.R;
import com.ybc.bmbhome.utils.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by ybc on 2016/5/13.
 * 睡眠音乐播放的后台服务
 */
public class Music extends Service {
    private MediaPlayer mediaPlayer1, mediaPlayer2, mediaPlayer3;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer1 = MediaPlayer.create(this, R.raw.bgm002);
        mediaPlayer2 = MediaPlayer.create(this, R.raw.bgm003);
        mediaPlayer3 = MediaPlayer.create(this, R.raw.bgm005);
        mediaPlayer1.setLooping(true);
        mediaPlayer2.setLooping(true);
        mediaPlayer3.setLooping(true);
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void changevo1(float a) {
        mediaPlayer1.setVolume(a, a);
    }

    public void changevo2(float a) {
        mediaPlayer2.setVolume(a, a);
    }

    public void changevo3(float a) {
        mediaPlayer3.setVolume(a, a);
    }


    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessage(MessageEvent event) {

        String s = event.getMessage();
        String as = s.substring(0, 2);
        if (as.equals("a1") || as.equals("a2") || as.equals("a3")) {
            String a2 = s.substring(2);
            float ss = Float.parseFloat(a2);
            switch (as) {
                case "a1":
                    if (mediaPlayer1.isPlaying())
                        changevo1(ss);
                    break;
                case "a2":
                    if (mediaPlayer2.isPlaying())
                        changevo2(ss);
                    break;
                case "a3":
                    if (mediaPlayer3.isPlaying())
                        changevo3(ss);
                    break;
            }
        }

        switch (s) {
            case "ifm1isplay":
                // if(!mediaPlayer1.isPlaying()) {
                mediaPlayer1.start();

                // }
                break;
            case "ifm2isplay":
                //if(!mediaPlayer2.isPlaying()){
                mediaPlayer2.start();

                //}
                break;
            case "ifm3isplay":
                //if(!mediaPlayer3.isPlaying()){
                mediaPlayer3.start();

                //}
                break;
            case "m1pause":
                if (mediaPlayer1.isPlaying()) {
                    mediaPlayer1.pause();
                }
                break;
            case "m2pause":
                if (mediaPlayer2.isPlaying()) {
                    mediaPlayer2.pause();
                }
                break;
            case "m3pause":
                if (mediaPlayer3.isPlaying()) {
                    mediaPlayer3.pause();
                }
                break;
            case "re":
                if (mediaPlayer1.isPlaying()) {
                    EventBus.getDefault().post(new MessageEvent("m1isplay"));
                }
                if (mediaPlayer2.isPlaying()) {
                    EventBus.getDefault().post(new MessageEvent("m2isplay"));
                }
                if (mediaPlayer2.isPlaying()) {
                    EventBus.getDefault().post(new MessageEvent("m3isplay"));
                }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer1.stop();
        mediaPlayer1.release();
        mediaPlayer2.stop();
        mediaPlayer2.release();
        mediaPlayer3.stop();
        mediaPlayer3.release();
        EventBus.getDefault().unregister(this);
    }


}
