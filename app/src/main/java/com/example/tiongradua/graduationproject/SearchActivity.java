package com.example.tiongradua.graduationproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView mSearchBack,mHisSearchClean;
    EditText mEditText;
    TextView textView;
    private MyFlowLayout myFlowLayout,myFlowLayout2;
    private static String[] hotSearchName = {"性心理", "抑郁", "焦虑", "婚姻挽回", "早恋", "同性恋", "惆怅", "失恋", "情感修复"};
    private static ArrayList<String> hisSearchList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }


    public void initView() {
        mSearchBack = (ImageView) findViewById(R.id.search_back);
        mSearchBack.setOnClickListener(this);
        mHisSearchClean = (ImageView)findViewById(R.id.Image_hisSearchClean) ;
        mHisSearchClean.setOnClickListener(this);
        mEditText = (EditText)findViewById(R.id.home_edit);

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //判断是否‘SEARCH’键
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);//关闭软键盘
                    hisSearchList.add(0,mEditText.getText().toString());
                    ReturnSearchResult(mEditText.getText().toString());
                    return true;
                }
                return false;
            }
        });

        myFlowLayout = (MyFlowLayout) findViewById(R.id.hotSearchLinear);
        for (int i = 0; i < hotSearchName.length; i++) {
            LinearLayout hotSearchChildLinear = new LinearLayout(this);
            addChildView(hotSearchChildLinear);
            textView.setText(hotSearchName[i]);
            textView.setTag(i);
            myFlowLayout.addView(hotSearchChildLinear);
         }
        myFlowLayout2 = (MyFlowLayout) findViewById(R.id.hotSearchLinear2);
        for (int i=0;i<hisSearchList.size();i++) {
            LinearLayout hisSearchChildLinear = new LinearLayout(SearchActivity.this);
            addChildView(hisSearchChildLinear);
            textView.setTag(hotSearchName.length+i);
            textView.setText(hisSearchList.get(i));
            myFlowLayout2.addView(hisSearchChildLinear);
        }
}

    private void addChildView(LinearLayout targetLinear) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,0);
        targetLinear.setLayoutParams(params);
        textView = new TextView(this);
        textView.setPadding(30,15,30,15);
        textView.setBackground(getResources().getDrawable(R.drawable.home_hot_shape));
        textView.setOnClickListener(this);
        targetLinear.addView(textView);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.search_back){
            finish();
        }
        else if (view.getId() == R.id.Image_hisSearchClean){
            hisSearchList.clear();
        }
        else {
            for (int i=0;i<hotSearchName.length;i++){
                Integer index = (Integer)view.getTag();
                if (index ==i){
                   // Toast.makeText(this,hotSearchName[i],Toast.LENGTH_SHORT).show();
                    ReturnSearchResult(hotSearchName[i]);
                     }
            }
            for (int i=0;i<hisSearchList.size();i++){
                Integer index = (Integer)view.getTag();
                if (index ==i+hotSearchName.length){
                    //Toast.makeText(this,hisSearchList.get(i),Toast.LENGTH_SHORT).show();
                   ReturnSearchResult(hisSearchList.get(i));
                }
            }
        }
    }

    /**
     * ReturnSearchResult()
     * 跳转到搜索结果页面
     */
    public void ReturnSearchResult(String data){
            Intent intent = new Intent(this,SearchResultActivity.class);
            intent.putExtra("SearchName",data);
            startActivity(intent);
            this.finish();
    }
}
