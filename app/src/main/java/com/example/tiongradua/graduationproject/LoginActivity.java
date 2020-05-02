package com.example.tiongradua.graduationproject;

import android.graphics.Color;
import android.support.annotation.Px;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tiongradua.graduationproject.Adapter.ViewPagerApdapter;
import com.example.tiongradua.graduationproject.Fragment.LoginFragment;
import com.example.tiongradua.graduationproject.Fragment.RegisterFragment;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    ViewPager mViewPager;
    List<Fragment> fragments;
    FragmentManager fragmentManager;
    TextView mTv_login,mTv_register;
    View mView_login,mView_register;
    ImageView mIv_login,mIv_register;
    RelativeLayout mLayoutBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initViewPagers();
    }

    private void initViewPagers() {
        fragmentManager = this.getSupportFragmentManager();
        fragments = new ArrayList<>();
        fragments.add(new LoginFragment());
        fragments.add(new RegisterFragment());
        mViewPager.setAdapter(new ViewPagerApdapter(fragmentManager,fragments));
        mViewPager.setCurrentItem(0);
        mIv_register.setVisibility(View.INVISIBLE);

    }

    private void initViews() {
        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mTv_login = (TextView)findViewById(R.id.Tv_login);
        mTv_register = (TextView)findViewById(R.id.Tv_register);
        mTv_login.setOnClickListener(this);
        mTv_register.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(this);
        mView_login = (View)findViewById(R.id.View_login);
        mView_register = (View)findViewById(R.id.View_register);
        mIv_login = (ImageView)findViewById(R.id.Iv_login);
        mIv_register = (ImageView)findViewById(R.id.Iv_register);
        mLayoutBack = (RelativeLayout)findViewById(R.id.LoginLayoutBack);
        mLayoutBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Tv_login){
            mViewPager.setCurrentItem(0);
            mView_login.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            mView_register.setBackgroundColor(Color.WHITE);
            mIv_register.setVisibility(View.INVISIBLE);
            mIv_login.setVisibility(View.VISIBLE);
        }
        if (v.getId() == R.id.Tv_register){
            mViewPager.setCurrentItem(1);
            mView_register.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            mView_login.setBackgroundColor(Color.WHITE);
            mIv_login.setVisibility(View.INVISIBLE);
            mIv_register.setVisibility(View.VISIBLE);
        }
        if (v.getId() == R.id.LoginLayoutBack){
            this.finish();
        }
    }

    @Override
    public void onPageScrolled(int i, float v, @Px int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        switch (i){
            case 0:
                mView_login.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mView_register.setBackgroundColor(Color.WHITE);
                mIv_register.setVisibility(View.INVISIBLE);
                mIv_login.setVisibility(View.VISIBLE);
                break;
            case 1:
                mView_register.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mView_login.setBackgroundColor(Color.WHITE);
                mIv_login.setVisibility(View.INVISIBLE);
                mIv_register.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
