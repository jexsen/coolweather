package com.coolweather.android.gson;

/**
 * Created by Administrator on 2018/8/30.
 */

//"aqi": {
//        "city": {
//        "aqi": "30",
//        "pm25": "13",
//        "qlty": "优"
//        }


public class AQI {
    public AQICity city;

    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
