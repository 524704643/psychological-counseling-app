package com.example.tiongradua.graduationproject.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tiongradua.graduationproject.Adapter.TopicAdapter;
import com.example.tiongradua.graduationproject.DynamicDetailsActivity;
import com.example.tiongradua.graduationproject.R;
import com.example.tiongradua.graduationproject.domain.Comment;
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
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class HotFragment extends Fragment {
    private List<Topic> topics ;
    private TopicAdapter adapter;
    private ListView mListView;
    private List<Comment> commentList;
    @Override
    public View onCreateView(LayoutInflater mInflater, ViewGroup container, Bundle savedInstanceState) {
        View hotView =mInflater.inflate(R.layout.hot_fragment,container,false);
        initView(hotView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                query("reflash");
                query2();
            }
        }).start();
        adapter = new TopicAdapter(getActivity(),topics, commentList);
        mListView.setAdapter(adapter);
        return hotView;
    }

    private void toast(String s) {
        Toast toast =Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT);
        toast.setText(s);
        toast.show();
    }
    private void initView(View v) {
        commentList = new ArrayList<>();
        topics = new ArrayList<Topic>();
        RefreshLayout refreshLayout = (RefreshLayout)v.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                topics.clear();
                refreshlayout.finishRefresh(1000);
                toast("已经是最新数据");
                query("reflash");
                adapter.notifyDataSetChanged();
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
        refreshLayout.setRefreshHeader(new MaterialHeader(getActivity()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        Bmob.initialize(getActivity(), "b735d6c77e6a925949bbd28bdf771f43"); //bmob初始化
        mListView = (ListView)v.findViewById(R.id.hot_listview);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Topic topic = topics.get(position);
                topic.setReadcount(topic.getReadcount()+1);

                topic.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            Intent intent = new Intent(getActivity(), DynamicDetailsActivity.class);
                            intent.putExtra("userName",topic.getUsername());
                            intent.putExtra("Id",topic.getId());
                            startActivity(intent);
                        }
                    }
                });

            }
        });

    }
    private void query2(){
        BmobQuery<Comment> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                Comment comment = null;
                int listCount = list.size();
                if (e == null){
                    for (int i=listCount-1;i>=0;i--){
                        comment = list.get(i);
                        commentList.add(comment);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void query(final String isRin) {

        BmobQuery<Topic> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereGreaterThanOrEqualTo("readcount",50);
        bmobQuery.findObjects(new FindListener<Topic>() {
            @Override
            public void done(List<Topic> topiclist, BmobException e) {
                Topic topic = null;
                int listCount = topiclist.size();
                int inNum = 8;
                if (e == null) {

                    if (isRin == "reflash"){
                        if (listCount > inNum){
                            for (int i=listCount-1;i>=listCount-inNum;i--){
                                topic = topiclist.get(i);
                                topics.add(topic);
                            }
                        }else {
                            for (int i=listCount-1;i>=0;i--){
                                topic = topiclist.get(i);
                                topics.add(topic);
                            }
                        }
                    }
                    else{
                        int currentCount= adapter.getCount();//获取当前listview item数
                        if (currentCount == listCount){
                            toast("数据已经空了");
                        }
                        if (currentCount + inNum <listCount){
                            for (int i=listCount-currentCount-1;i<listCount-currentCount-inNum;i--){
                                topic = topiclist.get(i);
                                topics.add(topic);
                            }

                        }else {
                            for (int i=listCount-currentCount-1;i>=0;i--){
                                topic = topiclist.get(i);
                                topics.add(topic);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }
}
