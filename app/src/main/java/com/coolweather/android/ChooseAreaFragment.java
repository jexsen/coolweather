package com.coolweather.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/8/29.
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTY=2;

    private ProgressDialog progressDialog;

    private TextView titleText;

    private Button backButton;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList=new ArrayList<>(); //数据源


    /*
    省列表
     */
    private List<Province> provinceList;

     /*
    市列表
     */
    private List<City> cityList;

     /*
    县列表
     */
    private List<County> countyList;

  /*
   选中的省份
     */
    private Province selectedProvince;

      /*
    选中的城市
     */
    private City selectedCity;

      /*
    当前选中的级别
     */
    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.choose_area,container,false);//拿到view
        titleText= (TextView) view.findViewById(R.id.title_text);//控件实例化
        backButton= (Button) view.findViewById(R.id.back_button);
        listView= (ListView) view.findViewById(R.id.list_view);

        adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,dataList);//初始化适配器adapter

        listView.setAdapter(adapter);//设置适配器
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //当点击了某个省的时候会进入到这个方法，会根据当前的级别来判断是去调用queryCities方法还是queryCounties方法
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置点击事件 记得是OnItemClickListener
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (currentLevel==LEVEL_PROVINCE){//当前选中的级别为省列表
                    selectedProvince=provinceList.get(position);//选中的省份
                    queryCities();//查询市级数据
                }else if(currentLevel==LEVEL_CITY){
                    selectedCity=cityList.get(position);
                    queryCounties();//查询县级数据
                }else if(currentLevel==LEVEL_COUNTY){//从省市县列表跳转到天气界面
                    String weatherId=countyList.get(position).getWeatherID();

                    if (getActivity()instanceof MainActivity){//java小技巧，用instanceof关键字来判断一个对象是否属于某个类的实例
                        Intent intent=new Intent(getActivity(),WeatherActivity.class);
                        intent.putExtra("weather_id",weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    }else if (getActivity()instanceof WeatherActivity){//java小技巧，用instanceof关键字来判断一个对象是否属于某个类的实例
                        WeatherActivity activity= (WeatherActivity) getActivity();

                        activity.drawerLayout.closeDrawers();//关闭滑动菜单
                        activity.swipeRefresh.setRefreshing(true);//显示下拉刷新进度条
                        activity.requestWeather(weatherId);//请求新城市的天气信息
                    }


                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {//返回按钮的点击事件，会对当前的ListView列表级别进行判断，如果是县级列表，就返回市级列表，如果是市级列表，就返回省级列表
                                                                    //返回省级列表时，返回按钮会自动隐藏
            @Override
            public void onClick(View v) {
                if (currentLevel==LEVEL_COUNTY){
                    queryCities();
                }else if(currentLevel==LEVEL_CITY){
                    queryProvinces();
                }
            }
        });

        //直接运行这个程序加载省级数据
        queryProvinces();//加载省级数据

    }

    /*
    查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
     */

    private void queryProvinces(){
        titleText.setText("中国");//将头布局的标题设置成中国

        backButton.setVisibility(View.GONE);//将返回按钮隐藏

        provinceList= DataSupport.findAll(Province.class);//调用LitePal的查询接口从数据库中读取省级数据

        if (provinceList.size()>0){//如果查询到了，直接将数据显示在界面上
            dataList.clear();

            for (Province province:provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();//将数据显示在界面上

            listView.setSelection(0);

            currentLevel=LEVEL_PROVINCE;
        }else {//如果没有读取到就组装一个请求地址，调用queryFromServer方法从服务器上查询数据
            String address="http://guolin.tech/api/china";

            queryFromServer(address,"province");
        }
    }

    /*
    查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询
     */

    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());

        backButton.setVisibility(View.VISIBLE);

        cityList=DataSupport.where("provinceid=?",String.valueOf(selectedProvince.getId())).find(City.class);

        if (cityList.size()>0){
            dataList.clear();

            for (City city:cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();

            listView.setSelection(0);

            currentLevel=LEVEL_CITY;

        }else{
            int provinceCode=selectedProvince.getProvinceCode();

            String address="http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }
    }

    /*
    查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCounties(){
        titleText.setText(selectedCity.getCityName());

        backButton.setVisibility(View.VISIBLE);

        countyList=DataSupport.where("cityid=?",String.valueOf(selectedCity.getId())).find(County.class);

        if (countyList.size()>0){
            dataList.clear();

            for (County county:countyList){
                dataList.add(county.getCountyName());

            }
            adapter.notifyDataSetChanged();

            listView.setSelection(0);

            currentLevel=LEVEL_COUNTY;
        }else {
            int provinceCode=selectedProvince.getProvinceCode();

            int cityCode=selectedCity.getCityCode();

            String address="http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;

            queryFromServer(address,"county");
        }
    }

    /*
    根据传入的地址和类型从服务器上查询市县是数据
     */

    private void queryFromServer(String address,final String type){

        showProgressDialog();

        HttpUtil.sendOkHttpRequest(address, new Callback() {//向服务器发送请求
            @Override
            public void onFailure(Call call, IOException e) {
                //通过runOnUiThread()方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        closeProgressDialog();

                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {//发送请求，响应数据的回调
                String responseText=response.body().string();

                boolean result=false;

                if ("province".equals(type)){
                    result= Utility.handleProvinceResponse(responseText);//解析和处理服务器返回的省的数据，存储到数据库中
                }else if ("city".equals(type)){
                    result=Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if ("county".equals(type)){
                    result=Utility.handleCountyResponse(responseText,selectedCity.getId());
                }

                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();

                            if ("province".equals(type)){
                                queryProvinces();//再次调用queryProvinces()方法来重新加载省级数据,由于该方法牵扯到了UI操作，所以必须要在主线程中调用，需要写在runOnUiThread方法里
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });

    }

    /*
    显示进度对话框
     */
    private void showProgressDialog(){
        if (progressDialog==null){
            progressDialog=new ProgressDialog(getActivity());

            progressDialog.setMessage("正在加载...");

            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }


    /*
    关闭进度对话框
     */
    private void closeProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }





}
