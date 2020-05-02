package com.example.tiongradua.graduationproject.Comon;

import android.graphics.Color;

import com.example.tiongradua.graduationproject.domain.MyUser;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 本人 on 2019/11/3.
 */

public class LoginCollection {
    public Boolean islogin(){
        BmobUser user = BmobUser.getCurrentUser(BmobUser.class);
        if (user != null){
            return true;
        }else {
            return false;
        }
    }
}
