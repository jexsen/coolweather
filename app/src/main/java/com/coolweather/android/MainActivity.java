package com.coolweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //一开始先从本地读取缓存数据，如果不为null，说明之前已经请求过天气数据了，没必要让用户再次选择城市，而是直接跳转到WeatherActivity即可
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getString("weather",null)!=null){
            Intent intent=new Intent(this,WeatherActivity.class);

            startActivity(intent);

            finish();
        }
    }
}
