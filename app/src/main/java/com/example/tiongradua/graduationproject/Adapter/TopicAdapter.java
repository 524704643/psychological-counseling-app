package com.example.tiongradua.graduationproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tiongradua.graduationproject.R;
import com.example.tiongradua.graduationproject.UserDetailsActivity;
import com.example.tiongradua.graduationproject.domain.Comment;
import com.example.tiongradua.graduationproject.domain.Topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

/**
 * Created by 本人 on 2019/11/11.
 */

public class TopicAdapter extends BaseAdapter  {
    private Context mContext;
    private List<Topic> topicList;
    private List<Comment> mCommentList;
    private HashMap<Integer,View> map = new HashMap<Integer,View>();
    public TopicAdapter(Context context, List<Topic> list, List<Comment> topicCommentList){
        mContext = context;
        topicList = list;
        mCommentList = topicCommentList;
    }

    @Override
    public int getCount() {
        return topicList == null ? 0 : topicList.size();
    }

    @Override
    public Object getItem(int position) {
        return topicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final Topic topic = topicList.get(position);
        if (map.get(position) == null){
            holder=new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.topic_layout,parent,false);
            holder.mIv_photo = convertView.findViewById(R.id.topic_photo);
            holder.mTv_name = convertView.findViewById(R.id.topic_name);
            holder.mTv_title = convertView.findViewById(R.id.topic_title);
            holder.mTv_content = convertView.findViewById(R.id.topic_content);
            holder.mTv_readcount = convertView.findViewById(R.id.topic_readCount);
            holder.mTv_commentcount = convertView.findViewById(R.id.topic_commentCount);
            holder.mTv_c1 = convertView.findViewById(R.id.psy_Tv1);
            holder.mTv_c2 = convertView.findViewById(R.id.psy_Tv2);
            holder.mTv_c3 = convertView.findViewById(R.id.psy_Tv3);

            convertView.setTag(holder);
            Bitmap bitmap = BitmapFactory.decodeFile(topic.getImage().getPath());
            holder.mIv_photo.setImageBitmap(bitmap);
            holder.mTv_name.setText(topic.getName());
            holder.mTv_title.setText(topic.getTitle());
            holder.mTv_content.setText(topic.getContent());
            holder.mTv_readcount.setText("阅读量："+String.valueOf(topic.getReadcount()));
            if (topic.getCommentcount() == 0){
                holder.mTv_commentcount.setText("评论");
            }else {
                holder.mTv_commentcount.setText(String.valueOf(topic.getCommentcount()));
            }
            //头像点击事件
            holder.mIv_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, UserDetailsActivity.class);
                    intent.putExtra("userName2",topic.getUsername());
                    mContext.startActivity(intent);

                }
            });
           //holder.mTv_c1.setText(mCommentList.get(0).getComment_username());
//            holder.mTv_c2.setText(mtopicCommentList.get(1).get(1).getRename());
//            holder.mTv_c3.setText(mtopicCommentList.get(1).get(2).getRename());
        }else {
            convertView = map.get(position);
            holder=(ViewHolder)convertView.getTag();
        }
        return convertView;
    }

    public class ViewHolder{
        ImageView mIv_photo;
        TextView mTv_name;
        TextView mTv_title;
        TextView mTv_content;
        TextView mTv_readcount;
        TextView mTv_commentcount;
        TextView mTv_more;
        TextView mTv_c1;
        TextView mTv_c2;
        TextView mTv_c3;
    }
}
