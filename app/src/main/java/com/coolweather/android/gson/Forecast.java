package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/8/30.
 */

/*
daily_forecast中包含的是一个数组，数组中的每一项都代表这未来一天的天气信息，针对这种情况，我们只需要定义出单日天气的实体类就可以了
然后在声明实体类引用的时候使用集合类型来进行声明。
"daily_forecast": [{
        "date": "2018-08-29",
        "cond": {
        "txt_d": "多云"
        },
        "tmp": {
        "max": "34",
        "min": "25"
        }
        }, {
        "date": "2018-08-30",
        "cond": {
        "txt_d": "多云"
        },
        "tmp": {
        "max": "34",
        "min": "26"
        }
        }, {
        "date": "2018-08-31",
        "cond": {
        "txt_d": "雷阵雨"
        },
        "tmp": {
        "max": "32",
        "min": "25"
        }
        }],

*/
public class Forecast {

    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature{
        public String max;
        public String min;
    }

    public class More{
        @SerializedName("txt_d")
        public String info;
    }






}
