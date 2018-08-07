package com.geekbrains.weather.retrofit.controller;

import android.util.Log;

import com.geekbrains.weather.retrofit.OpenWeather;
import com.geekbrains.weather.retrofit.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherManager implements IWeatherManager {
private static final double KELVIN_TO_CELSIUS_DELTA = 273.15;
    private static final String BASE_URL = "http://api.openweathermap.org";
    OpenWeather openWeather;

    @Override
    public void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        openWeather = retrofit.create(OpenWeather.class);
    }

    @Override
    public void requestRetrofit(String cityCountry, String keyApi) {
        openWeather.loadWeather(cityCountry, keyApi).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response != null) {
                    int temp = (int) (response.body().getMain().getTemp() - KELVIN_TO_CELSIUS_DELTA);
                    Log.d("TAG", String.valueOf(temp));
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.d("TAG", t.getMessage());
            }
        });
    }
}
