package com.example.tiongradua.graduationproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiongradua.graduationproject.domain.MyUser;
import com.example.tiongradua.graduationproject.domain.Topic;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;

public class InsertDynamicActivity extends AppCompatActivity implements View.OnClickListener {
    TextView mTv_publish;
    EditText mEt_title,mEt_content;
    private int identicalUserCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_dynamic);
        initView();
    }

    private void initView() {
        Bmob.initialize(this, "b735d6c77e6a925949bbd28bdf771f43"); //bmob初始化
        mTv_publish = (TextView)findViewById(R.id.insert_publish);
        mTv_publish.setOnClickListener(this);
        mEt_title = (EditText)findViewById(R.id.insert_title);
        mEt_content = (EditText)findViewById(R.id.insert_content);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() ==R.id.insert_publish){
            final String title = mEt_title.getText().toString();
            final String content = mEt_content.getText().toString();
            final BmobUser user = BmobUser.getCurrentUser(BmobUser.class);
            final Topic topic = new Topic();
            if (user != null){
                    String bql ="select * from Topic where username = '"+user.getUsername()+"'";
                    new BmobQuery<Topic>().doSQLQuery(bql,new SQLQueryListener<Topic>(){

                        @Override
                        public void done(BmobQueryResult<Topic> result, BmobException e) {
                            if(e ==null){
                                List<Topic> list = (List<Topic>) result.getResults();
                                if(list!=null && list.size()>=0){
                                   identicalUserCount = list.size();
                                    BmobQuery<MyUser> query = new BmobQuery<>();
                                    query.addWhereEqualTo("username",user.getUsername());
                                    query.findObjects(new FindListener<MyUser>() {
                                        @Override
                                        public void done(final List<MyUser> list, BmobException e) {
                                            if (e ==null){
                                                MyUser myUser =  list.get(0);
                                                topic.setImage(myUser.getImage());
                                                topic.setUsername(myUser.getUsername());
                                                topic.setName(myUser.getName());
                                                topic.setTitle(title);
                                                topic.setContent(content);
                                                topic.setReadcount(1);
                                                topic.setId(identicalUserCount+1);
                                                topic.setCommentcount(0);
                                                topic.save(new SaveListener<String>() {
                                                    @Override
                                                    public void done(String s, BmobException e) {
                                                        if (e == null){
                                                            Toast.makeText(InsertDynamicActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
                                                            finish();
                                                            new Thread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Intent intent = new Intent(InsertDynamicActivity.this,MainActivity.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                                                                    intent.putExtra("flag",2);
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
                                }else{
                                    Log.i("smile", "查询成功，无数据返回");
                                }
                            }else{
                                Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                            }
                        }
                    });


                    //Toast.makeText(InsertDynamicActivity.this,topicList.size(),Toast.LENGTH_SHORT).show();

            }
        }
    }
}
