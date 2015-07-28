package com.scutteam.lvyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scutteam.lvyou.R;
import com.scutteam.lvyou.model.LvYouTheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 15/7/27.
 */
public class SelectDestinationTypeAdapter extends BaseAdapter {
    
    private List<LvYouTheme> themeList = new ArrayList<LvYouTheme>();
    private Context context;
    private LayoutInflater layoutInflater;

    public SelectDestinationTypeAdapter(List<LvYouTheme> themeList, Context context) {
        this.themeList = themeList;
        this.context = context;
        
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return themeList.size();
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
            convertView = layoutInflater.inflate(R.layout.select_destination_type_item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
            //读theme数据
        LvYouTheme theme = themeList.get(position);

        viewHolder.tv_theme_name.setText(theme.title);
        if(theme.is_select) {
            viewHolder.iv_is_selected.setVisibility(View.VISIBLE);        
        } else {
            viewHolder.iv_is_selected.setVisibility(View.GONE);
        }
        
        return convertView;
    }
    
    public class ViewHolder {
        public TextView tv_theme_name;
        public ImageView iv_is_selected;
        
        public ViewHolder(View view) {
            tv_theme_name = (TextView) view.findViewById(R.id.tv_theme_name);
            iv_is_selected = (ImageView) view.findViewById(R.id.iv_is_select);
        }
    }
}
