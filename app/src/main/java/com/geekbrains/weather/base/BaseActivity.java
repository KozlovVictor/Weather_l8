package com.geekbrains.weather.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.geekbrains.weather.Constants;
import com.geekbrains.weather.PreferencesData;
import com.geekbrains.weather.PreferencesHelper;
import com.geekbrains.weather.R;
import com.geekbrains.weather.WeatherFragment;
import com.geekbrains.weather.city.CreateActionFragment;
import com.geekbrains.weather.city.SelectedCity;
import com.geekbrains.weather.service.SensorService;

public class BaseActivity extends AppCompatActivity
        implements BaseView.View, BaseFragment.Callback, NavigationView.OnNavigationItemSelectedListener, CreateActionFragment.OnHeadlineSelectedListener {

    private static final String NAME = "NAME";
    private static final String CITIES = "CITIES";
    public static final String BROADCAST_ACTION = "BROADCAST_ACTION";
    public static final String LIGHT_SENSOR_VALUE = "LIGHT_SENSOR_VALUE";
    private static String country;
    private static String cities;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    //инициализация переменных
    private FloatingActionButton fab;
    private BroadcastReceiver broadcastReceiver;
    private PreferencesData preferencesData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        if (savedInstanceState != null) {
//            country = savedInstanceState.getString(NAME);
//            cities = savedInstanceState.getString(CITIES);
//        }
        setContentView(R.layout.activity_base);

        initLayout();

        Intent sensorServiceIntent = new Intent(BaseActivity.this, SensorService.class);
        startService(sensorServiceIntent);
        IntentFilter serviceFilter = new IntentFilter(BROADCAST_ACTION);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String sensorValue = String.valueOf(intent.getFloatExtra(LIGHT_SENSOR_VALUE, 0));
                Log.d(LIGHT_SENSOR_VALUE, sensorValue);
            }
        };
        registerReceiver(broadcastReceiver, serviceFilter);
    }


    public CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return collapsingToolbarLayout;
    }

    private void initLayout() {
        //устанавливает тулбар
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //устанавливаем drawer (выездное меню)
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //анимация клавищи (три палочки сверху) выездного меня
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //инициализация навигации
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        preferencesData = new PreferencesData(this);
        String city = preferencesData.getSharedPreferences(Constants.CITY);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(city);


        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(new CreateActionFragment());
            }
        });


        addFragment(WeatherFragment.newInstance(country));
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            addFragment1(new CreateActionFragment());
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(NAME, ((TextView) findViewById(R.id.tvUsername)).getText().toString());
//        outState.putString("CITIES", cities);
        super.onSaveInstanceState(outState);
    }


    private void addFragment(Fragment fragment) {
        //вызываем SupportFragmentManager и указываем в каком контейнере будет находиться наш фрагмент
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack("")
                .commit();
    }

    private void addFragment1(Fragment fragment) {
        //вызываем SupportFragmentManager и указываем в каком контейнере будет находиться наш фрагмент
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame2, fragment)
                .addToBackStack("")
                .commit();
    }


    private Fragment getCurrentFragment() {
        //получаем наименование фрагмента находящегося в контейнере в данных момент
        return getSupportFragmentManager().findFragmentById(R.id.content_frame);
    }

    @Override
    public void onBackPressed() {
        //закрываем drawer если он был открыт при нажатии на аппаратную клавишу назад
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        //TODO add search action
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                //TODO add settings fragment
                return true;
            case R.id.action_help:
                //TODO add help fragment
                return true;
        }
        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_help) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    //работаем с навигацией
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            // Handle the camera action
        } else if (id == R.id.nav_info) {
            // Handle the camera action
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Boolean inNetworkAvailable() {
        return true;
    }

    @Override
    public void initDrawer(String username, Bitmap profileImage) {

    }

    @Override
    public void onFragmentAttached() {


    }

    @Override
    public void onFragmentDetached(String tag) {

    }


//    public void startWeatherFragment(String country) {
//        //запускаем WeatherFragment и передаем туда country
//        addFragment(WeatherFragment.newInstance(country));
//        //country = country;
//
//    }
//
//    public Fragment getAnotherFragment() {
//        return getSupportFragmentManager().findFragmentById(R.id.content_frame);
//    }

    @Override
    public void onArticleSelected(SelectedCity selectedCity) {
        collapsingToolbarLayout.setTitle(selectedCity.getCity());
    }

}
