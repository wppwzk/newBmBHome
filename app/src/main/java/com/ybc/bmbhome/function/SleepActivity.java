package com.ybc.bmbhome.function;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.ybc.bmbhome.R;
import com.ybc.bmbhome.service.Music;
import com.ybc.bmbhome.utils.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 安心睡眠界面
 */
public class SleepActivity extends AppCompatActivity {
    public AudioManager audiomanage;
    int maxVolume, currentVolume;
    private Button bt1, bt2, bt3;
    private SeekBar sb1, sb2, sb3;
    private boolean b1 = false, b2 = false, b3 = false;
    private Music music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        bt1 = (Button) findViewById(R.id.button_paly1_sleep);
        bt2 = (Button) findViewById(R.id.button_play2_sleep);
        bt3 = (Button) findViewById(R.id.button_play3_sleep);
        sb1 = (SeekBar) findViewById(R.id.seekBar1);
        sb2 = (SeekBar) findViewById(R.id.seekBar2);
        sb3 = (SeekBar) findViewById(R.id.seekBar3);

        audiomanage = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  //获取系统最大音量
        currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);
        EventBus.getDefault().register(this);
        sb1.setMax(100);
        sb2.setMax(100);
        sb3.setMax(100);
        sb1.setProgress(100);
        sb2.setProgress(100);
        sb3.setProgress(100);

        Intent intent = new Intent(this, Music.class);
        startService(intent);

        reuest();
        changeButton();
        changeSeek();


    }

    /**
     * 音量变化
     */
    private void changeSeek() {
        sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int i = seekBar.getProgress();
                float sss = i / 100f;

                EventBus.getDefault().post(new MessageEvent("a1" + sss));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int i = seekBar.getProgress();
                float sss = i / 100f;
                EventBus.getDefault().post(new MessageEvent("a2" + sss));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int i = seekBar.getProgress();
                float sss = i / 100f;
                EventBus.getDefault().post(new MessageEvent("a3" + sss));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void reuest() {
        EventBus.getDefault().post(new MessageEvent("re"));
    }


    private void changeButton() {

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SleepActivity.this,"b1", Toast.LENGTH_SHORT).show();
                if (b1) {
                    EventBus.getDefault().post(new MessageEvent("m1pause"));
                    b1 = false;
                } else {
                    EventBus.getDefault().post(new MessageEvent("ifm1isplay"));
                    b1 = true;
                }
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b2) {
                    EventBus.getDefault().post(new MessageEvent("m2pause"));
                    b2 = false;
                } else {
                    EventBus.getDefault().post(new MessageEvent("ifm2isplay"));
                    b2 = true;
                }
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b3) {
                    EventBus.getDefault().post(new MessageEvent("m3pause"));
                    b3 = false;
                } else {
                    EventBus.getDefault().post(new MessageEvent("ifm3isplay"));
                    b3 = true;
                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessageEvent event) {
        String s = event.getMessage();
        switch (s) {
            case "m1isplay":
                b1 = true;
                break;
            case "m2isplay":
                b2 = true;
                break;
            case "m3isplay":
                b3 = true;
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
