package com.ybc.bmbhome.function;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ybc.bmbhome.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ybc on 2016/7/13.
 * 音乐播放
 */
public class VoiceActivity extends Activity {
    static String ACTION = "com.cryrabbit.music_touch";
    static ImageButton button;
    static MediaPlayer mp;
    static RemoteViews remoteViews;
    static Notification notification;
    static NotificationManager nm;
    static int flag = 0;
    TextView textView1, textView2;
    MyBroadCast broadCast;
    SeekBar seekBar;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    int position = mp.getCurrentPosition();
                    int time = mp.getDuration();
                    int max = seekBar.getMax();
                    seekBar.setProgress(position * max / time);
                    int m = position / 1000;
                    int a = m / 60;
                    int f = m % 60;
                    textView1.setText(a + ":" + f);
                    break;
                default:
                    break;
            }
        }
    };
    Timer timer;

    private static void pauseOrStart() {
        if (flag == 0) {
            flag = 1;
            mp.start();
            remoteViews.setImageViewResource(R.id.notifica_ib, R.drawable.music_pause);
            button.setBackgroundResource(R.drawable.music_pause);
        } else {
            flag = 0;
            remoteViews.setImageViewResource(R.id.notifica_ib, R.drawable.music_play);
            button.setBackgroundResource(R.drawable.music_play);
            mp.pause();
        }
        nm.notify(1, notification);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        mp = MediaPlayer.create(this, R.raw.sport);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                flag = 0;
                seekBar.setProgress(0);
                button.setBackgroundResource(R.drawable.music_play);
                remoteViews.setImageViewResource(R.id.notifica_ib, R.drawable.music_play);
                nm.notify(1, notification);
                textView1.setText("0:0");
            }
        });
        remoteViews = new RemoteViews(getPackageName(), R.layout.mynotification);
        textView1 = (TextView) findViewById(R.id.time_now);
        textView2 = (TextView) findViewById(R.id.time_max);
        seekBar = (SeekBar) findViewById(R.id.seek);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int dest = seekBar.getProgress();
                int time = mp.getDuration();
                int max = seekBar.getMax();
                mp.seekTo(time * dest / max);
                int m = (time * dest / max) / 1000;
                int f = m % 60;
                int a = m / 60;
                textView1.setText(a + ":" + f);
            }
        });
        Intent intent = new Intent(this, VoiceActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_action_music);
        builder.setContentTitle("BOMB");
        builder.setContentIntent(PendingIntent.getActivity(this, 0, intent, 0));
        builder.setOngoing(true);
        notification = builder.build();
        notification.contentView = remoteViews;
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        button = (ImageButton) findViewById(R.id.musicbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (flag == 1)
                    handler.sendEmptyMessage(0);
            }
        }, 0, 1000);


    }

    public void click() {
        if (flag == 0) {
            mp.start();
            flag = 1;
            if (broadCast == null) {
                broadCast = new MyBroadCast();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ACTION);
                registerReceiver(broadCast, intentFilter);
                remoteViews.setOnClickPendingIntent(R.id.notifica_ib, PendingIntent.getBroadcast(this, 1, new Intent(ACTION), PendingIntent.FLAG_UPDATE_CURRENT));
            }
            remoteViews.setImageViewResource(R.id.notifica_ib, R.drawable.music_pause);
            button.setBackgroundResource(R.drawable.music_pause);
            nm.notify(1, notification);
        } else {
            mp.pause();
            flag = 0;
            remoteViews.setImageViewResource(R.id.notifica_ib, R.drawable.music_play);
            button.setBackgroundResource(R.drawable.music_play);
            nm.notify(1, notification);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadCast != null)
            unregisterReceiver(broadCast);
        nm.cancel(1);
        flag = 0;
        mp.stop();
    }

    public static class MyBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION)) {
                pauseOrStart();
            }
        }
    }
}
