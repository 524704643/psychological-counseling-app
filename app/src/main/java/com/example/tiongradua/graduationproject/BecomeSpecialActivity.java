package com.example.tiongradua.graduationproject;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tiongradua.graduationproject.domain.MyUser;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class BecomeSpecialActivity extends AppCompatActivity implements View.OnClickListener {
    Button mSpecialist_btn;
    EditText mEt_name,mEt_phone,mEt_credentials;
    Spinner mSpinner_type1,mSpinner_type2,mSpinner_type3,mSpinner_xueli;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter2;
    private String tempValue1,tempValue2,tempValue3,tempValue4;
    ImageView mSpecialistBack;
    private String [] types = {"请选择","情绪压力","学习压力","亲子教育","恋爱咨询","婚姻咨询","职业压力","个人成长","人际交往"};
    private String [] xuelis = {"请选择","大专","本科","硕士","博士"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_special);
        initView();
    }

    private void initView() {
        Bmob.initialize(this, "b735d6c77e6a925949bbd28bdf771f43"); //bmob初始化
        mSpecialistBack = (ImageView)findViewById(R.id.specialist_back);
        mSpecialistBack.setOnClickListener(this);
        mEt_name = (EditText)findViewById(R.id.Et_spe_name);
        mEt_phone = (EditText)findViewById(R.id.Et_spe_phone);
        mEt_credentials = (EditText)findViewById(R.id.Et_spe_credentials);
        mSpinner_xueli = (Spinner) findViewById(R.id.S_spe_xueli);
        mSpinner_type1 = (Spinner)findViewById(R.id.S_spe_type1);
        mSpinner_type2 = (Spinner)findViewById(R.id.S_spe_type2);
        mSpinner_type3 = (Spinner)findViewById(R.id.S_spe_type3);
        adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,xuelis);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner_xueli.setAdapter(adapter2);
        mSpinner_xueli.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tempValue4 =adapter2.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner_type1.setAdapter(adapter);
        mSpinner_type2.setAdapter(adapter);
        mSpinner_type3.setAdapter(adapter);
        mSpinner_type1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tempValue1 = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinner_type2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tempValue2 = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       mSpinner_type3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               tempValue3 = adapter.getItem(position);
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
        mSpecialist_btn = (Button)findViewById(R.id.special_btn);
        mSpecialist_btn.setOnClickListener(this);
    }
    private void toast(String s) {
        Toast toast =Toast.makeText(this,s,Toast.LENGTH_SHORT);
        toast.setText(s);
        toast.show();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.specialist_back){
            finish();
        }
        if (v.getId() == R.id.special_btn){
            //Toast.makeText(this,tempValue1+tempValue2+tempValue4+"",Toast.LENGTH_SHORT).show();
            final String  name = mEt_name.getText().toString();
            final String phone = mEt_phone.getText().toString();
            final String credentials = mEt_credentials.getText().toString();
            if (name.equals("")){
                toast("姓名不能为空");
            }
            else if (phone.equals("")){
                toast("联系方式不能为空");
            }
            else if (phone.length()!=11){
                toast("联系方式为11位");
            }
            else if (credentials.equals("")){
                toast("身份证号不能为空");
            }
            else if (credentials.length()!=18){
                toast("请输入正确的身份证号");
            }else {
                final BmobUser user = BmobUser.getCurrentUser(BmobUser.class);
                BmobQuery<MyUser> query = new BmobQuery<>();
                query.addWhereEqualTo("username",user.getUsername());
                query.findObjects(new FindListener<MyUser>() {
                    @Override
                    public void done(final List<MyUser> list, BmobException e) {
                        if (e ==null){
                            MyUser myuser =  list.get(0);
                            myuser.setName(name);
                            myuser.setPhone(phone);
                            myuser.setCredentials(credentials);
                            myuser.setSpecialist(true);
                            if (tempValue4.equals("请选择")){
                                myuser.setXueli("");
                            }else {
                                myuser.setXueli(tempValue4);
                            }

                            if (tempValue1.equals("请选择")){
                                myuser.setType1("");
                            }else {
                                myuser.setType1(tempValue1);
                            }

                            if (tempValue2.equals("请选择")){
                                myuser.setType2("");
                            }else {
                                myuser.setType2(tempValue2);
                            }

                            if (tempValue3.equals("请选择")){
                                myuser.setType3("");
                            }else {
                                myuser.setType3(tempValue3);
                            }
                            myuser.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e ==null){
                                        toast("入驻成功");
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(BecomeSpecialActivity.this,MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                                                intent.putExtra("flag",1);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }).start();
                                    }
                                }
                            });

                        }
                    }
                });




            }

        }
    }

}
