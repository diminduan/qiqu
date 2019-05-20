package com.example.duand.qiqu.UIFragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.duand.qiqu.Activity.NewMessageActivity;
import com.example.duand.qiqu.Adapter.DynamicAdapter;
import com.example.duand.qiqu.Adapter.PersonalDynamicAdapter;
import com.example.duand.qiqu.Constants;
import com.example.duand.qiqu.JavaBean.Dynamic;
import com.example.duand.qiqu.JavaBean.User;
import com.example.duand.qiqu.R;
import com.example.duand.qiqu.Utils.GetHttpConnection;
import com.example.duand.qiqu.Utils.GetImagePath;
import com.example.duand.qiqu.Utils.ListViewForScrollView;
import com.example.duand.qiqu.Utils.ProviderUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Inflater;

import static android.app.Activity.RESULT_OK;
import static com.example.duand.qiqu.Constants.back_savepath;
import static com.example.duand.qiqu.Constants.head_savepath;

public class SocietyFragment extends Fragment implements View.OnClickListener{


    private SwipeRefreshLayout society_refresh;
    private ImageView society_back;
    private ImageView add_message;
    private ImageView society_head;
    private ListViewForScrollView society_list;
    private List<Dynamic> list;
    private DynamicAdapter adapter;
    private int user_id;
    private Button btn_camera;
    private Button btn_gallery;
    private int dynamic_userId;

    private static final String TAG = "SocietyFragment";

