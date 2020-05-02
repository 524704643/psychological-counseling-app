package com.example.tiongradua.graduationproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiongradua.graduationproject.Adapter.ChatAdapter;
import com.example.tiongradua.graduationproject.Adapter.CommentAdapter;
import com.example.tiongradua.graduationproject.domain.Chat;
import com.example.tiongradua.graduationproject.domain.MyUser;
import com.example.tiongradua.graduationproject.domain.Topic;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout mChatBackLayout;
    TextView mTv_name;
    ListView mChat_listview;
    EditText mChat_content;
    Button mBtn_send;
    MyUser rUser;
    ChatAdapter adapter;
    List<Chat> chatList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        initData();
        adapter = new ChatAdapter(this,chatList);
        mChat_listview.setAdapter(adapter);
    }

    private void initData() {
        final BmobUser user = BmobUser.getCurrentUser(BmobUser.class);
        Intent intent = this.getIntent();
         rUser=(MyUser)intent.getSerializableExtra("rUser");
         mTv_name.setText(rUser.getName());
        new Thread(new Runnable() {
            @Override
            public void run() {
                String bql ="select * from Chat where (susername = '"+user.getUsername()+"' and rusername = '"+rUser.getUsername()+"') or (susername = '"+rUser.getUsername()+"' and rusername = '"+user.getUsername()+"')";
                new BmobQuery<Chat>().doSQLQuery(bql,new SQLQueryListener<Chat>(){
                    @Override
                    public void done(BmobQueryResult<Chat> result, BmobException e) {
                        if(e ==null){
                            Chat chat = null;
                            List<Chat> list = (List<Chat>) result.getResults();
                            if(list!=null && list.size()>0){
                                for (int i=0;i<list.size();i++){
                                    chat = list.get(i);
                                    chatList.add(chat);
                                }
                            }else{
                                Log.i("smile", "查询成功，无数据返回");
                            }
                            adapter.notifyDataSetChanged();
                        }else{
                            Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                        }

                    }
                });
            }
        }).start();

    }

    private void initView() {
        Bmob.initialize(this, "b735d6c77e6a925949bbd28bdf771f43"); //bmob初始化
        mChatBackLayout = (LinearLayout)findViewById(R.id.chatBackLayout);
        mChatBackLayout.setOnClickListener(this);
        mTv_name = (TextView)findViewById(R.id.chat_name);
        mChat_listview = (ListView)findViewById(R.id.chat_listview);
        chatList = new ArrayList<>();
        mChat_content = (EditText)findViewById(R.id.chat_content);
        mBtn_send = (Button)findViewById(R.id.chat_send);
        mBtn_send.setOnClickListener(this);
    }
    private void toast(String s) {
        Toast toast =Toast.makeText(this,s,Toast.LENGTH_SHORT);
        toast.setText(s);
        toast.show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chatBackLayout:
                finish();
                break;
            case R.id.chat_send:
                final BmobUser user = BmobUser.getCurrentUser(BmobUser.class);
                final String contnet = mChat_content.getText().toString();
                Log.d("succeff",contnet);
                if (!contnet.equals("")){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final Chat chat = new Chat();
                            BmobQuery<MyUser> bmobQuery = new BmobQuery<>();
                            bmobQuery.addWhereEqualTo("username",user.getUsername());
                            bmobQuery.findObjects(new FindListener<MyUser>() {
                                @Override
                                public void done(final List<MyUser> list, BmobException e) {
                                    if (e == null){
                                        chat.setSusername(user.getUsername());
                                        chat.setSimage(list.get(0).getImage());
                                        chat.setContent(contnet);
                                        chat.setRusername(rUser.getUsername());
                                        chat.setRimage(rUser.getImage());
                                        chat.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e == null){
                                                    Log.d("succeff",s);
                                                    mChat_content.setText("");
                                                    chatList.add(chat);
                                                    adapter.notifyDataSetChanged();
                                                    mChat_listview.setSelection(adapter.getCount()-1);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }).start();

                }else {
                    toast("不能发空白");
                }
                break;
            default:
                break;
        }
    }
}
