package com.example.zgift.firstLayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zgift.secondLayer.CourierListActivity;
import com.example.zgift.secondLayer.CustomerListOfHadActivity;
import com.example.zgift.R;
import com.example.zgift.adapter.CustomerAdapter;
import com.example.zgift.domain.Customer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ManagerActivity extends AppCompatActivity {

    private ListView lv_customers;
    private List<Customer> customerList = new ArrayList<Customer>();
    private String responseData;
    private Button btn_customer_of_had;
    private Button btn_refresh;
    private long exitTime  = 0; //再按一次退出应用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        lv_customers = (ListView) findViewById(R.id.lv_customers);
        btn_customer_of_had = (Button) findViewById(R.id.btn_customer_of_had);
        btn_refresh = (Button) findViewById(R.id.btn_refresh);

        initView();

        btn_refresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                initView();
            }
        });
    }

    public void initView(){

        customerList.clear(); // 集合初始化

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
                            .url("http://192.168.102.205:8090/zgiftServer/getCustomersOfNotServlet")
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
        System.out.println(customerList.toString());
        CustomerAdapter adapter = new CustomerAdapter(ManagerActivity.this, R.layout.customer_item, customerList);

        lv_customers.setAdapter(adapter);

        // 为每个列表项注册点击事件
        lv_customers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Customer customer = customerList.get(position);
                String customerId = "" + customer.getCustomerId();

                // 跳转到快递员列表页面，并传递客户 Id
                Intent intent = new Intent(ManagerActivity.this, CourierListActivity.class);
                intent.putExtra("customerId", customerId);
                startActivity(intent);
                ManagerActivity.this.finish();
            }
        });

        btn_customer_of_had.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerActivity.this, CustomerListOfHadActivity.class);
                startActivity(intent);
            }
        });
    }

    private void parseJSONToList(String jsonData){
        // 运用 GSON 解析 JSON 数据
        Gson gson = new Gson();
        customerList = gson.fromJson(jsonData, new TypeToken<List<Customer>>(){}.getType());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出应用", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}