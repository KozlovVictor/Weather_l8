package com.geekbrains.weather;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.geekbrains.weather.base.BaseActivity;

public class PreferencesData implements PreferencesHelper {
    private SharedPreferences sharedPreferences;

    public PreferencesData(Activity activity) {
        this.sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public String getSharedPreferences(String keyPref) {
        return sharedPreferences.getString(keyPref, "");
    }

    @Override
    public void saveSharedPreferences(String keyPref, String value) {
        sharedPreferences.edit().putString(keyPref, value).apply();
    }

    @Override
    public void deleteSharedPreferences(String keyPref) {
        sharedPreferences.edit().remove(keyPref).apply();
    }
}
