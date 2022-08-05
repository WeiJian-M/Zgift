package com.example.zgift.firstLayer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zgift.R;
import com.example.zgift.domain.CustomerMsg;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CourierActivity extends AppCompatActivity {

    private TextView tv_customer_scn_id;
    private TextView tv_customer_scn_name;
    private TextView tv_customer_scn_msg;
    private FloatingActionButton btn_scn_QR;
    private String JSONData;
    private String tag = "0"; //标记
    private long exitTime  = 0; //再按一次退出应用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        tv_customer_scn_id = (TextView) findViewById(R.id.tv_customer_scn_id);
        tv_customer_scn_name = (TextView) findViewById(R.id.tv_customer_scn_name);
        tv_customer_scn_msg = (TextView) findViewById(R.id.tv_customer_scn_msg);
        btn_scn_QR = (FloatingActionButton) findViewById(R.id.btn_scn_QR);

        initView();
    }

    public void initView(){
        btn_scn_QR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 创建IntentIntegrator对象
                IntentIntegrator intentIntegrator = new IntentIntegrator(CourierActivity.this);
                // 开始扫描
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 获取解析结果
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "取消扫描", Toast.LENGTH_LONG).show();
            } else {

                tag = "0"; //初始化标记

                final String courierId = getIntent().getStringExtra("courierId");

                JSONData = result.getContents();
                Gson gson = new Gson();
                CustomerMsg customerMsg = gson.fromJson(JSONData, CustomerMsg.class);
                final String customerId = "" + customerMsg.getCustomerId();
                String customerName = customerMsg.getCustomerName();
                String customerMessage = customerMsg.getCustomerMessage();

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
                                    .url("http://192.168.102.205:8090/zgiftServer/findDistributeServlet")
                                    .post(requestBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            String isDistribute = response.header("isDistribute");
                            if("1".equals(isDistribute)){
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
                    tv_customer_scn_id.setText(customerId);
                    tv_customer_scn_name.setText(customerName);
                    tv_customer_scn_msg.setText(customerMessage);
                    Toast.makeText(CourierActivity.this, "解析成功", Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(CourierActivity.this);
                    dialog.setTitle("您无权限查看该用户信息");
                    dialog.setMessage("请联系管理员为您分配权限");
                    dialog.setCancelable(true);
                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.show();
                    tv_customer_scn_id.setText("空");
                    tv_customer_scn_name.setText("空");
                    tv_customer_scn_msg.setText("空");
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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