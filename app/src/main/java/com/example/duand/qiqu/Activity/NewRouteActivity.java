package com.example.duand.qiqu.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.duand.qiqu.Constants;
import com.example.duand.qiqu.R;
import com.example.duand.qiqu.UIFragment.RouteFragment;
import com.example.duand.qiqu.Utils.GetImagePath;
import com.example.duand.qiqu.Utils.HttpPost;
import com.example.duand.qiqu.Utils.ProviderUtil;
import com.example.duand.qiqu.Utils.UploadUtil;

import org.angmarch.views.NiceSpinner;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import static com.example.duand.qiqu.Constants.route_savepath;

public class NewRouteActivity extends AppCompatActivity {

    private TextView cancel_add;
    private TextView submit_add;
    private EditText route_name;
    private NiceSpinner route_style;
    private EditText route_detail;
    private EditText route_desc;
    private ImageView route_icon;
    private List<String> spinnerData;
    private String style_name;
    private EditText route_city;
    private int user_id;

    String Url = Constants.URL + "RouteServlet";
    String loadUrl = Constants.URL + "uploadLet";

    String path = Environment.getExternalStorageDirectory()+"/Android/data/com.example.duand.qiqu/files/";
    File mGalleryFile = new File(path, "Gallery_File_NAME.jpg");//相册的File对象
    File mCropFile = new File(path, "Crop_FILE_NAME.jpg");//裁剪后的File对象


    private String fileName;

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_new_route);

        initView();
    }

    private void initView() {

        cancel_add = (TextView)findViewById(R.id.cancel_add);
        submit_add = (TextView)findViewById(R.id.submit_add);
        route_name = (EditText)findViewById(R.id.route_name);
        route_style = (NiceSpinner)findViewById(R.id.route_style);
        route_city = (EditText)findViewById(R.id.route_city);
        route_detail = (EditText)findViewById(R.id.route_detail);
        route_desc = (EditText)findViewById(R.id.route_desc);
        route_icon = (ImageView) findViewById(R.id.route_icon);

        Bundle bundle = this.getIntent().getExtras();
        user_id = bundle.getInt("user_id");

        //点起取消事件
        cancel_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //spinner选择事件
        spinnerData = new LinkedList<>(Arrays.asList("市区","公路","乡村","山地","休闲"));
        route_style.attachDataSource(spinnerData);
        route_style.setBackgroundResource(R.drawable.spinner_border);
        route_style.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
        style_name = "市区";
        route_style.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                style_name = route_style.getSelectedItem().toString();
                Log.e("Check", "onItemSelected: "+style_name );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //添加图片事件
        route_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto();

            }
        });

//        //发布事件
//        if (!route_name.getText().toString().trim().equals("")){      //route_name不为空
//            if (!route_city.getText().toString().trim().equals("")){    //route_city不为空
//                if(!route_detail.getText().toString().trim().equals("")){    //route_detail不为空
//                    if (!route_desc.getText().toString().trim().equals("")){     //route_desc不为空
//                        submit_add.setTextColor(getResources().getColor(R.color.smssdk_black));
//                        submit_add.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                SubmitRoute();
//                            }
//                        });
//                    }
//
//                }
//
//            }
//        }

        submit_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("check", "route_style:"+ style_name );
                SubmitRoute();
            }
        });

    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            if (msg.what == 1){
                String response = msg.obj.toString();
                Log.e("check", "handleMessage: "+ response );
                try{
                    JSONObject json = new JSONObject(response);
                    boolean result = json.getBoolean("json");
                    if (result){
                        Toast.makeText(NewRouteActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };



    private void SubmitRoute() {

        try {
            JSONObject route = new JSONObject();
            route.put("route_name", route_name.getText().toString());
            route.put("route_style",style_name);
            route.put("route_city", route_city.getText().toString());
            route.put("route_detail",route_detail.getText().toString());
            route.put("route_desc",route_desc.getText().toString());
            route.put("user_id",user_id);
            Log.e("check", "SubmitRoute: "+ route );

            new HttpPost(Url,handler,route).start();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(fileName);
                    Log.e("check", "fileName"+fileName);
                    Log.e("check", "file"+file);
                    if (file != null){
                        String request = UploadUtil.uploadFile(file,loadUrl);
                        Log.e("check", "request"+request);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //调用图库
    private void getPhoto() {

        //获取本地相册
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

            Uri targetUri = FileProvider.getUriForFile    //这里的authority需与AndroidManifest里面的provider的authority相同
                    (NewRouteActivity.this, ProviderUtil.getFileProviderName(this), mGalleryFile);
            Log.e("check", "getPhoto: "+ ProviderUtil.getFileProviderName(this ));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri);
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, 1);     //版本>7.0  图库后返回
        }
        else {
            startActivityForResult(intent, 2);        //版本<7.0  图库后返回
        }

    }
    //保存头像
    private void  setPhotoSave(Bitmap image) {
        String sdStatus = Environment.getExternalStorageState();
        //检查sd卡是否有用
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            return ;
        }

        File file = new File(route_savepath);
        file.mkdir();
        fileName = route_savepath + "route.jpg";
        try {
            FileOutputStream fo = new FileOutputStream(fileName);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fo);
            fo.flush();
            fo.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //设置裁剪方法
    private void CropPhoto(Uri inputUri) {

        if (inputUri == null) {
            Log.e("error","The uri is not exist.");
            return;
        }

        Intent intent = new Intent("com.android.camera.action.CROP");


        //  android7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            Uri outputUri = Uri.fromFile(mCropFile);
            intent.setDataAndType(inputUri,"image/*");

            intent.putExtra(MediaStore.EXTRA_OUTPUT,outputUri);
            intent.putExtra("noFaceDetection", false);//去除默认的人脸识别，否则和剪裁框重叠
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }else {
            Uri outputUri = Uri.fromFile(mCropFile);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                String url = GetImagePath.getPath(this,inputUri);
                intent.setDataAndType(Uri.fromFile(new File(url)),"image/*");
            }else {
                intent.setDataAndType(inputUri,"image/*");
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT,outputUri);
        }
        //设置裁剪

        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop","true");
        //  aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX",3);
        intent.putExtra("aspectY",2);
        //  outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX",140);
        intent.putExtra("outputY",100);
        intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
        //图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case 1:               //版本>7.0  图库后返回
                if (resultCode == RESULT_OK) {
                    File photoUri = new File(GetImagePath.getPath(this, data.getData()));
                    Uri dataUri = FileProvider.getUriForFile     //这里的authority需与AndroidManifest里面的provider的authority相同
                            (NewRouteActivity.this,
                                    ProviderUtil.getFileProviderName(NewRouteActivity.this), photoUri);
                    CropPhoto(dataUri);
                } else {
                    Toast.makeText(NewRouteActivity.this, "取消添加图片", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:         //版本<7.0  图库后返回
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    CropPhoto(uri);
                } else {
                    Toast.makeText(NewRouteActivity.this, "取消添加图片", Toast.LENGTH_SHORT).show();
                }
                break;

            case 3:
                Uri inputUri = FileProvider.getUriForFile
                        (NewRouteActivity.this,
                                ProviderUtil.getFileProviderName(NewRouteActivity.this), mCropFile);
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(NewRouteActivity.this.getContentResolver().openInputStream(inputUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                setPhotoSave(bitmap);
                route_icon.setImageBitmap(bitmap);

                break;
            default:
                break;
        }
    }

}
