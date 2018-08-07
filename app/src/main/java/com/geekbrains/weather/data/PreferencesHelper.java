package com.geekbrains.weather.data;

public interface PreferencesHelper {

    String getSharedPreferences(String keyPref);

    void saveSharedPreferences(String keyPref, String value);

    void deleteSharedPreferences(String keyPref);
}
