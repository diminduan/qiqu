package com.example.duand.qiqu.JavaBean;

import java.util.Date;

public class Dynamic {



    private int user_head;
    private String user_name;
    private String presentation;
    private int picture;
    private String date;
    private int good_count;


    public Dynamic(int user_id,String user_name,String presentation,int picture){

        this.user_name = user_name;
        this.presentation = presentation;
        this.picture = picture;
    }



    public Dynamic(String user_name,String presentation,int picture,String date,int good_count){
        this.user_name = user_name;
        this.presentation = presentation;
        this.picture = picture;
        this.date = date;
        this.good_count = good_count;
    }
    public int getUser_head() {
        return user_head;
    }

    public void setUser_head(int user_head) {
        this.user_head = user_head;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getGood_count() {
        return good_count;
    }

    public void setGood_count(int good_count) {
        this.good_count = good_count;
    }

}
