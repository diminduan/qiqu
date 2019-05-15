package com.example.duand.qiqu.JavaBean;

public class Diary {



    private int user_head;
    private String user_name;
    private String presentation;
    private int picture;
    private String date;


    public Diary(int user_head,String user_name,String presentation,int picture){
        this.user_head = user_head;
        this.user_name = user_name;
        this.presentation = presentation;
        this.picture = picture;
    }

    public Diary(String user_name,String presentation,int picture){
        this.user_name = user_name;
        this.presentation = presentation;
        this.picture = picture;
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

}
