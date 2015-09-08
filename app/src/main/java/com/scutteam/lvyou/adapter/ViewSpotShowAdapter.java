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
import com.scutteam.lvyou.activity.MakeJourneyActivity;
import com.scutteam.lvyou.model.ViewSpot;

import java.util.ArrayList;

/**
 * Created by admin on 15/8/19.
 */
public class ViewSpotShowAdapter extends BaseAdapter {
    
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<ViewSpot> viewSpotList = new ArrayList<ViewSpot>();

    public ViewSpotShowAdapter(Context context, ArrayList<ViewSpot> viewSpotList) {
        this.context = context;
        this.viewSpotList.addAll(viewSpotList);
        
        layoutInflater = LayoutInflater.from(context);
    }
    
    public void reloadWithList(ArrayList<ViewSpot> viewSpotList) {
        if(this.viewSpotList.size() > 0) {
            this.viewSpotList.clear();
        }
        this.viewSpotList.addAll(viewSpotList);
        this.notifyDataSetChanged();
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
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.view_spot_show_adapter_item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ViewSpot viewSpot = viewSpotList.get(position);
        
        ImageLoader.getInstance().displayImage(viewSpot.cover_pic,viewHolder.mIvAvatar);
        viewHolder.mTvName.setText(viewSpot.title);
        if(viewSpot.price == 0) {
            viewHolder.mTvPrice.setText("免费景点");
        } else {
            viewHolder.mTvPrice.setText(viewSpot.price + "元/人");
        }
        viewHolder.mIvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < ((MakeJourneyActivity)context).viewSpotList.size(); i++) {
                    long id = ((MakeJourneyActivity)context).viewSpotList.get(i).view_spot_id;
                    
                    if(id == viewSpot.view_spot_id) {
                        ((MakeJourneyActivity)context).viewSpotList.get(i).is_select = 0;
                        break;
                    }
                }
                ((MakeJourneyActivity)context).viewSpotSelectedList.remove(viewSpot);
                ((MakeJourneyActivity)context).changeCalculateUI();
                if(((MakeJourneyActivity)context).viewSpotSelectedList.size() > 0) {
                    ((MakeJourneyActivity)context).mj_play_item_nums.setText("已选择"+((MakeJourneyActivity)context).viewSpotSelectedList.size()+"个游玩项目");
                } else {
                    ((MakeJourneyActivity)context).mj_play_item_nums.setText("请选择游玩项目");
                }
                ((MakeJourneyActivity)context).selectNum = ((MakeJourneyActivity)context).viewSpotSelectedList.size();
                
                viewSpotList.remove(viewSpot);
                        
                reloadData();
            }
        });
        
        return convertView;
    }
    
    public void reloadData() {
        this.notifyDataSetChanged();
    }
    
    private class ViewHolder {
        private TextView mTvName;
        private TextView mTvPrice;
        private ImageView mIvAvatar;
        private ImageView mIvRemove;
        
        public ViewHolder(View view) {
            mTvName = (TextView) view.findViewById(R.id.tv_name);
            mTvPrice = (TextView) view.findViewById(R.id.tv_price);
            mIvAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            mIvRemove = (ImageView) view.findViewById(R.id.iv_remove);
        }
    }
}
