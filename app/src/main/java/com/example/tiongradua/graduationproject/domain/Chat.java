package com.example.tiongradua.graduationproject.domain;

import java.io.File;

import cn.bmob.v3.BmobObject;

/**
 * Created by 本人 on 2019/11/25.
 */

public class Chat extends BmobObject {
    private String susername;//发送者用户
    private String rusername;//接收者用户
    private String content;//聊天信息
    private File simage;//发送者用户是图片
    private File rimage;//接收者用户图片

    public String getSusername() {
        return susername;
    }

    public void setSusername(String susername) {
        this.susername = susername;
    }

    public String getRusername() {
        return rusername;
    }

    public void setRusername(String rusername) {
        this.rusername = rusername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public File getSimage() {
        return simage;
    }

    public void setSimage(File simage) {
        this.simage = simage;
    }

    public File getRimage() {
        return rimage;
    }

    public void setRimage(File rimage) {
        this.rimage = rimage;
    }
}
