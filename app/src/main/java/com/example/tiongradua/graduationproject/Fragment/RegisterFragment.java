package com.example.tiongradua.graduationproject.Fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tiongradua.graduationproject.ImproveUserActivity;
import com.example.tiongradua.graduationproject.MainActivity;
import com.example.tiongradua.graduationproject.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class RegisterFragment extends Fragment implements View.OnClickListener {
    EditText mRegister_user,mRegister_password,mRegister_rePassword;
    Button mRegister_btn;
    @Override
    public View onCreateView(LayoutInflater mInflater, ViewGroup container, Bundle savedInstanceState) {
        View registerView =mInflater.inflate(R.layout.register_fragment,container,false);
        initView(registerView);
        return registerView;
    }

    private void initView(View v) {
        Bmob.initialize(getActivity(), "b735d6c77e6a925949bbd28bdf771f43"); //bmob初始化
        mRegister_user = (EditText)v.findViewById(R.id.register_user);
        mRegister_password = (EditText)v.findViewById(R.id.register_password);
        mRegister_rePassword = (EditText)v.findViewById(R.id.register_rePassword);
        mRegister_btn = (Button)v.findViewById(R.id.register_btn);
        mRegister_btn.setOnClickListener(this);
    }
    private void toast(String s) {
        Toast toast=Toast.makeText(getActivity(),null,Toast.LENGTH_LONG);
        toast.setText(s);
        toast.show();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register_btn){
            String userName = mRegister_user.getText().toString();
            String passWord = mRegister_password.getText().toString();
            String rePassword = mRegister_rePassword.getText().toString();
            if (userName.equals("")){
                toast("用户名不能为空");
            }
            else if (passWord.equals("")){
                toast("密码不能为空");
            }
            else if (!passWord.equals(rePassword)){
                toast("两次输入的密码不正确");
            }else {
                BmobUser user =new BmobUser();
                user.setUsername(userName);
                user.setPassword(passWord);
                user.signUp(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser user, BmobException e) {
                        if(e==null)
                        {
                            toast("注册成功");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getActivity(),ImproveUserActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            }).start();
                        }else
                        {
                            toast("用户名已存在");
                        }

                    }
                });
            }
        }
    }
}
