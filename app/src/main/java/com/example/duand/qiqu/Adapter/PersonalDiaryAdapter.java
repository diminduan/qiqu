package com.example.duand.qiqu.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duand.qiqu.JavaBean.Diary;
import com.example.duand.qiqu.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import static com.example.duand.qiqu.Constants.head_savepath;

public class PersonalDiaryAdapter extends BaseAdapter {

    private LinkedList<Diary> mData;
    private Context mcontext;
    private ImageView small_head_iv;
    private TextView name;
    private TextView presentation;
    private ImageView picture;
    private TextView date;
    int muser_id;

    public PersonalDiaryAdapter(LinkedList<Diary> Data, Context context,int user_id){
        this.mData = Data;
        this.mcontext = context;
        this.muser_id = user_id;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mcontext).inflate(R.layout.dairy_list_item,parent,false);
        small_head_iv = (ImageView)convertView.findViewById(R.id.small_head_iv);
        name = (TextView)convertView.findViewById(R.id.name);
        presentation = (TextView)convertView.findViewById(R.id.presentation);
        picture = (ImageView)convertView.findViewById(R.id.picture);
        date = (TextView)convertView.findViewById(R.id.nowtime);

        Bitmap bitmap = BitmapFactory.decodeFile(head_savepath +muser_id+ "head.jpg");
        if (bitmap != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bitmap);  //将bitmap转换成drawable
            small_head_iv.setImageDrawable(drawable);
        }

        //记录当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date time = new Date(System.currentTimeMillis());
        date.setText(simpleDateFormat.format(time));

        presentation.setText(mData.get(position).getPresentation());
        picture.setImageResource(mData.get(position).getPicture());
        name.setText(mData.get(position).getUser_name());
        return convertView;
    }
}
