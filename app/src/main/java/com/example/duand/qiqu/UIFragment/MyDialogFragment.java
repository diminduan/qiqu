//package com.example.duand.qiqu.UIFragment;
//
//import android.annotation.SuppressLint;
//import android.app.Dialog;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.PorterDuff;
//import android.graphics.PorterDuffXfermode;
//import android.graphics.RectF;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.support.v4.app.DialogFragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.duand.qiqu.R;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//import static android.app.Activity.RESULT_CANCELED;
//import static com.example.duand.qiqu.Constants.path;
//
//
//public class MyDialogFragment extends DialogFragment {
//
//    private Button btn_photo;
//    private Button btn_camera;
//    private Button btn_cancel;
//    private ImageView iv;
//
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        //更换头像dialog
//        final LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.header_choose_dialog, null);
//        final Dialog dialog = new Dialog(getActivity(), R.style.DialogWindowStyle);
//        dialog.setContentView(view, new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        Window window = dialog.getWindow();
//        window.setWindowAnimations(R.style.main_menu_animal_style);    //设置显示动画
//        WindowManager.LayoutParams wl = window.getAttributes();
//        wl.x = 0;
//        wl.y = getActivity().getWindowManager().getDefaultDisplay().getHeight();   //getActivity()要加上，不然无法调用此后方法
//        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        dialog.onWindowAttributesChanged(wl);     //设置显示距离
//
//        dialog.setCanceledOnTouchOutside(false);    //设置点击聚焦以外取消活动
//
//        btn_photo = (Button) window.findViewById(R.id.btn_photo);
//        btn_camera = (Button) window.findViewById(R.id.btn_camera);
//        btn_cancel = (Button) window.findViewById(R.id.btn_cancel);
//
//        //设置相册选择事件
//        btn_photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                //获取本地相册
//                intent.setAction(Intent.ACTION_PICK);
//                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                startActivityForResult(intent, 1);
//
//                dialog.dismiss();
//            }
//        });
//        //设置拍照选择事件
//        btn_camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                //指定调用相机拍照后的照片存储路径
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(
//                        new File(Environment.getExternalStorageDirectory(), "head.jpg")));
////                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                dialog.dismiss();
//                startActivityForResult(intent, 2);
//            }
//        });
//        //设置取消事件
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        return dialog;
//    }
//
//
//    //设置裁剪方法
//    private void photoClip(Uri uri){
//        //调用系统自带的图片裁剪方法
//        Intent intent = new Intent();
//        intent.setAction("com.android.camera.action.CROP");
//        intent.setDataAndType(uri,"image/*");
//        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
//        intent.putExtra("crop","true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX",1);
//        intent.putExtra("aspectY",1);
//        /*outputX outputY 是裁剪图片宽高
//         *这里仅仅是头像展示，不建议将值设置过高
//         * 否则超过binder机制的缓存大小的1M限制
//         * 报TransactionTooLargeException
//         */
//        intent.putExtra("OutputX",120);
//        intent.putExtra("OutputY",120);
//        intent.putExtra("return_data",true);
//        startActivityForResult(intent,3);
//    }
//
//    //设置头像
//    private void setPhotoToView(Bitmap photo){
//        String sdStatus = Environment.getExternalStorageState();
//        //检查sd卡是否有用
//        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)){
//            return;
//        }
//        FileOutputStream fileOutputStream = null;
//        File file = new File(path);
//        file.mkdirs();
//        String fileName = path + "header.jpg";
//        try {
//            fileOutputStream = new FileOutputStream(fileName);
//            photo.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                fileOutputStream.flush();
//                fileOutputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    //将图片转换成圆形
//    public Bitmap toRoundBitmp(Bitmap bitmap){
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//        float roundPx;
//        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
//        if (width <= height) {
//            roundPx = width / 2;
//            top = 0;
//            bottom = width;
//            left = 0;
//            right = width;
//            height = width;
//            dst_left = 0;
//            dst_top = 0;
//            dst_right = width;
//            dst_bottom = width;
//        }else {
//            roundPx = height / 2;
//            float clip = (width - height) / 2;
//            left = clip;
//            right = width - clip;
//            top = 0;
//            bottom = height;
//            width = height;
//            dst_left = 0;
//            dst_top = 0;
//            dst_right = height;
//            dst_bottom = height;
//        }
//        Bitmap round_bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(round_bitmap);
//        final int color = 0xff424242;
//        final Paint paint = new Paint();
//        final android.graphics.Rect src = new android.graphics.Rect((int) left, (int) top, (int) right, (int) bottom);
//        final android.graphics.Rect dst = new android.graphics.Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
//        final RectF rectF = new RectF(dst);
//        paint.setAntiAlias(true);
//        canvas.drawARGB(0, 0, 0, 0);
//        paint.setColor(color);
//        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(bitmap,src,dst,paint);
//        return round_bitmap;
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        switch (requestCode) {
//
//            case 1:
//                if (resultCode == RESULT_CANCELED) {
//                    Toast.makeText(getActivity().getParent(), "更换头像取消", Toast.LENGTH_SHORT).show();
//                    return;
//                }else{
//                    photoClip(data.getData());
//                }break;
//
//            case 2:
//                if (resultCode == RESULT_CANCELED) {
//                    Toast.makeText(getActivity(), "更换头像取消", Toast.LENGTH_SHORT).show();
//                    return;
//                }else {
//                    File temp = new File(Environment.getExternalStorageDirectory()+"/head.jpg");
//                    photoClip(Uri.fromFile(temp));
//                }break;
//
//            case 3:
//                if(data != null){
//                    Bundle extras = data.getExtras();
//                    Bitmap photo = extras.getParcelable("data");
//                    if(photo != null){
//                        setPhotoToView(photo);
//
//                        toRoundBitmp(photo);
//
//                    }
//                }break;
//            default:break;
//        }
//        super.onActivityResult(requestCode,resultCode,data);
//
//    }
//
//
//}
