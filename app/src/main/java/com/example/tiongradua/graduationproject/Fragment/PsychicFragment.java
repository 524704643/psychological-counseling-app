package com.example.tiongradua.graduationproject.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiongradua.graduationproject.Adapter.TopicAdapter;
import com.example.tiongradua.graduationproject.Adapter.ViewPagerApdapter;
import com.example.tiongradua.graduationproject.Comon.WeiboDialogUtils;
import com.example.tiongradua.graduationproject.DynamicDetailsActivity;
import com.example.tiongradua.graduationproject.R;
import com.example.tiongradua.graduationproject.domain.Topic;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class PsychicFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    ViewPager mViewPager1;
    List<Fragment> fragments1;
    FragmentManager fragmentManager1;
    RelativeLayout mNewLayout,mHotLayout;
    TextView mNewTv,mHotTv;
    View mNewView,mHotView;
    @Override
    public View onCreateView(LayoutInflater mInflater, ViewGroup container,Bundle savedInstanceState) {
        View psychicView =mInflater.inflate(R.layout.psychic_fragment,container,false);
        initViews(psychicView);
        initViewPagers(psychicView);
        return psychicView;
    }

    private void initViews(View v) {
        mViewPager1 = (ViewPager)v.findViewById(R.id.psy_viewpaper);
        mViewPager1.setOnPageChangeListener(this);
        mNewLayout = (RelativeLayout)v.findViewById(R.id.newLayout);
        mNewLayout.setOnClickListener(this);
        mHotLayout = (RelativeLayout)v.findViewById(R.id.hotLayout);
        mHotLayout.setOnClickListener(this);
        mNewTv = (TextView)v.findViewById(R.id.newTv);
        mHotTv = (TextView)v.findViewById(R.id.hotTv);
        mNewView = (View)v.findViewById(R.id.newView1);
        mHotView = (View)v.findViewById(R.id.hotView1);
    }

    private void initViewPagers(View psychicView) {
        fragmentManager1 = getActivity().getSupportFragmentManager();
        fragments1 = new ArrayList<>();
        fragments1.add(new NewFragment());
        fragments1.add(new HotFragment());
        mViewPager1.setAdapter(new ViewPagerApdapter(fragmentManager1,fragments1));
        mViewPager1.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.newLayout){
            mViewPager1.setCurrentItem(0);
            mNewTv.setTextColor(Color.parseColor("#f43a3a"));
            mHotTv.setTextColor(Color.parseColor("#5b5b5b"));
            mNewView.setVisibility(View.VISIBLE);
            mHotView.setVisibility(View.INVISIBLE);
        }
        if (v.getId() == R.id.hotLayout){
            mViewPager1.setCurrentItem(1);
            mHotTv.setTextColor(Color.parseColor("#f43a3a"));
            mNewTv.setTextColor(Color.parseColor("#5b5b5b"));
            mHotView.setVisibility(View.VISIBLE);
            mNewView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPageScrolled(int i, float v, @Px int i1) {

    }

    @Override
    public void onPageSelected(int i) {
            switch (i){
                case 0:
                    mNewTv.setTextColor(Color.parseColor("#f43a3a"));
                    mHotTv.setTextColor(Color.parseColor("#5b5b5b"));
                    mNewView.setVisibility(View.VISIBLE);
                    mHotView.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    mHotTv.setTextColor(Color.parseColor("#f43a3a"));
                    mNewTv.setTextColor(Color.parseColor("#5b5b5b"));
                    mHotView.setVisibility(View.VISIBLE);
                    mNewView.setVisibility(View.INVISIBLE);
                    break;
            }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
