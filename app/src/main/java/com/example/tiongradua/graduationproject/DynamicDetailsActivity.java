package com.example.tiongradua.graduationproject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiongradua.graduationproject.Adapter.CommentAdapter;
import com.example.tiongradua.graduationproject.Comon.WeiboDialogUtils;
import com.example.tiongradua.graduationproject.domain.Comment;
import com.example.tiongradua.graduationproject.domain.MyUser;
import com.example.tiongradua.graduationproject.domain.Topic;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
import cn.bmob.v3.listener.UpdateListener;

public class DynamicDetailsActivity extends AppCompatActivity implements View.OnClickListener ,CommentAdapter.Callback{
    private ImageView mIv_back;
    private  EditText mEt_comment;
    private Button mBtn_send;
    private List<Comment> comments ;
    private CommentAdapter adapter;
    private ListView mComment_listview;
    public List<Topic> datas ;
    private Context mContext;
    private List<String> tempDatas = new ArrayList<>();
    InputMethodManager imm;
    private Dialog mWeiboDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_details);
        initView();
        initData();

    }

    private void show_Reply(String hint){
        mEt_comment.setHint(hint);
        mEt_comment.setFocusable(true);
        mEt_comment.requestFocus();
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
    }



    private void initData() {
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(DynamicDetailsActivity.this, "加载中...");
        mHandler.sendEmptyMessageDelayed(1, 1500);
        datas.clear();
        Intent intent = this.getIntent();
        final int id = intent.getIntExtra("Id",0);
        final String userName = intent.getStringExtra("userName");
//        datas.add(String.valueOf(id));
//        datas.add(userName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String bql ="select * from Topic where username = '"+userName+"' and id = "+id+"";
                new BmobQuery<Topic>().doSQLQuery(bql,new SQLQueryListener<Topic>(){
                    @Override
                    public void done(BmobQueryResult<Topic> result, BmobException e) {
                        if(e ==null){
                            List<Topic> list = (List<Topic>) result.getResults();
                            if(list!=null && list.size()>0){
                                Topic topic =  list.get(0);
                                datas.add(topic);
                                adapter = new CommentAdapter(DynamicDetailsActivity.this,comments,datas,DynamicDetailsActivity.this);
                                mComment_listview.setAdapter(adapter);
                            }else{
                                Log.i("smile", "查询成功，无数据返回");
                            }
                        }else{
                            Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                        }
                    }
                });
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                query("reflash");
            }
        }).start();

        tempDatas.add("");
        tempDatas.add("");

    }
    private void toast(String s) {
        Toast toast =Toast.makeText(this,s,Toast.LENGTH_SHORT);
        toast.setText(s);
        toast.show();
    }
    private void initView() {
        Bmob.initialize(this, "b735d6c77e6a925949bbd28bdf771f43"); //bmob初始化
        mIv_back = (ImageView)findViewById(R.id.details_back);
        mBtn_send = (Button)findViewById(R.id.details_send);
        mBtn_send.setOnClickListener(this);
        mEt_comment = (EditText) findViewById(R.id.detail_Et);
        mEt_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    if (imm.isActive()){
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
                    }
                    mEt_comment.setHint("发表评论");
                    return false;
                }
                return true;
            }
        });
        mIv_back.setOnClickListener(this);
        mComment_listview = (ListView)findViewById(R.id.details_listview);
        datas = new ArrayList<Topic>();
        comments = new ArrayList<Comment>();
        RefreshLayout refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                comments.clear();
                refreshlayout.finishRefresh(1000);
                toast("已经是最新数据");
                query("reflash");
                adapter.notifyDataSetChanged();
            }
        });
            refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
                @Override
                public void onLoadmore(RefreshLayout refreshlayout) {
                    refreshlayout.finishLoadmore(1000);
                    query("loadmore");
                    adapter.notifyDataSetChanged();
                }
            });


        //设置上下拉刷新主题
        refreshLayout.setRefreshHeader(new MaterialHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        Bmob.initialize(this, "b735d6c77e6a925949bbd28bdf771f43"); //bmob初始化
        mComment_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tempDatas.clear();
                Comment comm = comments.get(position-1);
                show_Reply("回复:"+comm.getComment_name());
                tempDatas.add(comm.getComment_name());
                tempDatas.add(comm.getComment_content());
            }
        });
        mComment_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                mEt_comment.setHint("发表评论");
                //mEt_comment.setFocusable(false);
                    tempDatas.clear();
                tempDatas.add("");
                tempDatas.add("");

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }
    private void query(final String isRin) {
        Intent intent = this.getIntent();
        final int id = intent.getIntExtra("Id",0);
        final String userName = intent.getStringExtra("userName");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String bql ="select * from Comment where public_username = '"+userName+"' and public_id = "+id+"";
                new BmobQuery<Comment>().doSQLQuery(bql,new SQLQueryListener<Comment>(){
                    @Override
                    public void done(BmobQueryResult<Comment> result, BmobException e) {
                        if(e ==null){
                            List<Comment> topiclist = (List<Comment>) result.getResults();
                                    if(topiclist!=null && topiclist.size()>0){
                                        Comment comment = null;
                                        int listCount = topiclist.size();
                                        int inNum = 20;
                                        if (e == null) {

                                            if (isRin == "reflash"){
                                                if (listCount > inNum){
                                                    for (int i=listCount-1;i>=listCount-inNum;i--){
                                                        comment = topiclist.get(i);
                                                        comments.add(comment);

                                                    }
                                                }else {
                                                    for (int i=listCount-1;i>=0;i--){
                                                        comment = topiclist.get(i);
                                                        comments.add(comment);
                                                    }
                                                }
                                            }
                                    else{
                                        int currentCount= adapter.getCount()-1;//获取当前listview item数
                                        if (currentCount == listCount){
                                        }
                                        else if (currentCount + inNum <listCount){
                                            for (int i=listCount-currentCount-1;i<listCount-currentCount-inNum;i--){
                                                comment = topiclist.get(i);
                                                comments.add(comment);
                                            }

                                        }else {
                                            for (int i=listCount-currentCount-1;i>=0;i--){
                                                comment = topiclist.get(i);
                                                comments.add(comment);
                                            }
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Log.e("BMOB", e.toString());
                                }

                            }else{
                                Log.i("smile11", "查询成功，无数据返回");
                            }
                        }else{
                            Log.i("smile11", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                        }
                    }
                });
            }
        }).start();

    }
    private void refresh() {
        onCreate(null);
    }

    private void updateCommentCountData(){
        Intent intent = this.getIntent();
        final int id = intent.getIntExtra("Id",0);
        final String userName = intent.getStringExtra("userName");
                String bql ="select * from Topic where username = '"+userName+"' and id = "+id+"";
                new BmobQuery<Topic>().doSQLQuery(bql,new SQLQueryListener<Topic>(){
                    @Override
                    public void done(BmobQueryResult<Topic> result, BmobException e) {
                        if(e ==null){
                            List<Topic> topiclist = (List<Topic>) result.getResults();
                            if(topiclist!=null && topiclist.size()>0){
                                Topic topic = null;
                                if (e == null) {
                                    topic = topiclist.get(0);
                                    topic.setCommentcount(topic.getCommentcount()+1);
                                    topic.update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null){
                                                refresh();
                                                adapter.notifyDataSetChanged();
                                            }else {
                                                Log.e("BMOB", e.toString());
                                            }
                                        }
                                    });
                                } else {
                                    Log.e("BMOB", e.toString());
                                }

                            }else{
                                Log.i("smile22", "查询成功，无数据返回");
                            }
                        }else{
                            Log.i("smile22", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                        }
                    }
                });

    }
    public void onClick(View v) {
        if (v.getId() == R.id.details_back){
            finish();
        }
        if (v.getId() == R.id.details_send){
            Intent intent = this.getIntent();
            final int topic_id = intent.getIntExtra("Id",0);//1
            final String username = intent.getStringExtra("userName");//2
            final String comment_content = mEt_comment.getText().toString();//3
            final Comment comment = new Comment();
            final BmobUser user = BmobUser.getCurrentUser(BmobUser.class);
            if (user != null){
                if (!comment_content.equals("")){
                    BmobQuery<MyUser> query = new BmobQuery<>();
                    query.addWhereEqualTo("username",user.getUsername());
                    query.findObjects(new FindListener<MyUser>() {
                        @Override
                        public void done(final List<MyUser> list, BmobException e) {
                            if (e ==null){
                                MyUser myUser =  list.get(0);
                                String sex = myUser.getSex();//4
                                String name = myUser.getName();//6
                                Boolean isSpecialist = myUser.getSpecialist();
                                comment.setSpecialist(isSpecialist);
                                comment.setPublic_id(topic_id);
                                comment.setPublic_username(username);
                                comment.setComment_username(user.getUsername());
                                comment.setComment_id(1);
                                comment.setComment_name(myUser.getName());
                                comment.setComment_photo(myUser.getImage());
                                comment.setComment_sex(sex);
                                comment.setComment_content(comment_content);
                                comment.setRename(tempDatas.get(0));
                                comment.setRecontent(tempDatas.get(1));
                                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(DynamicDetailsActivity.this, "加载中...");
                                mHandler.sendEmptyMessageDelayed(1, 1200);
                                comment.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null){
                                            updateCommentCountData();
                                            comments.add(0,comment);
                                            adapter.notifyDataSetChanged();
                                            mEt_comment.setText("");
                                            mEt_comment.setHint("发表评论");
                                            tempDatas.clear();
                                            tempDatas.add("");
                                            tempDatas.add("");
//                                             refresh();
                                            mComment_listview.setSelection(1);
                                        }
                                    }
                                });

                            }
                            else {
                                Log.e("dssf",e.getMessage());
                            }
                        }
                    });
                }else {
                    toast("不能发空白评论");
                }


            }
            else {
                Intent intent1 = new Intent(this,LoginActivity.class);
                startActivity(intent1);
            }
        }
    }

    @Override
    public void returnData(String rename, String recotnet) {
        tempDatas.clear();
        show_Reply("回复:"+rename);
        tempDatas.add(rename);
        tempDatas.add(recotnet);
    }
}
