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
import com.scutteam.lvyou.model.Hotel;

import java.util.ArrayList;

/**
 * Created by admin on 15/8/3.
 */
public class HotelAdapter extends BaseAdapter {
    
    private Context context;
    private ArrayList<Hotel>hotelList = new ArrayList<Hotel>();
    private LayoutInflater layoutInflater;

    public HotelAdapter(Context context, ArrayList<Hotel> hotelList) {
        this.context = context;
        this.hotelList = hotelList;
        
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.hotelList.size();
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
            convertView = layoutInflater.inflate(R.layout.hotel_item_layout,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        final Hotel hotel = hotelList.get(position);
        viewHolder.mTvIntro.setText(hotel.intro);
        viewHolder.mTvName.setText(hotel.level_name);
        viewHolder.mTvPrice.setText(hotel.price+"元/人");
        viewHolder.mRbScore.setRating(Float.parseFloat(hotel.score.toString()));
        viewHolder.mTvScore.setText(hotel.score.toString());
        ImageLoader.getInstance().displayImage(hotel.pic,viewHolder.mIvBackground);
        if(hotel.is_select == 1) {
            viewHolder.mIvIsSelect.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.hotel_item_layout_selected));
        } else {
            viewHolder.mIvIsSelect.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.hotel_item_layout_unselected));
        }

        viewHolder.mIvIsSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0 ; i < hotelList.size() ; i++) {
                    hotelList.get(i).is_select = 0;
                }
                hotelList.get(position).is_select = 1;
                HotelAdapter.this.notifyDataSetChanged();
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
