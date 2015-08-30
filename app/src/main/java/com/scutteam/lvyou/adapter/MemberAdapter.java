package com.scutteam.lvyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.model.Member;
import com.scutteam.lvyou.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hono on 15/8/22.
 */
public class MemberAdapter extends BaseAdapter {
    private ArrayList<Member> members;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader;

    public MemberAdapter(Context context, List<Member> members) {
        layoutInflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
        this.members = (ArrayList<Member>) members;
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int i) {
        return members.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_get_insurance_member, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(members.get(position).name);
        imageLoader.displayImage(Constants.IMAGE_URL + members.get(position).avatar, viewHolder.civAvatar);

        return convertView;
    }

    public static class ViewHolder {
        private TextView tvName;
        private CircleImageView civAvatar;


        public ViewHolder(View view) {
            civAvatar = (CircleImageView) view.findViewById(R.id.item_get_insurance_member_avatar);
            tvName = (TextView) view.findViewById(R.id.item_get_insurance_member_name);
        }
    }
}
