package com.example.duand.qiqu.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duand.qiqu.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewMessageActivity extends Activity implements View.OnClickListener{

    private ImageView image;
    private ImageView back;
    private Button submit;
    private EditText message;
    private TextView time;

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_new_message);

        initView();
    }

    private void initView() {
        back = (ImageView)findViewById(R.id.back);
        submit = (Button)findViewById(R.id.submit);
        message = (EditText)findViewById(R.id.message);
        image = (ImageView)findViewById(R.id.image);
        time = (TextView)findViewById(R.id.time);

        back.setOnClickListener(this);
        submit.setOnClickListener(this);

        //记录当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date nowtime = new Date(System.currentTimeMillis());
        time.setText(simpleDateFormat.format(nowtime));

        //解析图片并显示
        Bundle bundle = getIntent().getExtras();
        byte [] b = bundle.getByteArray("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length);
        image.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                SubmitMessage();
                break;
                default:break;
        }

    }

    private void SubmitMessage() {
        finish();

    }
}
