package com.example.tiongradua.graduationproject.domain;

import android.provider.ContactsContract;

import java.io.File;
import java.sql.Time;

import cn.bmob.v3.BmobObject;

/**
 * Created by 本人 on 2019/11/13.
 */

public class Comment extends BmobObject {
    //识别那一条话题
    private String public_username;
    private int public_id;
    //识别每一条评论
    private String comment_username;
    private String comment_name;
    private int comment_id;
    private File comment_photo;
    private String comment_sex;
    private Long comment_time;
    private String comment_content;
    private String recontent;
    private String rename;
    private Boolean specialist;

    public Boolean getSpecialist() {
        return specialist;
    }

    public void setSpecialist(Boolean specialist) {
        this.specialist = specialist;
    }

    public String getComment_name() {
        return comment_name;
    }

    public void setComment_name(String comment_name) {
        this.comment_name = comment_name;
    }

    public String getPublic_username() {
        return public_username;
    }

    public void setPublic_username(String public_username) {
        this.public_username = public_username;
    }

    public String getComment_username() {
        return comment_username;
    }

    public void setComment_username(String comment_username) {
        this.comment_username = comment_username;
    }

    public int getPublic_id() {
        return public_id;
    }

    public void setPublic_id(int public_id) {
        this.public_id = public_id;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public File getComment_photo() {
        return comment_photo;
    }

    public void setComment_photo(File comment_photo) {
        this.comment_photo = comment_photo;
    }

    public String getComment_sex() {
        return comment_sex;
    }

    public void setComment_sex(String comment_sex) {
        this.comment_sex = comment_sex;
    }

    public Long getComment_time() {
        return comment_time;
    }

    public void setComment_time(Long comment_time) {
        this.comment_time = comment_time;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getRecontent() {
        return recontent;
    }

    public void setRecontent(String recontent) {
        this.recontent = recontent;
    }

    public String getRename() {
        return rename;
    }

    public void setRename(String rename) {
        this.rename = rename;
    }
}
