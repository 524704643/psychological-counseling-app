package com.example.tiongradua.graduationproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tiongradua.graduationproject.domain.MyUser;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class UserDetailsActivity extends AppCompatActivity {
    ImageView mIv_photo,mIv_sex;
    LinearLayout mIsSpecialist;
    TextView mTv_name,mTv_profession,mTv_xueli,mTv_introduce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = this.getIntent();
        String userName = intent.getStringExtra("userName2");
        BmobQuery<MyUser> query = new BmobQuery<>();
        query.addWhereEqualTo("username",userName);
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(final List<MyUser> list, BmobException e) {
                if (e ==null){
                    MyUser myUser =  list.get(0);
                    if (myUser.getSpecialist()){
                        mIsSpecialist.setVisibility(View.VISIBLE);
                    }
                    mTv_name.setText(myUser.getName());
                    Bitmap bitmap = BitmapFactory.decodeFile(myUser.getImage().getPath());
                    String sex = myUser.getSex();
                    mIv_photo.setImageBitmap(bitmap);
                    if (sex.equals("男")){
                        mIv_sex.setImageResource(R.drawable.icon_man);
                    }else {
                        mIv_sex.setImageResource(R.drawable.icon_women);
                    }
                    mTv_xueli.setText(myUser.getXueli());
                    mTv_introduce.setText(myUser.getIntroduce());
                    mTv_profession.setText(myUser.getProfession());
                }
            }
        });
    }

    private void initView() {
        Bmob.initialize(this, "b735d6c77e6a925949bbd28bdf771f43"); //bmob初始化
        mIv_photo = (ImageView)findViewById(R.id.ud_headphoto);
        mIsSpecialist = (LinearLayout)findViewById(R.id.isSpecialistLinear);
        mIv_sex = (ImageView)findViewById(R.id.ud_sex);
        mTv_name = (TextView)findViewById(R.id.ud_name);
        mTv_profession = (TextView)findViewById(R.id.ud_profession);
        mTv_xueli = (TextView)findViewById(R.id.ud_xueli);
        mTv_introduce = (TextView)findViewById(R.id.ud_introduce);
    }
}
