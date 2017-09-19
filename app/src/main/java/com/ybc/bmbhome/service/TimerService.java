package com.ybc.bmbhome.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.ybc.bmbhome.R;
import com.ybc.bmbhome.widget.StepWidget;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 设置stepwidget的timertask*/
public class TimerService extends Service {
    private Timer timer;
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public TimerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateViews();
            }
        },0,1000);
    }

    private void updateViews(){
        String time=sdf.format(new Date());
        RemoteViews rv=new RemoteViews(getPackageName(), R.layout.step_widget);
        rv.setTextViewText(R.id.appwidget_text,time);
        AppWidgetManager manager=AppWidgetManager.getInstance(getApplicationContext());
        ComponentName componentName=new ComponentName(getApplicationContext(), StepWidget.class);
        manager.updateAppWidget(componentName,rv);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        timer=null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
