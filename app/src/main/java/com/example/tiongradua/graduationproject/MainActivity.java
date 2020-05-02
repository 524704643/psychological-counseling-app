package com.example.tiongradua.graduationproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tiongradua.graduationproject.Fragment.HomeFragment;
import com.example.tiongradua.graduationproject.Fragment.MineFragment;
import com.example.tiongradua.graduationproject.Fragment.PsychicFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout MenuTabLinearLayout;
    Fragment HomeFragment;
    Fragment PsychicFragment;
    Fragment MineFragment;
    Fragment LastFragment;
     int isSelectFragment = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
        LoginSuccessIntent();
        if (isSelectFragment == 0){
            View firstView = MenuTabLinearLayout.getChildAt(0);
            firstView.setSelected(true);
            if (HomeFragment == null){
                HomeFragment = new HomeFragment();
            }
            showFragment(HomeFragment,"HomeTag");
        }
        else if (isSelectFragment == 2){
            View firstView = MenuTabLinearLayout.getChildAt(1);
            firstView.setSelected(true);
            if (PsychicFragment == null){
                PsychicFragment = new PsychicFragment();
            }
            showFragment(PsychicFragment,"PsychicTag");
        }
        else {
            View firstView = MenuTabLinearLayout.getChildAt(2);
            firstView.setSelected(true);
            if (MineFragment == null){
                MineFragment = new MineFragment();
            }
            showFragment(MineFragment,"MineTag");
        }

    }

    private void LoginSuccessIntent() {
        int flag = getIntent().getIntExtra("flag",0);
        if (flag == 1){
            isSelectFragment = 1;
        }
        if (flag ==2){
            isSelectFragment = 2;
        }
    }

    private void initview() {
        MenuTabLinearLayout = findViewById(R.id.MenuTab);
        int MenuCount = MenuTabLinearLayout.getChildCount();
        for (int i=0;i<MenuCount;i++){
            View MenuView =MenuTabLinearLayout.getChildAt(i);
            MenuView.setTag(i);
            MenuView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        MenuTabLinearLayout.getChildAt(0).setSelected(false);
        MenuTabLinearLayout.getChildAt(1).setSelected(false);
        MenuTabLinearLayout.getChildAt(2).setSelected(false);
        int index = (Integer)view.getTag();
        if (index ==0){
            if (HomeFragment == null){
                HomeFragment = new HomeFragment();

            }
//            Toast.makeText(MainActivity.this,"1",Toast.LENGTH_LONG).show();
            showFragment(HomeFragment,"HomeTag");
            MenuTabLinearLayout.getChildAt(0).setSelected(true);
        }
        else if (index == 1){
            if (PsychicFragment == null){
                PsychicFragment = new PsychicFragment();
            }
//            Toast.makeText(MainActivity.this,"10",Toast.LENGTH_LONG).show();
            showFragment(PsychicFragment,"PsychicTag");
            MenuTabLinearLayout.getChildAt(1).setSelected(true);
        }else{
            if (MineFragment == null){
                MineFragment = new MineFragment();
            }
            showFragment(MineFragment,"MineTag");
            MenuTabLinearLayout.getChildAt(2).setSelected(true);
        }
    }

    private void showFragment(Fragment targetFragment,String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment tempFragment = fragmentManager.findFragmentByTag(tag);
        if (LastFragment!=null){
            transaction.hide(LastFragment);
        }
        if (tempFragment == null){
            transaction.add(R.id.fragmentContainer,targetFragment,tag);
        }else {
            transaction.show(targetFragment);
        }
        LastFragment =targetFragment;
        transaction.commit();
    }
}
