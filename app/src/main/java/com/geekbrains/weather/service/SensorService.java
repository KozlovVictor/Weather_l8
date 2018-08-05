package com.geekbrains.weather.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.geekbrains.weather.BaseActivity;

public class SensorService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    Sensor lightSensor;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(this, lightSensor,
                sensorManager.SENSOR_DELAY_NORMAL);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            sendSensorData(sensorEvent.values);
        }
    }

    private void sendSensorData(float[] values) {
        Intent intent = new Intent(BaseActivity.BROADCAST_ACTION);
        intent.putExtra(BaseActivity.LIGHT_SENSOR_VALUE, values[0]);
        sendBroadcast(intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
