package com.example.tiongradua.graduationproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiongradua.graduationproject.Comon.DateUtils;
import com.example.tiongradua.graduationproject.DynamicDetailsActivity;
import com.example.tiongradua.graduationproject.R;
import com.example.tiongradua.graduationproject.UserDetailsActivity;
import com.example.tiongradua.graduationproject.domain.Comment;
import com.example.tiongradua.graduationproject.domain.Topic;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 本人 on 2019/11/11.
 */

public class CommentAdapter extends BaseAdapter  {
    private Context mContext;
    private List<Comment> topicList;
    private List<Topic> mdatas;
    private Callback mCallBack;
    private HashMap<Integer,View> map = new HashMap<Integer,View>();
    private final int TYPE_1 = 0;
    private final int TYPE_2 = 1;
    private final int VIEW_TYPE = 2;
    public CommentAdapter(Context context, List<Comment> list, List<Topic> datas,Callback callback){
        mContext = context;
        topicList = list;
        mdatas =datas;
        mCallBack =callback;
    }

    @Override
    public int getCount() {
        return topicList == null ? 0 : topicList.size()+1;
    }

    @Override
    public Object getItem(int position) {
        if (position==0){
            return mdatas.get(position);
        }else {
            return topicList.get(position-1);
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override

    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_1;
        }else {
            return TYPE_2;
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

        switch (getItemViewType(position)){
            case TYPE_1:
                if (convertView == null){
                    typeholder = new TypeOneHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.gettopic_layout,null,false);
                    typeholder.mIv_photo = convertView.findViewById(R.id.details_photo);
                    typeholder.mTv_name = convertView.findViewById(R.id.details_name);
                    typeholder.mTv_title = convertView.findViewById(R.id.details_title);
                    typeholder.mTv_content = convertView.findViewById(R.id.details_content);
                    typeholder.mTv_readCount = convertView.findViewById(R.id.details_readCount);
                    typeholder.mTv_commentCount = convertView.findViewById(R.id.details_commentCount);
                    typeholder.mTv_time =convertView.findViewById(R.id.details_time);
                    convertView.setTag(typeholder);
                    final Topic topic =  mdatas.get(0);
                    typeholder.mIv_photo.setImageBitmap(BitmapFactory.decodeFile(topic.getImage().getPath()));
                    typeholder.mTv_name.setText(topic.getName());
                    typeholder.mTv_title.setText(topic.getTitle());
                    typeholder.mTv_content.setText(topic.getContent());
                    typeholder.mTv_readCount.setText("阅读量："+topic.getReadcount());
                    typeholder.mTv_commentCount.setText(topic.getCommentcount()+"个评论");
                    typeholder.mIv_photo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, UserDetailsActivity.class);
                            intent.putExtra("userName2",topic.getUsername());
                            mContext.startActivity(intent);

                        }
                    });

                    DateUtils dateUtils =new DateUtils();
                    try {
                        Date date = dateUtils.stringToDate(topic.getCreatedAt(),"yyyy-MM-dd HH:mm");
                        String getdate = dateUtils.getDateToString(date);
                        typeholder.mTv_time.setText(getdate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else {
                    //convertView = map.get(position);
                    typeholder=(TypeOneHolder) convertView.getTag();
                }
                break;

            case TYPE_2:
                 final Comment comment = topicList.get(position-1);
                if (map.get(position) == null){
                    if (topicList!=null){
                        holder=new ViewHolder();
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.comment_layout,parent,false);
                        holder.mIv_photo2 = convertView.findViewById(R.id.comment_photo);
                        holder.mTv_name = convertView.findViewById(R.id.comment_name);
                        holder.mIv_sex = convertView.findViewById(R.id.comment_sex);
                        holder.mTv_content = convertView.findViewById(R.id.comment_content);
                        holder.mTv_reComment = convertView.findViewById(R.id.comment_recontent);
                        holder.mSpecialist_Linear = convertView.findViewById(R.id.comment_isSpecialistLinear);
                        holder.mTv_time = convertView.findViewById(R.id.comment_time);
                        convertView.setTag(holder);
                        Bitmap bitmap = BitmapFactory.decodeFile(comment.getComment_photo().getPath());
                        holder.mIv_photo2.setImageBitmap(bitmap);
                        holder.mTv_name.setText(comment.getComment_name());
                        holder.mTv_content.setText(comment.getComment_content());

                        DateUtils dateUtils =new DateUtils();
                        Date date = null;
                        try {
                            date = dateUtils.stringToDate(comment.getCreatedAt(),"yyyy-MM-dd HH:mm");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String getdate = dateUtils.getDateToString(date);
                        holder.mTv_time.setText(getdate);


                        if (comment.getSpecialist()){
                            holder.mSpecialist_Linear.setVisibility(View.VISIBLE);
                        }

                        if (!comment.getRename().equals("")){
                            holder.mTv_reComment.setVisibility(View.VISIBLE);
                            SpannableStringBuilder builder = new SpannableStringBuilder(comment.getRename()+":"+comment.getRecontent());
                            SpannableStringBuilder builder2 = new SpannableStringBuilder(comment.getRecontent());
                            ForegroundColorSpan buleSpan = new ForegroundColorSpan(Color.parseColor("#4d8ade"));
                            builder.setSpan(buleSpan, 0, builder.length()-builder2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            holder.mTv_reComment.setText(builder);
                            holder.mTv_reComment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mCallBack.returnData(comment.getRename(),comment.getRecontent());
                                }
                            });
                        }else {
                            holder.mTv_reComment.setVisibility(View.GONE);
                        }
                        if (comment.getComment_sex().equals("男")){
                            holder.mIv_sex.setImageResource(R.drawable.icon_man);
                        }else {
                            holder.mIv_sex.setImageResource(R.drawable.icon_women);
                        }

                        //头像点击事件
                        final ViewHolder finalHolder = holder;
                        holder.mIv_photo2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, UserDetailsActivity.class);
                                intent.putExtra("userName2",comment.getComment_username());
                                mContext.startActivity(intent);
                            }
                        });
                    }else {
                        convertView = map.get(position);
                        holder=(ViewHolder)convertView.getTag();
                    }
                    }

                break;
        }

        return convertView;
    }
    public interface Callback{
        void returnData(String rename, String recotnet);
    }
    public class ViewHolder{
        ImageView mIv_photo2;
        ImageView mIv_sex;
        TextView mTv_name;
        TextView mTv_content;
        TextView mTv_reComment;
        TextView mTv_time;
        LinearLayout mSpecialist_Linear;
    }

    public class TypeOneHolder{
        ImageView mIv_photo;
        TextView mTv_title;
        TextView mTv_name;
        TextView mTv_content;
        TextView mTv_readCount;
        TextView mTv_time;
        TextView mTv_commentCount;
    }
}
