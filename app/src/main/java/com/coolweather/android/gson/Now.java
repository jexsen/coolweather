package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/8/30.
 */

//
//"now": {
//        "cloud": "0",
//        "cond_code": "100",
//        "cond_txt": "晴",
//        "fl": "31",
//        "hum": "67",
//        "pcpn": "0.0",
//        "pres": "1006",
//        "tmp": "30",
//        "vis": "10",
//        "wind_deg": "144",
//        "wind_dir": "东南风",
//        "wind_sc": "3",
//        "wind_spd": "18",
//        "cond": {
//        "code": "100",
//        "txt": "晴"
//        }

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{

        @SerializedName("txt")
        public String info;
    }
}
