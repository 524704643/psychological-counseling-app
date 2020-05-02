package com.example.tiongradua.graduationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.bmob.v3.BmobUser;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout mLinearBack;
    RelativeLayout mQuitLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        mLinearBack = (LinearLayout)findViewById(R.id.setting_back);
        mQuitLayout = (RelativeLayout)findViewById(R.id.setting_quitLayout);
        mQuitLayout.setOnClickListener(this);
        mLinearBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.setting_back){
            finish();
        }
        if (v.getId() == R.id.setting_quitLayout){
            BmobUser.logOut();
            Intent intent = new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
            intent.putExtra("flag",1);
            startActivity(intent);
            finish();
        }
    }
}
