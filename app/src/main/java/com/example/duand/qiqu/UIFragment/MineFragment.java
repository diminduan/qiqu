package com.example.duand.qiqu.UIFragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.duand.qiqu.Activity.MineInfoActivity;
import com.example.duand.qiqu.Adapter.DynamicAdapter;
import com.example.duand.qiqu.Adapter.PersonalDynamicAdapter;
import com.example.duand.qiqu.Constants;
import com.example.duand.qiqu.FragmentActivity;
import com.example.duand.qiqu.JavaBean.Dynamic;
import com.example.duand.qiqu.R;
import com.example.duand.qiqu.Utils.GetHttpConnection;
import com.example.duand.qiqu.Utils.GetImagePath;
import com.example.duand.qiqu.Utils.ListViewForScrollView;
import com.example.duand.qiqu.Utils.ProviderUtil;
import com.example.duand.qiqu.Utils.UploadFileUtils;
import com.example.duand.qiqu.Utils.UploadUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import static android.app.Activity.RESULT_OK;
import static com.example.duand.qiqu.Constants.head_savepath;

public class MineFragment extends Fragment implements View.OnClickListener{

    private ImageView header_iv;
    private ImageView menu_iv;
    private Button btn_photo;
    private Button btn_camera;
    private Button btn_cancel;
    private static final String TAG = "MineFragment";
    String path = Environment.getExternalStorageDirectory()+"/Android/data/com.example.duand.qiqu/files/";
    File mCameraFile = new File(path, "Camera_FILE_NAME.jpg");//照相机的File对象
    File mCropFile = new File(path, "Crop_FILE_NAME.jpg");//裁剪后的File对象
    File mGalleryFile = new File(path, "Gallery_File_NAME.jpg");//相册的File对象

    private ImageView bg_iv;
    private List<Dynamic> list;
    private TextView nickname;
    private PersonalDynamicAdapter adapter;
    private ListViewForScrollView list_diary;
    private ScrollView src_view;
    private SwipeRefreshLayout diary_refresh;
    private int user_id;
    private NavigationView nav_view;
    private DrawerLayout mine_drawer;
    private View headerView;
    private ImageView nav_head;
    private TextView nav_name;
    private Bitmap bitmap;
    private TextView nav_moto;
    private TextView tel_num;
    private ImageView gender;
    private TextView address;
    private TextView moto;
    private File photoUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mine_fragment, container, false);

        nickname = (TextView)view.findViewById(R.id.tv_nickname);
        tel_num = (TextView)view.findViewById(R.id.tel_num);
        gender = (ImageView)view.findViewById(R.id.gender);
        address = (TextView)view.findViewById(R.id.address);
        moto = (TextView)view.findViewById(R.id.moto);
        header_iv = (ImageView) view.findViewById(R.id.header_iv);
        menu_iv = (ImageView) view.findViewById(R.id.menu_iv);
        bg_iv = (ImageView)view.findViewById(R.id.bg_iv);

        list_diary = (ListViewForScrollView)view.findViewById(R.id.list_diary);
        diary_refresh = (SwipeRefreshLayout)view.findViewById(R.id.diary_refresh);
        src_view = (ScrollView)view.findViewById(R.id.scr_view);

        mine_drawer = (DrawerLayout)view.findViewById(R.id.mine_drawer);
        nav_view = (NavigationView)view.findViewById(R.id.nav_view);
        headerView = nav_view.getHeaderView(0);
        nav_head = (ImageView)headerView.findViewById(R.id.nav_head);
        nav_name = (TextView)headerView.findViewById(R.id.nav_name);
        nav_moto = (TextView)headerView.findViewById(R.id.nav_moto);


        header_iv.setOnClickListener(this);
        menu_iv.setOnClickListener(this);

        //获取user_id
        Bundle bundle = this.getArguments();
        user_id = bundle.getInt("user_id");
        Log.e("check", "user_id: "+ user_id);

        requestWritePermission();    //请求权限

        //设置头像
        bitmap = BitmapFactory.decodeFile(head_savepath + user_id + "head.jpg");
        Log.e(TAG, "head_savepath :" + head_savepath);
