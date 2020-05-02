package com.example.tiongradua.graduationproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tiongradua.graduationproject.domain.MyUser;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class InformationActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView mInfo_photo;
    LinearLayout mInfo_back;
    TextView mInfo_name,mInfo_sex,mInfo_profession,mInfo_xueli;
    RelativeLayout mXueliLinear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        initView();
        getInfoValue();
    }
    private void getInfoValue() {
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
                                    mXueliLinear.setVisibility(View.VISIBLE);
                                    mInfo_xueli.setText(myUser.getXueli());
                                }
                                mInfo_photo.setImageBitmap(BitmapFactory.decodeFile(myUser.getImage().getPath()));
                                mInfo_name.setText(myUser.getName());
                                mInfo_sex.setText(myUser.getSex());
                                mInfo_profession.setText(myUser.getProfession());

                    }
                }
            });
        }
    }
    private void initView() {
        Bmob.initialize(this, "b735d6c77e6a925949bbd28bdf771f43"); //bmob初始化
        mInfo_photo = (ImageView)findViewById(R.id.info_photo);
        mInfo_back = (LinearLayout) findViewById(R.id.info_back);
        mInfo_back.setOnClickListener(this);
        mInfo_name = (TextView)findViewById(R.id.info_name);
        mInfo_sex = (TextView)findViewById(R.id.info_sex);
        mInfo_profession = (TextView)findViewById(R.id.info_profession);
        mInfo_xueli = (TextView)findViewById(R.id.info_xueli);
        mXueliLinear = (RelativeLayout)findViewById(R.id.xueliLinear);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.info_back:
                this.finish();
                break;
            default:
                break;
        }
    }
}
