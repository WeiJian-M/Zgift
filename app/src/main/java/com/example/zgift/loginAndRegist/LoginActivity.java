package com.example.zgift.loginAndRegist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zgift.firstLayer.CourierActivity;
import com.example.zgift.firstLayer.ManagerActivity;
import com.example.zgift.R;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;
    private RadioGroup rg_role;
    private RadioButton rb_courier;
    private RadioButton rb_manager;
    private TextView tv_click_regist;
    private Button btn_login;
    private String tag = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        rg_role = (RadioGroup) findViewById(R.id.rg_role);
        rb_courier = (RadioButton) findViewById(R.id.rb_courier);
        rb_manager = (RadioButton) findViewById(R.id.rb_manager);
        tv_click_regist = (TextView) findViewById(R.id.tv_click_regist);
        btn_login = (Button) findViewById(R.id.btn_login);

        initView();
    }

    public void initView(){

        // 给注册文字添加监听事件
        tv_click_regist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 给登录按钮添加点击事件
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tag = "0"; // 初始化标记
                final String username = et_username.getText().toString();
                final String password = et_password.getText().toString();

                if("".equals(username) || "".equals(password)){
                    Toast.makeText(LoginActivity.this, "用户名或密码无效或不正确", Toast.LENGTH_SHORT).show();
                }else{
                    if(rb_courier.isChecked()){ // 快递员登录

                        // 创建一个对话框，用于缓冲
                        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                        progressDialog.setTitle("正在登录");
                        progressDialog.setMessage("Loading");
                        progressDialog.show();

                        Thread thread = null;

                        thread = new Thread(new Runnable(){
                            @Override
                            public void run() {
                                try{
                                    OkHttpClient client = new OkHttpClient();
                                    RequestBody requestBody = new FormBody.Builder()
                                            .add("username", username)
                                            .add("password", password)
                                            .build();
                                    Request request = new Request.Builder()
                                            .url("http://192.168.102.205:8090/zgiftServer/courierLoginServlet")
                                            .post(requestBody)
                                            .build();
                                    Response response = client.newCall(request).execute();
                                    String isLogin = response.header("isLogin");
                                    if("0".equals(isLogin)){
                                        tag = "0";
                                    }else{
                                        tag = isLogin;
                                    }
                                }catch(Exception e){
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

                        if("0".equals(tag)){
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }else{
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, CourierActivity.class);
                            intent.putExtra("courierId", tag);
                            progressDialog.dismiss();//关闭对话框
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }

                    }else if(rb_manager.isChecked()){ // 管理员登录
//                        Toast.makeText(LoginActivity.this, "管理员登录", Toast.LENGTH_SHORT).show();
                        // 创建一个对话框，用于缓冲
                        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                        progressDialog.setTitle("正在登录");
                        progressDialog.setMessage("Loading");
                        progressDialog.show();

                        Thread thread = null;

                        thread = new Thread(new Runnable(){
                            @Override
                            public void run() {
                                try{
                                    OkHttpClient client = new OkHttpClient();
                                    RequestBody requestBody = new FormBody.Builder()
                                            .add("username", username)
                                            .add("password", password)
                                            .build();
                                    Request request = new Request.Builder()
                                            .url("http://192.168.102.205:8090/zgiftServer/managerLoginServlet")
                                            .post(requestBody)
                                            .build();
                                    Response response = client.newCall(request).execute();
                                    String isLogin = response.header("isLogin");
                                    if("0".equals(isLogin)){
                                        tag = "0";
                                    }else{
                                        tag = isLogin;
                                    }
                                }catch(Exception e){
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

                        if("0".equals(tag)){
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }else{
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, ManagerActivity.class);
                            intent.putExtra("courierId", tag);
                            progressDialog.dismiss();//关闭对话框
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, "请选择角色", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}