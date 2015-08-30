package com.scutteam.lvyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scutteam.lvyou.R;
import com.scutteam.lvyou.interfaces.RecommendViewSpotItemListener;
import com.scutteam.lvyou.model.Recommendtrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 15/8/19.
 */
public class RecommendViewSpotAdapter extends BaseAdapter {
    
    private List<Recommendtrip> recommendtripList = new ArrayList<Recommendtrip>();
    private Context context;
    private LayoutInflater layoutInflater;
    private RecommendViewSpotItemListener listener;
    
    public void setListener(RecommendViewSpotItemListener listener) {
        this.listener = listener;
    }
    
    @Override
    public int getCount() {
        return recommendtripList.size();
    }
    
    public RecommendViewSpotAdapter(Context context,List<Recommendtrip>recommendtripList) {
        this.context = context;
        this.recommendtripList = recommendtripList;
        
        this.layoutInflater = LayoutInflater.from(context);
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
            convertView = layoutInflater.inflate(R.layout.recommend_view_spot_item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        final Recommendtrip trip = recommendtripList.get(position);
        viewHolder.mTvDest.setText(trip.dest);
        viewHolder.mTvTitle.setText(trip.trip_title);
        viewHolder.mTvPlayNum.setText(trip.play_num+"个游玩项目");
        viewHolder.mTvRemark.setText(trip.remark);
        viewHolder.mTvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.recommendViewSpotItemClick(trip);
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        private TextView mTvDest;
        private TextView mTvTitle;
        private TextView mTvPlayNum;
        private TextView mTvSelect;
        private ImageView mIvBackground;
        private TextView mTvRemark;

        public ViewHolder(View view) {
            mTvDest = (TextView) view.findViewById(R.id.tv_dest);
            mTvTitle = (TextView) view.findViewById(R.id.tv_title);
            mTvPlayNum = (TextView) view.findViewById(R.id.tv_play_num);
            mTvSelect = (TextView) view.findViewById(R.id.tv_select);
            mIvBackground = (ImageView) view.findViewById(R.id.iv_background);
            mTvRemark = (TextView) view.findViewById(R.id.tv_remark);
        }
    }
}
