package com.geekbrains.weather.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class SensorService extends Service {
    CallBack activity;
    private SensorManager sensorManager;
    private SensorEventListener listenerTemperature;
    private SensorEventListener listenerHumidity;
    private Sensor temperatureSensor;
    private Sensor humiditySensor;

    IBinder binder = new SensorServiceBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getApplicationContext().getSystemService(SENSOR_SERVICE);
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        listenerTemperature = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                String temperature = sensorEvent.values[0] + "C";
                activity.setTemperature(temperature);
//                Log.d("Sensors", "Tempersture" + sensorEvent.values[0]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        listenerHumidity = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                String humidity = sensorEvent.values[0] + "%";
                activity.setHumidity(humidity);
//                Log.d("Sensors", "humidity" + sensorEvent.values[0]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        sensorManager.registerListener(listenerTemperature, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listenerHumidity, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
        return binder;
    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(listenerTemperature);
        sensorManager.unregisterListener(listenerHumidity);
        super.onDestroy();
    }

    public class SensorServiceBinder extends Binder {
        public SensorService getService() {
            return SensorService.this;
        }
    }

    public void registerClient(Activity activity) {
        this.activity = (CallBack) activity;
    }

    public interface CallBack {
        void setTemperature(String value);

        void setHumidity(String value);
    }
}
