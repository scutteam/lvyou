package com.scutteam.lvyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.model.ViewSpot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 15/8/22.
 */
public class RecommendViewSpotDetailAdapter extends BaseAdapter {
    private List<ViewSpot>viewSpotList = new ArrayList<ViewSpot>();
    private Context context;
    private LayoutInflater layoutInflater;

    public RecommendViewSpotDetailAdapter(List<ViewSpot> viewSpotList, Context context) {
        this.viewSpotList = viewSpotList;
        this.context = context;
        
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return viewSpotList.size();
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
        final ViewSpot viewSpot = viewSpotList.get(position);

        if(viewSpot.view_spot_id != null) {
            if(convertView == null) {
                convertView = layoutInflater.inflate(R.layout.recommend_view_spot_detail_adapter_item,null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ImageLoader.getInstance().displayImage(viewSpot.cover_pic,viewHolder.mIvAvatar);
            viewHolder.mTvName.setText(viewSpot.title);
            if(viewSpot.price == 0) {
                viewHolder.mTvPrice.setText("免费景点");
            } else {
                viewHolder.mTvPrice.setText(viewSpot.price + "元/人");
            }
        } else {
            if(convertView == null) {
                convertView = layoutInflater.inflate(R.layout.view_spot_show_adapter_item_two,null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.mTvTitleHead.setText(viewSpot.title);
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView mTvName;
        private TextView mTvPrice;
        private ImageView mIvAvatar;
        private ImageView mIvRemove;
        private TextView mTvTitleHead;

        public ViewHolder(View view) {
            mTvName = (TextView) view.findViewById(R.id.tv_name);
            mTvPrice = (TextView) view.findViewById(R.id.tv_price);
            mIvAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            mIvRemove = (ImageView) view.findViewById(R.id.iv_remove);

            mTvTitleHead = (TextView) view.findViewById(R.id.tv_title_head);
        }
    }
}
