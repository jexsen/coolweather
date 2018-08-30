package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/8/30.
 */

//"basic": {
//        "cid": "CN101190102",
//        "location": "溧水",
//        "parent_city": "南京",
//        "admin_area": "江苏",
//        "cnty": "中国",
//        "lat": "31.65306091",
//        "lon": "119.0287323",
//        "tz": "+8.00",
//        "city": "溧水",
//        "id": "CN101190102",
//        "update": {
//        "loc": "2018-08-29 09:45",
//        "utc": "2018-08-29 01:45"
//        }

public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{

        @SerializedName("loc")
        public String updateTime;
    }

}
