package com.example.zgift.secondLayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.zgift.R;
import com.example.zgift.adapter.CustomerOfHadAdapter;
import com.example.zgift.domain.CustomerOfHad;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CustomerListOfHadActivity extends AppCompatActivity {

    private ListView lv_customers_of_had;
    private List<CustomerOfHad> customerOfHadList = new ArrayList<CustomerOfHad>();
    private String responseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list_of_had);
        lv_customers_of_had = (ListView) findViewById(R.id.lv_customers_of_had);

        initView();
    }

    public void initView(){
        customerOfHadList.clear(); // 集合初始化

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
                            .url("http://192.168.102.205:8090/zgiftServer/getCustomersOfHadServlet")
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
        System.out.println(customerOfHadList.toString());
        CustomerOfHadAdapter adapter = new CustomerOfHadAdapter(CustomerListOfHadActivity.this, R.layout.customer_of_had_item, customerOfHadList);

        lv_customers_of_had.setAdapter(adapter);
    }

    private void parseJSONToList(String jsonData){
        // 运用 GSON 解析 JSON 数据
        Gson gson = new Gson();
        customerOfHadList = gson.fromJson(jsonData, new TypeToken<List<CustomerOfHad>>(){}.getType());
    }
}