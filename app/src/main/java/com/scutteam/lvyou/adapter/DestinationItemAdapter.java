package com.scutteam.lvyou.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.model.Destination;
import com.scutteam.lvyou.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 15/7/27.
 */
public class DestinationItemAdapter extends BaseAdapter{

    private Context context;
    private List<Destination> destinationList = new ArrayList<Destination>();
    private LayoutInflater layoutInflater;

    public DestinationItemAdapter(Context context, List<Destination> destinationList) {
        this.context = context;
        this.destinationList.addAll(destinationList);
        
        layoutInflater = LayoutInflater.from(context);
    }
    
    public void refreshWithDestinationList(List<Destination>newDestinationList) {
        destinationList.clear();
        destinationList.addAll(newDestinationList);
        this.notifyDataSetChanged();
    }
    
    public void loadMoreWithDestinationList(List<Destination>newDestinationList) {
        destinationList.addAll(newDestinationList);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return destinationList.size();
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
        int theme_count = destinationList.size();
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.main_fragment_adapter_item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //读dest数据
        Destination dest = destinationList.get(position);
        ImageLoader.getInstance().displayImage(dest.cover_pic, viewHolder.mIvBackground);

        viewHolder.mTvTitle.setText(dest.title);
        viewHolder.mTvShortIntro.setText(dest.short_intro);
        viewHolder.mTvMark.setText(dest.label);
        viewHolder.mTvMark.setVisibility(View.VISIBLE);

        return convertView;
    }

    public class ViewHolder {
        private ImageView mIvBackground;
        private TextView mTvTitle;
        private TextView mTvShortIntro;
        private TextView mTvMark;

        public ViewHolder(View view) {
            mIvBackground = (ImageView) view.findViewById(R.id.iv_background);
            mTvTitle = (TextView) view.findViewById(R.id.tv_title);
            mTvShortIntro = (TextView) view.findViewById(R.id.tv_short_intro_or_num_of_destination);
            mTvMark = (TextView) view.findViewById(R.id.tv_mark);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtil.getScreenWidthPx(context),
                    ((int)((double)DensityUtil.getScreenWidthPx(context) / Constants.Config.IMAGE_WIDTH * Constants.Config.IMAGE_HEIGHT)));
            mIvBackground.setLayoutParams(params);
        }
    }
}
