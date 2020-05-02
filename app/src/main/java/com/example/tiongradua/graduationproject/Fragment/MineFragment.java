package com.example.tiongradua.graduationproject.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiongradua.graduationproject.BecomeSpecialActivity;
import com.example.tiongradua.graduationproject.Comon.LoginCollection;
import com.example.tiongradua.graduationproject.InformationActivity;
import com.example.tiongradua.graduationproject.LoginActivity;
import com.example.tiongradua.graduationproject.MainActivity;
import com.example.tiongradua.graduationproject.R;
import com.example.tiongradua.graduationproject.SettingActivity;
import com.example.tiongradua.graduationproject.domain.MyUser;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class MineFragment extends Fragment implements View.OnClickListener {
    ImageView mIv_headPhoto,mIv_sex,mIv_comingInfo;
    TextView mMine_login_register;
    LinearLayout mLinear_setting,mIsSpecialistLinear;
    RelativeLayout mLinear_becomeSpecial;
    @Override
    public View onCreateView(LayoutInflater mInflater, ViewGroup container,Bundle savedInstanceState) {
        View mineView =mInflater.inflate(R.layout.mine_fragment,container,false);
        initview(mineView);
        getReturnLoginValue();
        return mineView;

    }

    private void getReturnLoginValue() {
        final BmobUser user = BmobUser.getCurrentUser(BmobUser.class);
        if (user != null){
                BmobQuery<MyUser> query = new BmobQuery<>();
                query.addWhereEqualTo("username",user.getUsername());
                query.findObjects(new FindListener<MyUser>() {
                    @Override
                    public void done(final List<MyUser> list, BmobException e) {
                        if (e ==null){
                                    MyUser myUser =  list.get(0);
                                    if (myUser.getSpecialist()){
                                        mIsSpecialistLinear.setVisibility(View.VISIBLE);
                                    }
                                    mMine_login_register.setText(myUser.getName());
                                    // mMine_login_register.setBackgroundColor(Color.argb(0,0,0,0));
                                    Bitmap bitmap = BitmapFactory.decodeFile(myUser.getImage().getPath());
                                    String sex = myUser.getSex();
                                    mIv_headPhoto.setImageBitmap(bitmap);
                                    if (sex.equals("男")){
                                        mIv_sex.setImageResource(R.drawable.icon_man);
                                    }else {
                                        mIv_sex.setImageResource(R.drawable.icon_women);
                                    }

                        }
                    }
                });
        }
    }


    private void initview(View v) {
        Bmob.initialize(getActivity(), "b735d6c77e6a925949bbd28bdf771f43"); //bmob初始化
        mIv_sex = (ImageView)v.findViewById(R.id.mine_sex);
        mIv_comingInfo = (ImageView)v.findViewById(R.id.mine_comingInfo);
        mIv_comingInfo.setOnClickListener(this);
        mIv_headPhoto = (ImageView)v.findViewById(R.id.mine_headphoto);
        mIv_headPhoto.setOnClickListener(this);
        mMine_login_register = (TextView)v.findViewById(R.id.mine_login_register);
        mMine_login_register.setOnClickListener(this);
        mLinear_setting = (LinearLayout)v.findViewById(R.id.Linear_setting);
        mLinear_setting.setOnClickListener(this);
        mLinear_becomeSpecial = (RelativeLayout) v.findViewById(R.id.becomeSpecialLayout);
        mLinear_becomeSpecial.setOnClickListener(this);
        mIsSpecialistLinear = (LinearLayout)v.findViewById(R.id.isSpecialistLinear);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mine_headphoto){
            LoginCollection collection = new LoginCollection();
            Boolean islogin = collection.islogin();
            if (islogin){
                Intent in = new Intent(getActivity(), InformationActivity.class);
                startActivity(in);
            }else {
                Intent in = new Intent(getActivity(), LoginActivity.class);
                startActivity(in);
            }

        }
        if (v.getId() == R.id.Linear_setting){

            LoginCollection collection = new LoginCollection();
            Boolean islogin = collection.islogin();
            if (islogin){
                Intent intent = new Intent(getActivity(),SettingActivity.class);
                startActivity(intent);
            }else {
                Intent in = new Intent(getActivity(), LoginActivity.class);
                startActivity(in);
            }
        }
        if (v.getId() == R.id.mine_comingInfo){
            Intent in = new Intent(getActivity(), InformationActivity.class);
            startActivity(in);
        }

        if (v.getId() == R.id.becomeSpecialLayout){
            LoginCollection collection = new LoginCollection();
            Boolean islogin = collection.islogin();
            if (islogin){
                Intent in = new Intent(getActivity(), BecomeSpecialActivity.class);
                startActivity(in);
            }else {
                Intent in = new Intent(getActivity(), LoginActivity.class);
                startActivity(in);
            }
        }
    }
}
