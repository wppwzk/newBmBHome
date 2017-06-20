package com.ybc.bmbhome.step;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;

public class Pedometer implements SensorEventListener {
    private static final int sensorTypeD = Sensor.TYPE_STEP_DETECTOR;
    private static final int sensorTypeC = Sensor.TYPE_STEP_COUNTER;
    public boolean step = true;
    private SensorManager mSensorManager;
    private Sensor mStepCount;
    private Sensor mStepDetector;
    private float mCount = 0;
    private float mDetector;
    private Context context;
    private List<Sensor> allSensors;
    private Sensor s;

    public Pedometer() {

    }

    public Pedometer(Context context) {
        this.context = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mStepCount = mSensorManager.getDefaultSensor(sensorTypeC);
        mStepDetector = mSensorManager.getDefaultSensor(sensorTypeD);
        allSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for (int i = 0; i < allSensors.size(); i++) {
            s = allSensors.get(i);
            switch (s.getType()) {
                case Sensor.TYPE_STEP_DETECTOR:
                    break;
                case Sensor.TYPE_STEP_COUNTER:
                    break;
                default:
                    step = false;
            }
        }
    }


    public void register() {
        register(mStepCount, SensorManager.SENSOR_DELAY_FASTEST);
        register(mStepDetector, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void unRegister() {
        mSensorManager.unregisterListener(this);
    }

    private void register(Sensor sensor, int rateUs) {
        mSensorManager.registerListener(this, sensor, rateUs);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        if (event.sensor.getType() == sensorTypeC) {
            setStepCount(event.values[0]);
        }
        if (event.sensor.getType() == sensorTypeD) {
            if (event.values[0] == 1.0) {
                mDetector++;
            }
        }
    }

    public float getStepCount() {
        return mCount;
    }

    private void setStepCount(float count) {
        this.mCount = count;
    }

    public float getmDetector() {
        return mDetector;
    }

}
