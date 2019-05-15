package com.example.duand.qiqu.Adapter;

import android.content.Context;
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

public class DiaryAdapter extends BaseAdapter {

    private LinkedList<Diary> mData;
    private Context mcontext;
    private ImageView small_head_iv;
    private TextView name;
    private TextView presentation;
    private ImageView picture;
    private TextView date;

    public DiaryAdapter(LinkedList<Diary> Data,Context context){
        this.mData = Data;
        this.mcontext = context;
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


        small_head_iv.setImageResource(mData.get(position).getUser_head());
        name.setText(mData.get(position).getUser_name());
        presentation.setText(mData.get(position).getPresentation());
        picture.setImageResource(mData.get(position).getPicture());

        //记录当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date time = new Date(System.currentTimeMillis());
        date.setText(simpleDateFormat.format(time));


        return convertView;
    }
}
