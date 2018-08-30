package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/8/30.
 */

public class Weather {

    public String status;//返回的天气数据中还会包含一向status数据，成功返回ok，失败返回具体原因

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;






}
