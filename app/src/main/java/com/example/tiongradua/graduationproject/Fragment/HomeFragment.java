package com.example.tiongradua.graduationproject.Fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiongradua.graduationproject.Adapter.RecyclerViewApapter;
import com.example.tiongradua.graduationproject.ChatActivity;
import com.example.tiongradua.graduationproject.Comon.LoginCollection;
import com.example.tiongradua.graduationproject.DynamicDetailsActivity;
import com.example.tiongradua.graduationproject.InsertDynamicActivity;
import com.example.tiongradua.graduationproject.LoginActivity;
import com.example.tiongradua.graduationproject.R;
import com.example.tiongradua.graduationproject.SearchActivity;
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
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class HomeFragment extends Fragment implements View.OnClickListener, RecyclerViewApapter.OnItemClickListener {
    RecyclerView mRecyclerVIew;
    LinearLayout mLinearSearch;
    RecyclerViewApapter apapter;
    private List<MyUser> mData;
    @Override
    public View onCreateView(LayoutInflater mInflater, ViewGroup container,Bundle savedInstanceState) {
        View homeView =mInflater.inflate(R.layout.home_fragment,container,false);
        initview(homeView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                query("reflash");
            }
        }).start();
        apapter = new RecyclerViewApapter(getActivity(),mData);
        apapter.setmItemClickListener(this);
        mRecyclerVIew.setAdapter(apapter);
        return homeView;
    }

    private void initview(View view) {
        Bmob.initialize(getActivity(), "b735d6c77e6a925949bbd28bdf771f43"); //bmob初始化
        mLinearSearch = (LinearLayout)view.findViewById(R.id.home_search);
        mLinearSearch.setOnClickListener(this);
        mRecyclerVIew = (RecyclerView)view.findViewById(R.id.recyclerview);
        mRecyclerVIew.setLayoutManager(new LinearLayoutManager(getActivity()));
        mData = new ArrayList<MyUser>();
        RefreshLayout refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mData.clear();
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
        refreshLayout.setRefreshHeader(new MaterialHeader(getActivity()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        Bmob.initialize(getActivity(), "b735d6c77e6a925949bbd28bdf771f43"); //bmob初始化

    }
    private void toast(String s) {
        Toast toast =Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT);
        toast.setText(s);
        toast.show();
    }
    private void query(final String isRin) {

        BmobQuery<MyUser> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("specialist",true);
        bmobQuery.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> topiclist, BmobException e) {
                MyUser myUser = null;
                int listCount = topiclist.size();
                int inNum = 6;
                if (e == null) {

                    if (isRin == "reflash"){
                        if (listCount > inNum){
                            for (int i=listCount-1;i>=listCount-inNum;i--){
                                myUser = topiclist.get(i);
                                mData.add(myUser);
                            }
                        }else {
                            for (int i=listCount-1;i>=0;i--){
                                myUser = topiclist.get(i);
                                mData.add(myUser);
                            }
                        }
                    }
                    else{
                        int currentCount= apapter.getItemCount();//获取当前listview item数
                        if (currentCount == listCount){
                            toast("数据已经空了");
                        }
                        if (currentCount + inNum <listCount){
                            for (int i=listCount-currentCount-1;i<listCount-currentCount-inNum;i--){
                                myUser = topiclist.get(i);
                                mData.add(myUser);
                            }

                        }else {
                            for (int i=listCount-currentCount-1;i>=0;i--){
                                myUser = topiclist.get(i);
                                mData.add(myUser);
                            }
                        }
                    }
                    apapter.notifyDataSetChanged();
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.home_search){
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onItemClick(int position) {
         BmobUser user = BmobUser.getCurrentUser(BmobUser.class);
        if (user != null){
            MyUser myUser = mData.get(position-1);
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("rUser",myUser);
            intent.putExtras(bundle);
            startActivity(intent);
        }else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }


    }
}
