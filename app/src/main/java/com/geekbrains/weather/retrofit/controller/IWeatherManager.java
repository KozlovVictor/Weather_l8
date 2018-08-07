package com.geekbrains.weather.retrofit.controller;

public interface IWeatherManager {

    void initRetrofit();

    void requestRetrofit(String cityCountry, String keyApi);
}