    String path = Environment.getExternalStorageDirectory()+"/Android/data/com.example.duand.qiqu/files/";
    File mCameraFile = new File(path, "Camera_FILE_NAME.jpg");//照相机的File对象
    File mCropFile = new File(path, "Crop_FILE_NAME.jpg");//裁剪后的File对象
    File mGalleryFile = new File(path, "Gallery_File_NAME.jpg");//相册的File对象
    private Button btn_back;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.society_fragment, container, false);

        society_refresh = (SwipeRefreshLayout)view.findViewById(R.id.society_refresh);
        society_back = (ImageView)view.findViewById(R.id.society_back);
        add_message = (ImageView)view.findViewById(R.id.add_message);
        society_head = (ImageView)view.findViewById(R.id.society_head);
        society_list = (ListViewForScrollView)view.findViewById(R.id.society_list);

        add_message.setOnClickListener(this);
        society_back.setOnClickListener(this);

        //获取user_id
        Bundle bundle = this.getArguments();
        user_id = bundle.getInt("user_id");
        Log.e("check", "user_id: "+ user_id);

        //头像获取
        Bitmap bitmap = BitmapFactory.decodeFile(head_savepath +user_id+ "head.jpg");
        if (bitmap != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bitmap);  //将bitmap转换成drawable
            society_head.setImageDrawable(drawable);
        }
        //背景获取
        Bitmap bitmap1 = BitmapFactory.decodeFile(back_savepath +user_id+ "back.jpg");
        if (bitmap1 != null) {
            Drawable drawable = new BitmapDrawable(bitmap1);  //将bitmap转换成drawable
            society_back.setImageDrawable(drawable);
        }

        //下拉刷新动态
        society_refresh.setColorSchemeResources(R.color.start,R.color.middle,R.color.end);
        society_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //设置2s的时间来执行后续操作
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity()," you refresh it ",Toast.LENGTH_SHORT).show();
                        society_refresh.setRefreshing(false);
                    }
                },2000);
            }
        });

        showDiary();   //展示动态

        return view;
    }

    //展示动态
    private void showDiary() {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Bitmap bitmap = null;
//                try (InputStream pathstream = new URL(URL).openStream()) {
//                    bitmap = BitmapFactory.decodeStream(pathstream);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }if (bitmap != null) {
//                    @SuppressWarnings("deprecation")
//                    Drawable drawable = new BitmapDrawable(bitmap);  //将bitmap转换成drawable
//                    society_back.setImageDrawable(drawable);
//                }
//            }
//        }).start();


        list = new LinkedList<Dynamic>();
        String dynamic_url = Constants.newUrl + "getDynamic?"+"userId="+user_id+"&pageNumber=1";

        Handler handler = new Handler(){

            public void handleMessage(Message msg) {
                if (msg.what == 1){
                    String response = msg.obj.toString();
                    Log.e("dynamic_check", "response: "+response );
                    if (response.equals("[]")){
                        Toast.makeText(getActivity(),"未加载到更多数据", Toast.LENGTH_SHORT).show();
                    }
                    try {

                        JSONArray jsonArray = new JSONArray(response);
                        Log.e(TAG, "jsonArray:"+jsonArray );
                        for (int i=0; i<jsonArray.length();i++){
                            JSONObject dynamic = jsonArray.getJSONObject(i);
                            String content = dynamic.getString("content");
                            int good_count = dynamic.getInt("thumbUpCount");
                            String time = dynamic.getString("createTime");
                            JSONObject user = dynamic.getJSONObject("user");
//                            String jsonStr = JSON.toJSONString(dynamic, SerializerFeature.DisableCircularReferenceDetect);
                            Log.e(TAG, "user:"+user );
                            dynamic_userId = user.getInt("userId");
                            String name = user.getString("userName");
                            Log.e(TAG, "dynmc_user_id:"+dynamic_userId );

                            LayoutInflater inflater = LayoutInflater.from(getActivity());
                            View view = inflater.inflate(R.layout.dairy_list_item,null);
                            ImageView small_head_iv = (ImageView)view.findViewById(R.id.small_head_iv);
                            Bitmap bitmap = BitmapFactory.decodeFile(head_savepath + dynamic_userId + "head.jpg");
                            Log.e(TAG, "bitmsp:"+bitmap);
                            if (bitmap != null) {
                                @SuppressWarnings("deprecation")
                                Drawable drawable = new BitmapDrawable(bitmap);  //将bitmap转换成drawable
                                small_head_iv.setImageDrawable(drawable);
                            }
                            long new_time = Long.parseLong(time);
                            Date date = new Date(new_time);
                            String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
//                            Log.e(TAG, "nickname2in_dynamic:"+name );
                            list.add(new Dynamic(name,content,R.mipmap.picture_1,dateStr,good_count));
                            adapter.notifyDataSetChanged();
                            Log.e(TAG, "list: "+list );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(),"未查询到信息", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        Thread dynamic = new GetHttpConnection(dynamic_url,handler);
        dynamic.setPriority(5);   //设置线程低优先级
        dynamic.start();

        adapter = new DynamicAdapter((LinkedList<Dynamic>) list,getActivity());
        society_list.setAdapter(adapter);

//        list.add(new Dynamic(R.mipmap.head_1,"小老弟",
//                "骑行，绿色，健康，快乐！",R.mipmap.picture_1));
//        list.add(new Dynamic(R.mipmap.head_2,"小茄子",
//                "表白广播站\n" +
//                        "遗失一把雨伞\n" +
//                        "世界微笑日",R.mipmap.picture_2));
//        list.add(new Dynamic(R.mipmap.head_3,"小可爱",
//                "wherever you are,always with you .",R.mipmap.picture_3));
//        list.add(new Dynamic(R.mipmap.head_4,"小蜘蛛",
//                "我能想到最幸福的事，就是和你骑车去看日落",R.mipmap.picture_4));
//        adapter = new DynamicAdapter((LinkedList<Dynamic>) list,getActivity());
//        society_list.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_message:
                newMessageDialog();       //写动态调用
                break;
            case R.id.society_back:
                newBackDialog();        //改背景调用
                break;
                default:break;
    }

    }

    //改背景提醒Dialog
    private void newBackDialog() {
        View view = getLayoutInflater().inflate(R.layout.getback_dialog,null);
        final Dialog dialog = new Dialog(getActivity(),R.style.DialogWindowStyle);
        dialog.setContentView(view,new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

        Window window =dialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.y = -360;
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        //设置显示距离
        dialog.onWindowAttributesChanged(wl);
        //设置点击聚焦以外取消活动
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
        btn_back = (Button)window.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetImageInGallery("back");
                dialog.dismiss();
            }
        });

    }

    //写动态提醒Dialog
    private void newMessageDialog() {
            View view = getLayoutInflater().inflate(R.layout.getimage_dialog,null);
            final Dialog dialog = new Dialog(getActivity(),R.style.DialogWindowStyle);
            dialog.setContentView(view,new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

            Window window =dialog.getWindow();
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.y = -360;
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            //设置显示距离
            dialog.onWindowAttributesChanged(wl);
            //设置点击聚焦以外取消活动
            dialog.setCanceledOnTouchOutside(true);

            dialog.show();
            btn_camera = (Button)window.findViewById(R.id.btn_camera);
            btn_gallery = (Button)window.findViewById(R.id.btn_gallery);

            btn_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetImageInCamera();
                    dialog.dismiss();
                }
            });

            btn_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetImageInGallery("message");
                    dialog.dismiss();

                }
            });

    }

    //从图库选择图片
    private void GetImageInGallery(String s) {
        //获取本地相册
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (s.equals("message")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

                Uri targetUri = FileProvider.getUriForFile    //这里的authority需与AndroidManifest里面的provider的authority相同
                        (getActivity(), ProviderUtil.getFileProviderName(getActivity()), mGalleryFile);
                Log.e(TAG, "getImgInGallery: "+ ProviderUtil.getFileProviderName(getActivity()) );
                intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri);
                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, 1);     //版本>7.0  图库后返回
            }
            else {
                startActivityForResult(intent, 2);        //版本<7.0  图库后返回
            }
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

                Uri targetUri = FileProvider.getUriForFile    //这里的authority需与AndroidManifest里面的provider的authority相同
                        (getActivity(), ProviderUtil.getFileProviderName(getActivity()), mGalleryFile);
                Log.e(TAG, "getImgInGallery: "+ ProviderUtil.getFileProviderName(getActivity()) );
                intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri);
                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, 3);     //版本>7.0  图库后返回
            }
            else {
                startActivityForResult(intent, 4);        //版本<7.0  图库后返回
            }
        }

    }

    //调用相机获取图片
    private void GetImageInCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //判断android SDK 的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){    //Android 7.0 以上要调用FileProvider获取uri

            Uri targetUri = FileProvider.getUriForFile    //这里的authority需与AndroidManifest里面的provider的authority相同
                    (getActivity(),ProviderUtil.getFileProviderName(getActivity()), mCameraFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri);

            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Log.d(TAG, "getPicFromCamera: targetUri"+targetUri.toString());
        }else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(mCameraFile));
        }
        startActivityForResult(intent,5);
    }

    //设置裁剪方法
    private void CropPhoto(String s, Uri inputUri,int aspectX,int aspectY,int outputX,int outputY) {

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
                String url = GetImagePath.getPath(getContext(),inputUri);
                intent.setDataAndType(Uri.fromFile(new File(url)),"image/*");
            }else {
                intent.setDataAndType(inputUri,"image/*");
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT,outputUri);
        }
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop","true");
        //  aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX",aspectX);
        intent.putExtra("aspectY",aspectY);
        //  outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX",outputX);
        intent.putExtra("outputY",outputY);
        intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
        //图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        if (s.equals("message")){
            startActivityForResult(intent, 6);
        }
        else {
            startActivityForResult(intent, 7);
        }

    }
    //保存图片
    private void  setPhotoSave(Bitmap image) {
        String sdStatus = Environment.getExternalStorageState();
        //检查sd卡是否有用
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            return ;
        }

        File file = new File(back_savepath);
        file.mkdir();
        String fileName = back_savepath +user_id+ "back.jpg";
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);

        switch (requestCode) {

            case 1:               //版本>7.0  图库后返回
                if (resultCode == RESULT_OK) {
                    File  photoUri = new File(GetImagePath.getPath(getContext(),data.getData()));
                    Uri dataUri = FileProvider.getUriForFile     //这里的authority需与AndroidManifest里面的provider的authority相同
                            (getActivity(),ProviderUtil.getFileProviderName(getActivity()), photoUri);
                    CropPhoto("message",dataUri,3,2,240,160);
                } else {
                    Toast.makeText(getActivity(), "更换头像取消", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:         //版本<7.0  图库后返回
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    CropPhoto("message",uri,3,2,240,160);
                }else {
                    Toast.makeText(getActivity(), "更换头像取消", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:               //版本>7.0  图库后返回
                if (resultCode == RESULT_OK) {
                    File  photoUri = new File(GetImagePath.getPath(getContext(),data.getData()));
                    Uri dataUri = FileProvider.getUriForFile     //这里的authority需与AndroidManifest里面的provider的authority相同
                            (getActivity(),ProviderUtil.getFileProviderName(getActivity()), photoUri);
                    CropPhoto("back",dataUri,3,2,240,160);
                } else {
                    Toast.makeText(getActivity(), "更换头像取消", Toast.LENGTH_SHORT).show();
                }
                break;
            case 4:         //版本<7.0  图库后返回
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    CropPhoto("back",uri,3,2,240,160);
                }else {
                    Toast.makeText(getActivity(), "更换头像取消", Toast.LENGTH_SHORT).show();
                }
                break;

            case 5:
                if (resultCode == RESULT_OK){

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {     //此处用相机返回的照片去调用剪裁也需要对Uri进行处理
                        Uri targetUri = FileProvider.getUriForFile
                                (getActivity(), ProviderUtil.getFileProviderName(getActivity()), mCameraFile);
                        CropPhoto("message",targetUri,3,2,240,160);
                    } else {
                        Uri low_uri = Uri.fromFile(mCameraFile);
                        CropPhoto("message",low_uri,3,2,240,160);
                    }
                }else {
                    Toast.makeText(getActivity(), "更换头像取消", Toast.LENGTH_SHORT).show();
                }
                break;

            case 6:
                Uri inputUri = FileProvider.getUriForFile
                        (getActivity(), ProviderUtil.getFileProviderName(getActivity()), mCropFile);
                Bitmap msg_bitmap = null;
                try {
                    msg_bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(inputUri));
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }

                //将图片转化成byteArray传到NewMessageActivity中
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                msg_bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                byte [] b = baos.toByteArray();
                Bundle bundle = new Bundle();
                bundle.putInt("user_id",user_id);
                Intent intent =  new Intent(getActivity(), NewMessageActivity.class);
                intent.putExtra("image",b);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case 7:
                Uri backUri = FileProvider.getUriForFile
                        (getActivity(), ProviderUtil.getFileProviderName(getActivity()), mCropFile);
                Bitmap back_bitmap = null;
                try {
                    back_bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(backUri));
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }
                setPhotoSave(back_bitmap);
                society_back.setImageBitmap(back_bitmap);

                break;
            default:break;
        }

    }
}
