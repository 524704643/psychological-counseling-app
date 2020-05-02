package com.example.tiongradua.graduationproject.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tiongradua.graduationproject.MainActivity;
import com.example.tiongradua.graduationproject.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginFragment extends Fragment implements View.OnClickListener {
    EditText mLogin_user,mLogin_password;
    Button mLogin_btn;
    CheckBox mRememberPassword;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater mInflater, ViewGroup container,Bundle savedInstanceState) {
        View loginView =mInflater.inflate(R.layout.login_fragment,container,false);
        initView(loginView);
        RememberPassword();
        return loginView;
    }

    private void RememberPassword() {
        preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        Boolean isRemember = preferences.getBoolean("remember",false);
        if (isRemember){
            String userName = preferences.getString("username","");
            String passWord = preferences.getString("password","");
            mLogin_user.setText(userName);
            mLogin_password.setText(passWord);
            mRememberPassword.setChecked(true);
        }
    }

    private void initView(View v) {
        mLogin_user = (EditText)v.findViewById(R.id.login_user);
        mLogin_password = (EditText)v.findViewById(R.id.login_password);
        mLogin_btn = (Button)v.findViewById(R.id.login_btn);
        mLogin_btn.setOnClickListener(this);
        mRememberPassword = (CheckBox)v.findViewById(R.id.rememberPassword);
    };
    private void toast(String s) {
        Toast toast=Toast.makeText(getActivity(),null,Toast.LENGTH_LONG);
        toast.setText(s);
        toast.show();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_btn){
            final String userName = mLogin_user.getText().toString();
            final String passWord = mLogin_password.getText().toString();
            final BmobUser userlogin=new BmobUser();
            userlogin.setUsername(userName);
            userlogin.setPassword(passWord);
            userlogin.login(new SaveListener<BmobUser>() {
                @Override
                public void done(BmobUser bmobUser, BmobException e) {
                    if(e==null){
                        toast("登录成功");
                            editor = preferences.edit();
                            if (mRememberPassword.isChecked()){
                                editor.putString("username",userName);
                                editor.putString("password",passWord);
                                editor.putBoolean("remember",true);
                            }else {
                                editor.clear();
                            }
                            editor.apply();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(),MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                                intent.putExtra("flag",1);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }).start();

                    }else {
                            Toast.makeText(getActivity(),"密码错误",Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

}
