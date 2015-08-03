package com.scutteam.lvyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.interfaces.ViewSpotListener;
import com.scutteam.lvyou.model.ViewSpot;

import java.util.ArrayList;

/**
 * Created by admin on 15/8/3.
 */
public class ViewSpotAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ViewSpot>viewSpotList = new ArrayList<ViewSpot>();
    private LayoutInflater layoutInflater;
    private ViewSpotListener listener;

    public ViewSpotAdapter(Context context, ArrayList<ViewSpot>viewSpotList) {
        this.context = context;
        this.viewSpotList = viewSpotList;

        layoutInflater = LayoutInflater.from(context);
    }
    
    public void setListener(ViewSpotListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return this.viewSpotList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.view_spot_item_layout,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ViewSpot viewSpot = viewSpotList.get(position);
        viewHolder.mTvName.setText(viewSpot.title);
        if(viewSpot.price > 0) {
            viewHolder.mTvPrice.setText("成团价 "+viewSpot.price+"元/人");    
        } else {
            viewHolder.mTvPrice.setText("免费景点");
        }
        
        viewHolder.mRbScore.setRating(Float.parseFloat(viewSpot.score.toString()));
        viewHolder.mTvScore.setText(viewSpot.score.toString());
        ImageLoader.getInstance().displayImage(viewSpot.cover_pic,viewHolder.mIvBackground);
        if(viewSpot.is_select == 1) {
            viewHolder.mIvIsSelect.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.hotel_item_layout_selected));
        } else {
            viewHolder.mIvIsSelect.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.hotel_item_layout_unselected));
        }
        
        viewHolder.mIvIsSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             listener.WhenViewSpotSelectIconClick(viewSpot);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        private ImageView mIvIsSelect;
        private ImageView mIvBackground;
        private TextView  mTvName;
        private RatingBar mRbScore;
        private TextView mTvScore;
        private TextView mTvPrice;
        private TextView mTvIntro;

        public ViewHolder(View view) {
            mIvBackground = (ImageView) view.findViewById(R.id.iv_background);
            mIvIsSelect = (ImageView) view.findViewById(R.id.iv_is_select);
            mTvName = (TextView) view.findViewById(R.id.tv_name);
            mRbScore = (RatingBar) view.findViewById(R.id.rb_score);
            mTvScore = (TextView) view.findViewById(R.id.tv_score);
            mTvPrice = (TextView) view.findViewById(R.id.tv_price);
            mTvIntro = (TextView) view.findViewById(R.id.tv_intro);
        }
    }
}
