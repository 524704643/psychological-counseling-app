package com.example.tiongradua.graduationproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tiongradua.graduationproject.Comon.DateUtils;
import com.example.tiongradua.graduationproject.R;
import com.example.tiongradua.graduationproject.UserDetailsActivity;
import com.example.tiongradua.graduationproject.domain.Chat;
import com.example.tiongradua.graduationproject.domain.Comment;
import com.example.tiongradua.graduationproject.domain.Topic;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by 本人 on 2019/11/11.
 */

public class ChatAdapter extends BaseAdapter  {
    private Context mContext;
    private List<Chat> chatList;
    private HashMap<Integer,View> map = new HashMap<Integer,View>();
    private final int TYPE_1 = 0;
    private final int TYPE_2 = 1;
    private final int VIEW_TYPE = 2;
    public ChatAdapter(Context context, List<Chat> list){
        mContext = context;
        chatList = list;
    }

    @Override
    public int getCount() {
        return chatList == null ? 0 : chatList.size();
    }

    @Override
    public Object getItem(int position) {

      return chatList.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override

    public int getItemViewType(int position) {
        Bmob.initialize(mContext, "b735d6c77e6a925949bbd28bdf771f43"); //bmob初始化
        final BmobUser user = BmobUser.getCurrentUser(BmobUser.class);
        if (chatList.get(position).getSusername().equals(user.getUsername())){
            return TYPE_2;
        }else {
            return TYPE_1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        TypeOneHolder typeholder = null;
        final Chat chat = chatList.get(position);
        switch (getItemViewType(position)){
            case TYPE_1:
                if (map.get(position) == null){
                    typeholder = new TypeOneHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.char_left_layout,parent,false);
                    typeholder.mTv_photo1 = convertView.findViewById(R.id.chat_photo);
                    typeholder.mTv_time1 = convertView.findViewById(R.id.chat_time);
                    typeholder.mTv_text1 = convertView.findViewById(R.id.chat_text);
                    convertView.setTag(typeholder);
                    Bitmap bitmap = BitmapFactory.decodeFile(chat.getSimage().getPath());
                    typeholder.mTv_photo1.setImageBitmap(bitmap);
                    typeholder.mTv_text1.setText(chat.getContent());
                    DateUtils dateUtils =new DateUtils();
                    Date date = null;
                    try {
                        date = dateUtils.stringToDate(chat.getCreatedAt(),"yyyy-MM-dd HH:mm");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String getdate = dateUtils.getDateToString(date);
                    if (position == 1){
                        typeholder.mTv_time1.setVisibility(View.VISIBLE);
                        typeholder.mTv_time1.setText(getdate);
                    }

                }else {
                    convertView = map.get(position);
                    typeholder=(TypeOneHolder) convertView.getTag();
                }
                break;

            case TYPE_2:
                if (map.get(position) == null) {
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.char_right_layout, parent, false);
                    holder.mTv_photo = convertView.findViewById(R.id.chat_photo2);
                    holder.mTv_time = convertView.findViewById(R.id.chat_time2);
                    holder.mTv_text = convertView.findViewById(R.id.chat_text2);
                    convertView.setTag(holder);
                   Bitmap bitmap = BitmapFactory.decodeFile(chat.getSimage().getPath());
                    holder.mTv_photo.setImageBitmap(bitmap);
                    holder.mTv_text.setText(chat.getContent());
                    DateUtils dateUtils = new DateUtils();
                    Date date = null;
                    try {
                        date = dateUtils.stringToDate(chat.getCreatedAt(), "yyyy-MM-dd HH:mm");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String getdate = dateUtils.getDateToString(date);
                    if (position==1 || position == 4){
                        holder.mTv_time.setVisibility(View.VISIBLE);
                        holder.mTv_time.setText(getdate);
                    }


                }else {
                        convertView = map.get(position);
                        holder=(ViewHolder)convertView.getTag();
                    }

                break;
        }

        return convertView;
    }

    public class ViewHolder{
        ImageView mTv_photo;
        TextView mTv_time;
        TextView mTv_text;
    }

    public class TypeOneHolder{
        ImageView mTv_photo1;
        TextView mTv_time1;
        TextView mTv_text1;
    }
}
