package com.example.zgift.loginAndRegist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zgift.R;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistActivity extends AppCompatActivity {

    private EditText et_regist_username;
    private EditText et_regist_password;
    private Button btn_regist;
    private String tag = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        et_regist_username = (EditText) findViewById(R.id.et_regist_username);
        et_regist_password = (EditText) findViewById(R.id.et_regist_password);
        btn_regist = (Button) findViewById(R.id.btn_regist);

        initView();
    }

    public void initView(){

        // 监听注册按钮
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = et_regist_username.getText().toString();
                final String password = et_regist_password.getText().toString();

                if("".equals(username) || "".equals(password)){
                    Toast.makeText(RegistActivity.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                }else{
                    ProgressDialog progressDialog = new ProgressDialog(RegistActivity.this);
                    progressDialog.setTitle("正在注册");
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
                                        .url("http://192.168.102.205:8090/zgiftServer/courierRegistServlet")
                                        .post(requestBody)
                                        .build();
                                Response response = client.newCall(request).execute();
                                String isRegist = response.header("isRegist");
                                if("-1".equals(isRegist)){
                                    tag = "-1"; // 用户名已存在
                                }else if("0".equals(isRegist)){
                                    tag = "0";
                                }else{
                                    tag = isRegist;
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

                    if("-1".equals(tag)){
                        progressDialog.dismiss();
                        Toast.makeText(RegistActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                    }else if("0".equals(tag)){
                        progressDialog.dismiss();
                        Toast.makeText(RegistActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(RegistActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(RegistActivity.this);
                        dialog.setTitle("注册成功");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("返回登录", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                                startActivity(intent);
                                RegistActivity.this.finish();
                            }
                        });
                        dialog.show();
                    }
                }
            }
        });
    }
}