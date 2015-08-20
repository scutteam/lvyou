package com.scutteam.lvyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.model.Comment;
import com.scutteam.lvyou.widget.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 15/7/31.
 */
public class CommentAdapter extends BaseAdapter {
    
    private Context context;
    private List<Comment> commentList = new ArrayList<Comment>();
    private LayoutInflater layoutInflater;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        if(this.commentList.size() > 0) {
            this.commentList.clear();
        }
        this.commentList.addAll(commentList);
        
        layoutInflater = LayoutInflater.from(context);
    }
    
    public void reloadWithCommentList(List<Comment> commentList) {
        if(this.commentList.size() > 0) {
            this.commentList.clear();
        }
        this.commentList.addAll(commentList);
        
        this.notifyDataSetChanged();
    }
    
    public void loadMoreWithCommentList(List<Comment> commentList) {
        this.commentList.addAll(commentList);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.comment_adapter_item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        Comment comment = commentList.get(position);
        if(comment.face.equals(Constants.IMAGE_URL + "null")) {
            viewHolder.mCivAvatar.setImageDrawable(context.getResources().getDrawable(R.mipmap.default_icon));
        } else {
            ImageLoader.getInstance().displayImage(comment.face,viewHolder.mCivAvatar);
        }

        viewHolder.mStarBar.setRating(Float.parseFloat(comment.total_score.toString()));
        viewHolder.mTvComment.setText(comment.total_comment);
        viewHolder.mTvName.setText(comment.cust);

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String new_time = "";
        if(comment.create_time != null) {
            new_time = sdf.format(new Date(Long.parseLong(comment.create_time)));
        }

        viewHolder.mTvTime.setText(new_time);
        
        return convertView;
    }
    
    public class ViewHolder {
        public CircleImageView mCivAvatar;
        public RatingBar mStarBar;
        public TextView mTvName;
        public TextView mTvTime;
        public TextView mTvComment;
        
        public ViewHolder(View view) {
            mCivAvatar = (CircleImageView) view.findViewById(R.id.civ_avatar);
            mStarBar = (RatingBar) view.findViewById(R.id.rb_score);
            mTvName = (TextView) view.findViewById(R.id.tv_name);
            mTvComment = (TextView) view.findViewById(R.id.tv_comment);
            mTvTime = (TextView) view.findViewById(R.id.tv_time);
        }
    }
}
