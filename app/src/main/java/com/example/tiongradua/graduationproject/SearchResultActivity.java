package com.example.tiongradua.graduationproject;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiongradua.graduationproject.Adapter.ResultApapter;
import com.example.tiongradua.graduationproject.Comon.WeiboDialogUtils;
import com.example.tiongradua.graduationproject.domain.Comment;
import com.example.tiongradua.graduationproject.domain.MyUser;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.header.PhoenixHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;

public class SearchResultActivity extends AppCompatActivity implements View.OnClickListener {

    List<MyUser> mList;
    RecyclerView mRecyclerView;
    ResultApapter apapter;
    ImageView mIv_back;
    EditText mEt_searchText;
    String SearchName;
    private Dialog mWeiboDialog;
    RefreshLayout refreshLayout;
    RelativeLayout mSearchNoneLayout;
     Handler mHmandler = new Handler() {
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
        setContentView(R.layout.activity_search_result);
        initData();
        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                query("reflash");
            }
        }).start();
        apapter = new ResultApapter(SearchResultActivity.this,mList);
        mRecyclerView.setAdapter(apapter);
    }

    private void initData() {
        Intent intent = getIntent();
         SearchName = intent.getStringExtra("SearchName");
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SearchResultActivity.this, "加载中...");
        mHmandler.sendEmptyMessageDelayed(1, 1000);
    }

    private void query(final String isRin) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String bql;
                if (SearchName.equals("")){
                     bql = "select * from MyUser where specialist = "+true;
                }else {
                     bql = "select * from MyUser where  name = '"+SearchName+"' or type1= '"+SearchName+"'  or type2= '"+SearchName+"'  or type3= '"+SearchName+"'";
                }

                new BmobQuery<MyUser>().doSQLQuery(bql, new SQLQueryListener<MyUser>() {
                    @Override
                    public void done(BmobQueryResult<MyUser> result, BmobException e) {
                        if (e == null) {
                            List<MyUser> topiclist = (List<MyUser>) result.getResults();
                            if (topiclist != null && topiclist.size() > 0) {
                                MyUser myUser = null;
                                int listCount = topiclist.size();
                                int inNum = 6;
                                if (e == null) {
                                    mSearchNoneLayout.setVisibility(View.GONE);
                                    if (isRin == "reflash") {
                                        if (listCount > inNum) {
                                            for (int i = listCount - 1; i >= listCount - inNum; i--) {
                                                myUser = topiclist.get(i);
                                                mList.add(myUser);
                                            }
                                        } else {
                                            for (int i = listCount - 1; i >= 0; i--) {
                                                myUser = topiclist.get(i);
                                                mList.add(myUser);
                                            }
                                        }
                                    } else {
                                        int currentCount = apapter.getItemCount();//获取当前listview item数
                                        if (currentCount == listCount) {
                                            toast("数据已经空了");
                                        }
                                        if (currentCount + inNum < listCount) {
                                            for (int i = listCount - currentCount - 1; i < listCount - currentCount - inNum; i--) {
                                                myUser = topiclist.get(i);
                                                mList.add(myUser);
                                            }

                                        } else {
                                            for (int i = listCount - currentCount - 1; i >= 0; i--) {
                                                myUser = topiclist.get(i);
                                                mList.add(myUser);
                                            }
                                        }
                                    }
                                    apapter.notifyDataSetChanged();
                                } else {
                                    Log.e("BMOB", e.toString());
                                }

                            } else {
                                Log.i("smile", "查询成功，无数据返回");
                                mSearchNoneLayout.setVisibility(View.VISIBLE);


                            }
                        } else {
                            Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                        }
                    }
                });
            }
        }).start();
    }
    public void ReturnSearchResult(String data){
        Intent intent = new Intent(this,SearchResultActivity.class);
        intent.putExtra("SearchName",data);
        startActivity(intent);
        this.finish();
    }
    private void initView() {

        refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mList.clear();
                refreshlayout.finishRefresh(1000);
                toast("已经是最新数据");
                query("reflash");
                apapter.notifyDataSetChanged();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                query("loadmore");
                refreshlayout.finishLoadmore(1000);
            }
        });
        //设置上下拉刷新主题
        refreshLayout.setRefreshHeader(new MaterialHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        Bmob.initialize(this, "b735d6c77e6a925949bbd28bdf771f43"); //bmob初始化
        mRecyclerView = (RecyclerView) findViewById(R.id.result_RecycleView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mList = new ArrayList<>();
        mIv_back = (ImageView)findViewById(R.id.result_back);
        mIv_back.setOnClickListener(this);
        mEt_searchText = (EditText)findViewById(R.id.result_edit);
        mEt_searchText.setText(SearchName);
        mSearchNoneLayout = (RelativeLayout)findViewById(R.id.SearchNoneLayout);
        mEt_searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //判断是否‘SEARCH’键
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);//关闭软键盘
                    ReturnSearchResult(mEt_searchText.getText().toString());
                    return true;
                }
                return false;
            }
        });

    }
    private void toast(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick( View v) {
        if (v.getId() == R.id.result_back){
            finish();
        }
    }
}
