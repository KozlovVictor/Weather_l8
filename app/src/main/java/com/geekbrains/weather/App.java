package com.geekbrains.weather;

import android.app.Application;

import es.dmoral.toasty.Toasty;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Настройка всплывающих окон оповещений
        Toasty.Config.getInstance().setSuccessColor(getResources().getColor(R.color.blue)).apply();
    }
}
