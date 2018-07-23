package com.geekbrains.weather;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.content.Context.SENSOR_SERVICE;

public class WeatherFragment extends BaseFragment {

    private static final String ARG_COUNTRY = "ARG_COUNTRY";
    private String country;
    private SensorEventListener listenerLight;
    private SensorEventListener listenerTemperature;
    private SensorEventListener listenerHumidity;
    private SensorManager sensorManager;


    public WeatherFragment() {
//        Особенностью поведения android-а состоит в том, что в любой момент
//        он может убить конкретный фрагмент (с случаи нехватки памяти например)
//        и потом попытаться восстановить его, используя конструктор без параметров,
//                следовательно передача параметров через конструкторы черевата
//        крэшами приложения в произвольный момент времени.
    }

    public static WeatherFragment newInstance(String country) {
//        Для того что бы положить требуемые значения во фрагмент,
//        нужно обернуть их в Bundle и передать через метод setArguments.
//        Стандартным способом передачи параметров считается создание статического
//        метода newInstance (...),
//        а для восстановление параметров используется метод getArguments(...),вызываемый в
//        методе жизненного цикла onCreate (...) .
        Bundle args = new Bundle();
        args.putString(ARG_COUNTRY, country);
        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            country = getArguments().getString(ARG_COUNTRY);
        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_layout, container, false);
    }

    @Override
    protected void initLayout(View view, Bundle savedInstanceState) {

//       ((TextView) getBaseActivity().findViewById(R.id.tv_humidity)).setText("30%");
//        ((TextView) getBaseActivity().findViewById(R.id.tv_pressure)).setText("752mmHg");
        initSensors();

    }

    private void initSensors() {
        sensorManager = (SensorManager) getBaseActivity().getSystemService(SENSOR_SERVICE);
        Sensor temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        Sensor humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        listenerLight = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                ((TextView) getBaseActivity().findViewById(R.id.tv_pressure)).setText(sensorEvent.values[0] + " !!!");
                Log.d("Sensors", "Light " + sensorEvent.values[0]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        listenerTemperature = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                ((TextView) getBaseActivity().findViewById(R.id.bigTemp)).setText((sensorEvent.values[0] + "C"));
                Log.d("Sensors", "Tempersture" + sensorEvent.values[0]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        listenerHumidity = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                ((TextView) getBaseActivity().findViewById(R.id.tv_humidity)).setText((sensorEvent.values[0] + "%"));
                Log.d("Sensors", "humidity" + sensorEvent.values[0]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        sensorManager.registerListener(listenerTemperature, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listenerHumidity, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listenerLight, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listenerTemperature);
        sensorManager.unregisterListener(listenerHumidity);
        sensorManager.unregisterListener(listenerLight);
    }
}
