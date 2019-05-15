package com.example.duand.qiqu.Activity;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.duand.qiqu.R;

public class ChooseActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_reg;
    private Button btn_log;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        initView();
    }

    private void initView() {
        btn_reg = (Button)findViewById(R.id.btn_register);
        btn_log = (Button)findViewById(R.id.btn_login);
        back = (ImageView)findViewById(R.id.back);
        btn_reg.setOnClickListener(this);
        btn_log.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                startActivity(new Intent(this,RegisterActivity.class),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                break;
            case R.id.btn_login:
                startActivity(new Intent(this,LoginActivity.class),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

                break;
                default:break;
        }

    }


}
