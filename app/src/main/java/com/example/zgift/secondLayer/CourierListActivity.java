package com.example.zgift.secondLayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zgift.R;
import com.example.zgift.adapter.CourierAdapter;
import com.example.zgift.domain.Courier;
import com.example.zgift.firstLayer.ManagerActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CourierListActivity extends AppCompatActivity {

    private ListView lv_couriers;
    private List<Courier> courierList = new ArrayList<Courier>();
    private String responseData;
    private String tag = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_list);
        lv_couriers = (ListView) findViewById(R.id.lv_couriers);
        initView();
    }

    public void initView(){

        courierList.clear(); // 集合初始化

        /**
         * 获取用户列表
         */
        Thread thread = null;


        thread =  new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url("http://192.168.102.205:8090/zgiftServer/getCouriersServlet")
                            .build();
                    Response response = client.newCall(request).execute();
                    responseData = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        parseJSONToList(responseData);
        //将数据显示在ListView中
        System.out.println(courierList.toString());
        CourierAdapter adapter = new CourierAdapter(CourierListActivity.this, R.layout.courier_item, courierList);

        lv_couriers.setAdapter(adapter);

        // 为每个列表项注册点击事件
        lv_couriers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tag = "0"; // 初始化标记
                Courier courier = courierList.get(position);
                final String courierId = "" + courier.getCourierId();

                final String customerId = getIntent().getStringExtra("customerId");

//                Toast.makeText(CourierListActivity.this, customerId + " " + courierId, Toast.LENGTH_SHORT).show();
                Thread thread = null;

                thread =  new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();

                            RequestBody requestBody = new FormBody.Builder()
                                    .add("courierId", courierId)
                                    .add("customerId", customerId)
                                    .build();

                            Request request = new Request.Builder()
                                    .url("http://192.168.102.205:8090/zgiftServer/addDistributeServlet")
                                    .post(requestBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            String isAdd = response.header("isAdd");
                            if("1".equals(isAdd)){
                                tag = "1";
                            }else{
                                tag = "0";
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if("1".equals(tag)){
                    Toast.makeText(CourierListActivity.this, "分配成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CourierListActivity.this, ManagerActivity.class);
                    startActivity(intent);
                    CourierListActivity.this.finish();
                }else{
                    Toast.makeText(CourierListActivity.this, "分配失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void parseJSONToList(String jsonData){
        // 运用 GSON 解析 JSON 数据
        Gson gson = new Gson();
        courierList = gson.fromJson(jsonData, new TypeToken<List<Courier>>(){}.getType());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CourierListActivity.this, ManagerActivity.class);
        startActivity(intent);
        CourierListActivity.this.finish();
    }
}