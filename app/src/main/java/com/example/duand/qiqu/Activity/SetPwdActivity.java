package com.example.duand.qiqu.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duand.qiqu.Constants;
import com.example.duand.qiqu.R;
import com.example.duand.qiqu.Utils.GetHttpConnection;

public class SetPwdActivity extends AppCompatActivity {

    private EditText pwd;
    private EditText confirm_pwd;
    private Button reg_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Fade().setDuration(1000));
        setContentView(R.layout.activity_setpwd);

        initView();
    }

    private void initView() {

        Bundle bundle = this.getIntent().getExtras();     //获取number的值
        final String user_number = bundle.getString("user_number");
        Log.e("SetPwd", "phone=" + user_number);

        pwd = (EditText) findViewById(R.id.pwd);
        confirm_pwd = (EditText) findViewById(R.id.confirm_pwd);
        reg_btn = (Button) findViewById(R.id.reg_btn);

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pwd.getText().toString().trim().equals("")) {   //如果密码不为空
                    Log.e("pwd", "pwd:"+pwd.getText().toString() );
                    if (confirm_pwd.getText().toString().equals(pwd.getText().toString())) {     //密码输入一致
                        Log.e("conpwd", "conpwd:"+confirm_pwd.getText().toString() );
                        register(user_number, confirm_pwd.getText().toString());
                    }else {
                        Log.e("conpwd", "conpwd:"+confirm_pwd.getText().toString() );
                        Toast.makeText(SetPwdActivity.this,"密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(SetPwdActivity.this,"请输入密码", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String response = msg.obj.toString();
                Log.e("check", "handleMessage: "+response );
                String result = response.substring(1,7);
                Toast.makeText(SetPwdActivity.this,result,Toast.LENGTH_SHORT).show();
                if (result.equals("密码设置成功")){
                    startActivity(new Intent(SetPwdActivity.this,LoginActivity.class),
                            ActivityOptions.makeSceneTransitionAnimation(SetPwdActivity.this).toBundle());
                }
            }
        }
    };

    private void register(String user_number, String user_pwd) {

        String Url = Constants.newUrl + "register?" + "phoneNumber=" + user_number + "&&pwd=" + user_pwd;
        new GetHttpConnection(Url,handler).start();
    }



}
