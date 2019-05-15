package com.example.duand.qiqu.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.Toast;

import com.example.duand.qiqu.Constants;
import com.example.duand.qiqu.R;
import com.example.duand.qiqu.Utils.GetHttpConnection;
import com.mob.MobSDK;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends AppCompatActivity {

    private ImageView back;
    private EditText phone;
    private EditText code;
    private Button get_code;
    private Button reg_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Fade().setDuration(1000));
        getWindow().setReturnTransition(new Explode().setDuration(800));

        setContentView(R.layout.activity_register);

        MobSDK.init(this);
        initEvent();
    }

    private void initEvent() {
        back = (ImageView)findViewById(R.id.back);
        phone = (EditText)findViewById(R.id.phone);
        code = (EditText)findViewById(R.id.code);

        get_code = (Button)findViewById(R.id.get_code);
        reg_btn = (Button)findViewById(R.id.reg_btn);

//        reg_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(RegisterActivity.this,SetPwdActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("user_number",phone.getText().toString());
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();
            }
        });

        get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!phone.getText().toString().trim().equals("")){     //手机号不为空
                    if(checkPhone(phone.getText().toString().trim())){      //手机号符合规范

                        JudgePhone(phone.getText().toString());

//                        SMSSDK.getVerificationCode("+86",phone.getText().toString());    //获取验证码
//                        TimeCount timeCount = new TimeCount(60000,1000);
//                        timeCount.start();
                    }else {
                        Toast.makeText(RegisterActivity.this,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(RegisterActivity.this,"请输入手机号",Toast.LENGTH_SHORT).show();
                }
            }
        });

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!phone.getText().toString().trim().equals("")){     //手机号不为空
                    if(!code.getText().toString().trim().equals("")){   //验证码不为空
                        SMSSDK.submitVerificationCode("+86",phone.getText().toString().trim(),
                                code.getText().toString());            //提交验证码
                    }else {
                        Toast.makeText(RegisterActivity.this,"请输入验证码",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(RegisterActivity.this,"请输入手机号",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //检验验证码
        EventHandler eventHandler = new EventHandler(){
            public void afterEvent(int event, int result, Object data){
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                new Handler(Looper.getMainLooper(), new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        int event = msg.arg1;
                        int result = msg.arg2;
                        Object data = msg.obj;
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                            if(result == SMSSDK.RESULT_COMPLETE){
                                msg.obj = "get code successfully";
                            }else {
                                ((Throwable)data).printStackTrace();
                            }
                        }else
                            if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                            if (result == SMSSDK.RESULT_COMPLETE){
                                msg.obj = "submit code successfully";
                                Toast.makeText(RegisterActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this,SetPwdActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("user_number",phone.getText().toString());
                                intent.putExtras(bundle);
                                startActivity(intent,
                                        ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this).toBundle());
                            }else {
                                msg.obj = "code is error";
                                Toast.makeText(RegisterActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                                ((Throwable)data).printStackTrace();
                            }
                        }
                        return false;
                    }
                }).sendMessage(msg);
            }
        };

        SMSSDK.registerEventHandler(eventHandler);    //注册短信回调
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String response = msg.obj.toString();
                Log.e("check", "response_register: "+ response);
                String result = response.substring(1,4);
                Log.e("check", "result_register: "+result );
                if (result.equals("该用户")){
                    Toast.makeText(RegisterActivity.this,response,Toast.LENGTH_SHORT).show();
                }else {
                    SMSSDK.getVerificationCode("+86",phone.getText().toString());    //获取验证码
                    TimeCount timeCount = new TimeCount(60000,1000);
                    timeCount.start();
                }
            }
        }
    };
    //判断号码是否被注册
    private void JudgePhone(String s) {

        String Url = Constants.newUrl + "registerCheckPN?" + "phoneNumber=" + s;
        new GetHttpConnection(Url,handler).start();

    }

    //正则手机号匹配原则
    private boolean checkPhone(String phone) {
        Pattern pattern = Pattern.compile("[1][3,4,5,7,8][0-9]{9}");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
    //获取验证码倒计时计数器
    private class TimeCount extends CountDownTimer{

        public TimeCount(long millsInFuture, long countDownInterval){
            super(millsInFuture,countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            get_code.setClickable(false);
            get_code.setText(millisUntilFinished/1000 + "s后获取");
        }

        @Override
        public void onFinish() {
            get_code.setClickable(true);
            get_code.setText("获取验证码");
        }
    }
}
