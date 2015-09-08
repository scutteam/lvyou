package com.scutteam.lvyou.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
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
import com.scutteam.lvyou.model.LvYouDest;
import com.scutteam.lvyou.model.LvYouTheme;
import com.scutteam.lvyou.util.DensityUtil;
import com.scutteam.lvyou.util.ScreenManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 15/7/24.
 */
public class MainFragmentItemAdapter extends BaseAdapter {

    private Context context;
    private List<LvYouDest> destList = new ArrayList<LvYouDest>();
    private List<LvYouTheme> themeList = new ArrayList<LvYouTheme>();
    private LayoutInflater layoutInflater;

    public MainFragmentItemAdapter(Context context, List<LvYouDest> destList, List<LvYouTheme> themeList) {
        this.context = context;
        this.destList = destList;
        this.themeList = themeList;

        layoutInflater = LayoutInflater.from(context);
    }

    public void refreshWithThemeListAndDestList(List<LvYouTheme> newThemeList,List<LvYouDest> newDestList) {
        if(newThemeList.size() > 0) {
            destList.clear();
            themeList.clear();
        }
        destList.addAll(newDestList);
        themeList.addAll(newThemeList);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return destList.size() + themeList.size();
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
        int theme_count = themeList.size();
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.main_fragment_adapter_item,null);
           viewHolder = new ViewHolder(convertView);
           convertView.setTag(viewHolder);
        } else {
           viewHolder = (ViewHolder) convertView.getTag();
        }
        if(position >= theme_count) {
            //读dest数据
            LvYouDest dest = destList.get(position - theme_count);
            ImageLoader.getInstance().displayImage(dest.cover_pic, viewHolder.mIvBackground);
            viewHolder.mTvTitle.setText(dest.title);
            viewHolder.mTvShortIntroOrNumOfDestination.setText(dest.short_intro);
            viewHolder.mTvMark.setText(dest.label);
            viewHolder.mTvMark.setVisibility(View.VISIBLE);

        } else {
            //读theme数据
            LvYouTheme theme = themeList.get(position);
            ImageLoader.getInstance().displayImage(theme.cover_pic, viewHolder.mIvBackground);
            viewHolder.mTvTitle.setText(theme.title);
            viewHolder.mTvShortIntroOrNumOfDestination.setText(theme.dest_num + "个目的地");
            viewHolder.mTvMark.setVisibility(View.GONE);
        }
        return convertView;
    }

    public class ViewHolder {
        private ImageView mIvBackground;
        private TextView mTvTitle;
        private TextView mTvShortIntroOrNumOfDestination;
        private TextView mTvMark;

        public ViewHolder(View view) {
            mIvBackground = (ImageView) view.findViewById(R.id.iv_background);
            mTvTitle = (TextView) view.findViewById(R.id.tv_title);
            mTvShortIntroOrNumOfDestination = (TextView) view.findViewById(R.id.tv_short_intro_or_num_of_destination);
            mTvMark = (TextView) view.findViewById(R.id.tv_mark);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtil.getScreenWidthPx(context),
                    ((int)((double)DensityUtil.getScreenWidthPx(context) / Constants.Config.IMAGE_WIDTH * Constants.Config.IMAGE_HEIGHT)));
            mIvBackground.setLayoutParams(params);
        }
    }
}