//        Bitmap bitmap1 = null;
//        try (InputStream pathstream = new URL(savepath).openStream()) {
//            bitmap1 = BitmapFactory.decodeStream(pathstream);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (bitmap != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(toRoundBitmp(bitmap));  //将bitmap转换成drawable
            header_iv.setImageDrawable(drawable);
        }

        //展示个人动态
        ShowDiary();

        //获取用户数据
        getData(user_id);

        return view;
    }

    private void getData(int id) {
        String url = Constants.newUrl+"user?"+"id="+ id;

        Handler handler = new Handler(){
            public  void handleMessage(Message msg){
                if (msg.what == 1){
                    String response = msg.obj.toString();
                    Log.e("check", "handleMessage: "+response );
                    try {
                        JSONObject result = new JSONObject(response);
                        nickname.setText(result.getString("userName"));
                        nav_name.setText(result.getString("userName"));
                        tel_num.setText(result.getString("phoneNumber"));
                        nav_moto.setText(result.getString("moto"));
                        moto.setText(result.getString("moto"));
                        address.setText(result.getString("userAddress"));
                        String gender_name = result.getString("gender");
                        Log.e(TAG, "nicknae: "+nickname.getText().toString());
                        if (gender_name.equals("男")){
                            gender.setImageResource(R.drawable.male);
                        }else {
                            gender.setImageResource(R.drawable.female);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread dynamic = new GetHttpConnection(url,handler);
        dynamic.setPriority(Thread.MAX_PRIORITY);   //设置线程高优先级（也可把Thread.MAX_PRIORITY 改成 1~10）
        dynamic.start();

    }

    private void ShowDiary() {

        src_view.smoothScrollTo(0,0);
        list = new LinkedList<Dynamic>();

        String url = Constants.newUrl + "getUserDynamics?"+"userId="+user_id+"&pageNumber=1";

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

                        for (int i=0; i<jsonArray.length();i++){
                            JSONObject dynamic = jsonArray.getJSONObject(i);
                            String content = dynamic.getString("content");
                            int good_count = dynamic.getInt("thumbUpCount");
                            String time = dynamic.getString("createTime");
                            long new_time = Long.parseLong(time);
                            Date date = new Date(new_time);
                            String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                            Log.e(TAG, "nickname2:"+nickname.getText().toString() );
                            list.add(new Dynamic(nickname.getText().toString(),content,R.mipmap.picture_2,dateStr,good_count));
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

        Thread dynamic = new GetHttpConnection(url,handler);
        dynamic.setPriority(Thread.MIN_PRIORITY);   //设置线程低优先级
        dynamic.start();

        adapter = new PersonalDynamicAdapter((LinkedList<Dynamic>) list,getActivity(),user_id);
        list_diary.setAdapter(adapter);

//        list.add(new Diary(nickname.getText().toString(),"骑行，绿色，健康，快乐",R.mipmap.picture_1));
//        list.add(new Diary(nickname.getText().toString(),"Take the time,just enjoying !",R.mipmap.picture_2));
//        adapter = new PersonalDynamicAdapter((LinkedList<Diary>) list,getActivity(),user_id);
//        list_diary.setAdapter(adapter);


//        diary_refresh.setColorSchemeResources(R.color.start,R.color.middle,R.color.end);
//        diary_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        list = new LinkedList<Diary>();
//                        list.add(new Diary(nickname.getText().toString(),"骑行，绿色，健康，快乐",R.mipmap.picture_1));
//                        list.add(new Diary(nickname.getText().toString(),"Take the time,just enjoying !",R.mipmap.picture_2));
//                        adapter = new PersonalDynamicAdapter((LinkedList<Diary>) list,getActivity(),user_id);
//                        list_diary.setAdapter(adapter);
//                        src_view.smoothScrollTo(0,0);
//                        diary_refresh.setRefreshing(false);
//                    }
//                },1000);
//            }
//        });

    }

    //请求权限
    private void requestWritePermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.header_iv:
                showDialogChoose();
                break;

            case R.id.menu_iv:
                popDrawer();
                break;

            default:break;
        }
    }

    //弹出抽屉侧边栏
    private void popDrawer(){
        mine_drawer.openDrawer(Gravity.START);

        //menu点击事件
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Toast.makeText(getActivity(),menuItem.getTitle(), Toast.LENGTH_LONG).show();
                mine_drawer.closeDrawers();
                return false;
            }
        });

        nav_head.setImageBitmap(bitmap);
        //header点击事件
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MineInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("user_id",user_id);
                intent.putExtras(bundle);
                startActivityForResult(intent,5);

            }
        });

   }
    //设置选择事件
    private void showDialogChoose() {

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.header_choose_dialog, null);
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogWindowStyle);
        dialog.setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.main_menu_animal_style);    //设置显示动画
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getActivity().getWindowManager().getDefaultDisplay().getHeight();   //getActivity()要加上，不然无法调用此后方法
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.onWindowAttributesChanged(wl);     //设置显示距离

        dialog.setCanceledOnTouchOutside(true);    //设置点击聚焦以外取消活动
        dialog.show();
        btn_photo = (Button) window.findViewById(R.id.btn_photo);
        btn_camera = (Button) window.findViewById(R.id.btn_camera);
        btn_cancel = (Button) window.findViewById(R.id.btn_cancel);


        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPicFromPhoto();
                dialog.dismiss();
            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPicFromCamera();
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //设置相册选择事件
    private void getPicFromPhoto() {

        //获取本地相册
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

            Uri targetUri = FileProvider.getUriForFile    //这里的authority需与AndroidManifest里面的provider的authority相同
                    (getActivity(), ProviderUtil.getFileProviderName(getActivity()), mGalleryFile);
            Log.e(TAG, "getPicFromPhoto: "+ ProviderUtil.getFileProviderName(getActivity()) );
            intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri);
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, 1);     //版本>7.0  图库后返回
        }
        else {
            startActivityForResult(intent, 2);        //版本<7.0  图库后返回
        }

    }

    //设置拍照选择事件
    private void getPicFromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //判断android SDK 的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){    //Android 7.0 以上要调用FileProvider获取uri


            Uri targetUri = FileProvider.getUriForFile    //这里的authority需与AndroidManifest里面的provider的authority相同
                    (getActivity(),ProviderUtil.getFileProviderName(getActivity()), mCameraFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri);

            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Log.e(TAG, "getPicFromCamera: targetUri"+targetUri.toString());
        }else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(mCameraFile));
        }
        startActivityForResult(intent,3);
    }


    //保存头像
    private void  setPhotoSave(Bitmap image) {
        String sdStatus = Environment.getExternalStorageState();
        //检查sd卡是否有用
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            return ;
        }

        File file = new File(head_savepath);
        file.mkdir();
        String fileName = head_savepath +user_id+ "head.jpg";
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
                String url = GetImagePath.getPath(getContext(),inputUri);
                intent.setDataAndType(Uri.fromFile(new File(url)),"image/*");
            }else {
                intent.setDataAndType(inputUri,"image/*");
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT,outputUri);
            Log.e(TAG, "Photofilename: " + outputUri );
        }
        //设置裁剪

        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop","true");
        //  aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        //  outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX",160);
        intent.putExtra("outputY",160);
        intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
        //图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, 4);
    }

    //将图片转换成圆形
        private Bitmap toRoundBitmp(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        }else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap round_bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(round_bitmap);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final android.graphics.Rect src = new android.graphics.Rect((int) left, (int) top, (int) right, (int) bottom);
        final android.graphics.Rect dst = new android.graphics.Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,src,dst,paint);
        return round_bitmap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);

        switch (requestCode) {

            case 1:               //版本>7.0  图库后返回
                if (resultCode == RESULT_OK) {
                    photoUri = new File(GetImagePath.getPath(getContext(),data.getData()));
                   Uri dataUri = FileProvider.getUriForFile     //这里的authority需与AndroidManifest里面的provider的authority相同
                           (getActivity(),ProviderUtil.getFileProviderName(getActivity()), photoUri);
                    CropPhoto(dataUri);
                    Log.e(TAG, "onActivityResult_photo: "+ photoUri);
                    Log.e(TAG, "onActivityResult_photo_path: "+ photoUri.getPath());
                    Log.e(TAG, "onActivityResult_photo_str: "+ photoUri.toString());
                } else {
                    Toast.makeText(getActivity(), "更换头像取消", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:         //版本<7.0  图库后返回
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    CropPhoto(uri);
                }else {
                    Toast.makeText(getActivity(), "更换头像取消", Toast.LENGTH_SHORT).show();
                }
                break;

            case 3:
                if (resultCode == RESULT_OK){

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {     //此处用相机返回的照片去调用剪裁也需要对Uri进行处理
                        Uri targetUri = FileProvider.getUriForFile
                                (getActivity(), ProviderUtil.getFileProviderName(getActivity()), mCameraFile);
                        CropPhoto(targetUri);
                    } else {
                        Uri low_uri = Uri.fromFile(mCameraFile);
                        CropPhoto(low_uri);
                    }
                }else {
                    Toast.makeText(getActivity(), "更换头像取消", Toast.LENGTH_SHORT).show();
                }
                break;

            case 4:
                Uri inputUri = FileProvider.getUriForFile
                        (getActivity(), ProviderUtil.getFileProviderName(getActivity()), mCropFile);
                Bitmap bitmap = null;
                try {
                     bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(inputUri));
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }
                setPhotoSave(bitmap);
                header_iv.setImageBitmap(toRoundBitmp(bitmap));

                //上传头像
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = Constants.newUrl +"uploadPic?"+"userId="+user_id;
//                        UploadFileUtils.uploadFile(photoUri.getPath(),url);
                        UploadUtil.uploadFile(photoUri,url);
                    }
                }).start();

                ShowDiary();

                break;
            case 5:
                if (resultCode == 2){
                    FragmentActivity activity = (FragmentActivity)getActivity();
                    activity.BackFragment();
                }
            default:break;
        }

    }

}
