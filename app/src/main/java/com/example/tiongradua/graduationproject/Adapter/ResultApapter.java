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
import com.example.tiongradua.graduationproject.domain.MyUser;

import java.util.List;

/**
 * Created by 本人 on 2019/11/17.
 */

public class ResultApapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<MyUser> mData;
    public ResultApapter(Context context, List<MyUser> data){
        mContext = context;
        mData = data;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.home_specialist_layout, parent, false);
        return new ViewHolder(inflate);

    }



    @Override
    public int getItemViewType(int position) {
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
            MyUser myUser = mData.get(position);
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
        return mData.size();
    }
}
