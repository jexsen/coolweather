package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/8/30.
 */

//"suggestion": {
//        "comf": {
//        "type": "comf",
//        "brf": "较不舒适",
//        "txt": "白天天气多云，同时会感到有些热，不很舒适。"
//        },
//        "sport": {
//        "type": "sport",
//        "brf": "较适宜",
//        "txt": "天气较好，但因气温较高且风力较强，请适当降低运动强度并注意户外防风。"
//        },
//        "cw": {
//        "type": "cw",
//        "brf": "较适宜",
//        "txt": "较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"
//        }
//        }

public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("sport")
    public Sport sport;

    @SerializedName("cw")
    public CarWash carWash;

    public class Comfort{

        @SerializedName("txt")
        public String info;
    }

    public class CarWash{
        @SerializedName("txt")
        public String info;
    }

    public class Sport{
        @SerializedName("txt")
        public String info;
    }

}
