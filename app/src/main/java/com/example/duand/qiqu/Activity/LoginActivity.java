package com.example.duand.qiqu.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duand.qiqu.Constants;
import com.example.duand.qiqu.FragmentActivity;
import com.example.duand.qiqu.R;
import com.example.duand.qiqu.Utils.GetHttpConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private ImageView back;
    private EditText login_phone;
    private EditText login_pwd;
    private Button btn_login;
    private TextView pwd_forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Fade().setDuration(1000));
        getWindow().setReturnTransition(new Explode().setDuration(800));
        setContentView(R.layout.activity_login);

        initEvent();

    }

    private void initEvent() {

        back = (ImageView)findViewById(R.id.back);
        login_phone = (EditText)findViewById(R.id.login_phone);
        login_pwd = (EditText)findViewById(R.id.login_pwd);
        btn_login = (Button)findViewById(R.id.btn_login);
        pwd_forgot = (TextView)findViewById(R.id.pwd_forgot);


        //返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();
            }
        });

        //登录
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!login_phone.getText().toString().trim().equals("")){     //手机号不为空
                    if (!login_pwd.getText().toString().trim().equals("")){    //密码不为空
                        login(login_phone.getText().toString(),login_pwd.getText().toString());
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"请输入密码", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this,"请输入账号", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //忘记密码
        pwd_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private Handler handler = new Handler(){

        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String response = msg.obj.toString();
                Log.e("check", "handleMessage: "+response );
                String result = response.substring(3,5);
                Log.e("check:", "result "+result );
                if (result.equals("成功")){
                    int user_id = Integer.parseInt(response.substring(6,8));
                    Intent intent = new Intent(LoginActivity.this,FragmentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("user_id",user_id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginActivity.this,response,Toast.LENGTH_SHORT).show();
                }
//
//                try {
//                    JSONObject result = new JSONObject(response);
//                    int user_id = result.getInt("user_id");
//                    String resMsg = result.getString("resMsg");
//                    Log.e("Check", "user_id:" +user_id);
//                    Log.e("Check", "resMsg:" +resMsg);
//
//                    Toast.makeText(LoginActivity.this,resMsg,Toast.LENGTH_SHORT).show();
//
//                    if (user_id != 0){
//                        Intent intent = new Intent(LoginActivity.this,FragmentActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putInt("user_id",user_id);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }
        }
    };

    private void login(String user_number, String user_pwd) {

        String Url = Constants.newUrl + "login?" + "userPhoneNumber=" + user_number + "&password=" + user_pwd;
//        String Url = "139.199.156.122:8088/user?4";
        new GetHttpConnection(Url,handler).start();
    }

}
