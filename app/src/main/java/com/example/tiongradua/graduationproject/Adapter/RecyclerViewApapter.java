package com.example.tiongradua.graduationproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiongradua.graduationproject.Comon.LoginCollection;
import com.example.tiongradua.graduationproject.InsertDynamicActivity;
import com.example.tiongradua.graduationproject.LoginActivity;
import com.example.tiongradua.graduationproject.R;
import com.example.tiongradua.graduationproject.SearchActivity;
import com.example.tiongradua.graduationproject.SearchResultActivity;
import com.example.tiongradua.graduationproject.domain.MyUser;

import java.util.List;

/**
 * Created by 本人 on 2019/11/17.
 */

public class RecyclerViewApapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private final LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<MyUser> mData;
    private int TYPE_HEADER = 1001;
    LinearLayout mLinearIvGroup,mLinearIvGroup2;
    RelativeLayout mDescribeLayout,mSearchSpecialistLayout;
    private OnItemClickListener mItemClickListener;
    public static int ImageGroup[]={R.drawable.icon_01,R.drawable.icon_02,R.drawable.icon_03,R.drawable.icon_04,
            R.drawable.icon_05,R.drawable.icon_06,R.drawable.icon_07,R.drawable.icon_08};
    public static String nameGroup[]={"学习压力","情绪压力","亲子教育","恋爱咨询","婚姻咨询","职业压力","个人成长","人际交往"};
    public RecyclerViewApapter(Context context, List<MyUser> data){
        mContext = context;
        mData = data;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setmItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TYPE_HEADER){
            View headerView = mLayoutInflater.inflate(R.layout.hone_header_layout, parent, false);
            return new HeaderViewHolder(headerView);
        }
        View inflate = mLayoutInflater.inflate(R.layout.home_specialist_layout, parent, false);
        inflate.setOnClickListener(this);
        return new ViewHolder(inflate);

    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null){
            mItemClickListener.onItemClick((Integer)v.getTag());
        }
    }

    public interface  OnItemClickListener{
        void onItemClick(int position);
}
    /**
     * 头布局的viewholder
     */
    class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mDescribeLayout = itemView.findViewById(R.id.describeLaypout);
            mDescribeLayout.setOnClickListener(this);
            mSearchSpecialistLayout = itemView.findViewById(R.id.SearchSpecialistLayout);
            mSearchSpecialistLayout.setOnClickListener(this);
            mLinearIvGroup = itemView.findViewById(R.id.ImageGroup);
            mLinearIvGroup.setOnClickListener(this);
            mLinearIvGroup2 = itemView.findViewById(R.id.ImageGroup2);
            addImageView(ImageGroup);
        }
        /**
         * 动态增加ImageView控件,图片相同布局适配
         * @param imageGroup
         */
        private void addImageView(int[] imageGroup) {
            mLinearIvGroup.removeAllViews();
            for (int i=0;i<imageGroup.length;i++){
                LinearLayout mLinear =(LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.identical_pig_layout,null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT,1);
                mLinear.setLayoutParams(params);
                ImageView image = (ImageView)mLinear.findViewById(R.id.indentical_image);
                TextView tv = (TextView) mLinear.findViewById(R.id.indentical_tv);
                image.setImageResource(imageGroup[i]);
                tv.setText(nameGroup[i]);
                if (i<=3){
                    mLinearIvGroup.addView(mLinear);
                }
                else {
                    mLinearIvGroup2.addView(mLinear);
                }


            }
            //每个布局的事件
            for (int j=0;j<mLinearIvGroup.getChildCount();j++){
                View tempView =mLinearIvGroup.getChildAt(j);
                tempView.setTag(j);
                tempView.setOnClickListener(this);
            }
            for (int j=0;j<mLinearIvGroup2.getChildCount();j++){
                View tempView =mLinearIvGroup2.getChildAt(j);
                tempView.setTag(j+4);
                tempView.setOnClickListener(this);
            }
        }
        /**
         * ReturnSearchResult()
         * 跳转到搜索结果页面
         */
        public void ReturnSearchResult(String data){
            Intent intent = new Intent(mContext,SearchResultActivity.class);
            intent.putExtra("SearchName",data);
            mContext.startActivity(intent);
        }
        @Override
        public void onClick(View view) {

             if (view.getId() == R.id.describeLaypout){
                LoginCollection login = new LoginCollection();
                Boolean isRain = login.islogin();
                Intent intent;
                if (isRain){
                    intent = new Intent(mContext, InsertDynamicActivity.class);
                }else {
                    intent = new Intent(mContext, LoginActivity.class);
                }
                mContext.startActivity(intent);
            }
            else if (view.getId() == R.id.SearchSpecialistLayout){
                 ReturnSearchResult("");
             }
            else {
                int index = (Integer) view.getTag();
                if (index == 0){
                    ReturnSearchResult(nameGroup[0]);
                }
                else if(index == 1){
                    ReturnSearchResult(nameGroup[1]);
                }
                else if(index == 2){
                    ReturnSearchResult(nameGroup[2]);
                }
                else if(index == 3){
                    ReturnSearchResult(nameGroup[3]);
                }
                else if(index == 4){
                    ReturnSearchResult(nameGroup[4]);
                }
                else if(index == 5){
                    ReturnSearchResult(nameGroup[5]);
                }
                else if(index == 6){
                    ReturnSearchResult(nameGroup[6]);
                }
                else {
                    ReturnSearchResult(nameGroup[7]);
                }

            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        //在第一个位置添加头
        if (position==0){
            return TYPE_HEADER;
        }
        return super.getItemViewType(position);
    }



    /**
     * 正常内容的viewholder
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIv_photo;
        TextView mTv_name,mTv_type1,mTv_type2,mTv_type3;
        public ViewHolder(View itemView) {
            super(itemView);
            mIv_photo=itemView.findViewById(R.id.home_spe_photo);
            mTv_name = itemView.findViewById(R.id.home_spe_name);
            mTv_type1 = itemView.findViewById(R.id.home_spe_type1);
            mTv_type2 = itemView.findViewById(R.id.home_spe_type2);
            mTv_type3 = itemView.findViewById(R.id.home_spe_type3);
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            holder.itemView.setTag(position);
            MyUser myUser = mData.get(position-1);
            //postion==0被头布局占据
            ((ViewHolder) holder).mIv_photo.setImageBitmap(BitmapFactory.decodeFile(myUser.getImage().getPath()));
            ((ViewHolder) holder).mTv_name.setText(myUser.getName());
            if (myUser.getType1().equals("")){
                ((ViewHolder) holder).mTv_type1.setVisibility(View.GONE);
            }else {
                ((ViewHolder) holder).mTv_type1.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).mTv_type1.setText(myUser.getType1());
            }

            if (myUser.getType2().equals("")){
                ((ViewHolder) holder).mTv_type2.setVisibility(View.GONE);
            }else {
                ((ViewHolder) holder).mTv_type2.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).mTv_type2.setText(myUser.getType2());
            }

            if (myUser.getType3().equals("")){
                ((ViewHolder) holder).mTv_type3.setVisibility(View.GONE);
            }else {
                ((ViewHolder) holder).mTv_type3.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).mTv_type3.setText(myUser.getType3());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size()+1;
    }
}
