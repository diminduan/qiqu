package com.example.duand.qiqu.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duand.qiqu.Constants;
import com.example.duand.qiqu.FragmentActivity;
import com.example.duand.qiqu.R;
import com.example.duand.qiqu.Utils.GetHttpConnection;
import com.example.duand.qiqu.Utils.HttpPost;
import com.zaaach.citypicker.CityPickerActivity;
import org.angmarch.views.NiceSpinner;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class MineInfoActivity extends Activity {

    private ImageView back;
    private Button save;
    private TextView number;
    private EditText name;
    private EditText moto;
    private EditText qq;
    private EditText wechat;
    private NiceSpinner gender_spinner;
    private int user_id;
    private TextView address;
    private List<String> spinnerData;
    private String gender;

    private static final int REQUEST_CODE_PICK_CITY = 1;
    private TextView birthday;
    private int mYear;
    private int mMonth;
    private int mDay;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mineinfo);

        initView();
    }

    /**
     *     显示界面
     */
    private void initView() {
        //获取user_id
        Bundle bundle = this.getIntent().getExtras();
        user_id = bundle.getInt("user_id");

        back = (ImageView)findViewById(R.id.back);
        save = (Button)findViewById(R.id.save);
        number = (TextView)findViewById(R.id.number);
        name = (EditText)findViewById(R.id.name);
        moto = (EditText)findViewById(R.id.moto);
        birthday = (TextView)findViewById(R.id.birthday);
        address = (TextView)findViewById(R.id.address);
        qq = (EditText)findViewById(R.id.qq);
        wechat = (EditText)findViewById(R.id.wechat);
        gender_spinner = (NiceSpinner)findViewById(R.id.gender);

        spinnerData = new LinkedList<>(Arrays.asList("男","女"));
        gender = "男";
        gender_spinner.attachDataSource(spinnerData);
        gender_spinner.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);

        //性别选择事件
        gender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = gender_spinner.getSelectedItem().toString();
                Log.e("Check", "onItemSelected: "+ gender );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //生日选择事件
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //日期选择器
                new DatePickerDialog(MineInfoActivity.this, AlertDialog.THEME_HOLO_LIGHT,
                        onDateSetListener, mYear, mMonth, mDay).show();
            }
        });

        //选择地址事件
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MineInfoActivity.this,
                        CityPickerActivity.class),REQUEST_CODE_PICK_CITY);
            }
        });


        //返回事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //提交个人信息
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpdateData();

            }
        });

        /**
         *     获取个人信息
         */
        getData(user_id);

    }

    /**
     *   保存个人信息
     */
    private void UpdateData() {
        String url = Constants.newUrl + "setUserInfo";

        Handler handler = new Handler(){
            public  void handleMessage(Message msg){
                if (msg.what == 1){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MineInfoActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                            setResult(2);
                            finish();
                        }
                    },1000);

                }
                else {
                    Toast.makeText(MineInfoActivity.this,"出现错误",Toast.LENGTH_SHORT).show();
                }
            }
        };

        try {
            JSONObject user = new JSONObject();
            user.put("phoneNumber",number.getText().toString());
            user.put("userName",name.getText().toString());
            user.put("moto",moto.getText().toString());
            user.put("gender",gender);
            user.put("birthday",birthday.getText().toString());
            user.put("userAdress",address.getText().toString());
            user.put("qqId",qq.getText().toString());
            user.put("wechatId",wechat.getText().toString());

            new HttpPost(url,handler,user).start();  //上传数据

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     *   日期选择生日监听
     */
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mYear = year;
            mMonth = month;
            mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }
            }else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }
            }
            birthday.setText(days);
        }
    };

    /**
     *    获取用户信息
     * @param id
     */
    private void getData(int id){

        String url = Constants.newUrl+"user?"+"id="+ id;

        Handler handler = new Handler(){
            public  void handleMessage(Message msg){
                if (msg.what == 1){
                    String response = msg.obj.toString();
                    Log.e("check", "handleMessage: "+response );
                    try {
                        JSONObject result = new JSONObject(response);
                        number.setText(result.getString("phoneNumber"));
                        name.setText(result.getString("userName"));
                        moto.setText(result.getString("moto"));
                        gender_spinner.setText(result.getString("gender"));
                        birthday.setText(result.getString("birthday"));
                        address.setText(result.getString("userAddress"));
                        qq.setText(result.getString("qqId"));
                        wechat.setText(result.getString("wechatId"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        new GetHttpConnection(url,handler).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK){
            if (data != null){
                String city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                address.setText(city);
            }
        }
    }

}
