package com.scutteam.lvyou.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scutteam.lvyou.R;
import com.scutteam.lvyou.activity.MyJourneyActivity;
import com.scutteam.lvyou.model.Plan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hono on 15/8/22.
 */
public class MyPlanAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Plan> plans;
    private LayoutInflater layoutInflater;

    public MyPlanAdapter(Context context, List<Plan> plans) {
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
        this.plans = (ArrayList<Plan>) plans;
    }

    @Override
    public int getCount() {
        return plans.size();
    }

    @Override
    public Object getItem(int i) {
        return plans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.my_plan_adapter_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(mContext, MyJourneyActivity.class);
                        intent.putExtra("plan_logic_id", plans.get(position).id + "");
                        mContext.startActivity(intent);
                    }
                });
            }
        });

        viewHolder.tvName.setText(plans.get(position).title);
        viewHolder.tvStartPlace.setText(plans.get(position).place + "(" + plans.get(position).area + ")");
        viewHolder.tvMemberNum.setText(plans.get(position).member_num + "人团");
        viewHolder.tvCreateTime.setText("创建于: " + plans.get(position).create_time);
        viewHolder.tvId.setText("订单号: " + plans.get(position).order_num);

        viewHolder.tvState.setText(plans.get(position).state_text);
        switch (plans.get(position).state) {
            case Plan.STATE_SAVE:
                viewHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.my_plan_state_1));
                viewHolder.tvState.setBackgroundResource(R.drawable.shape_my_plan_state_1);
                viewHolder.tvLine.setBackgroundColor(mContext.getResources().getColor(R.color.my_plan_state_1));
                break;

            case Plan.STATE_HAS_SUBMIT:
            case Plan.STATE_PLAN_MADE:
                viewHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.my_plan_state_23));
                viewHolder.tvState.setBackgroundResource(R.drawable.shape_my_plan_state_23);
                viewHolder.tvLine.setBackgroundColor(mContext.getResources().getColor(R.color.my_plan_state_23));

                break;

            case Plan.STATE_SURE:
            case Plan.STATE_SIGN:
            case Plan.STATE_TRAVELING:
                viewHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.my_plan_state_456));
                viewHolder.tvState.setBackgroundResource(R.drawable.shape_my_plan_state_456);
                viewHolder.tvLine.setBackgroundColor(mContext.getResources().getColor(R.color.my_plan_state_456));

                break;

            case Plan.STATE_COMPLETE:
                viewHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.my_plan_state_7));
                viewHolder.tvState.setBackgroundResource(R.drawable.shape_my_plan_state_7);
                viewHolder.tvLine.setBackgroundColor(mContext.getResources().getColor(R.color.my_plan_state_7));

                break;

            case Plan.STATE_STOP:
                viewHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.my_plan_state_0));
                viewHolder.tvState.setBackgroundResource(R.drawable.shape_my_plan_state_0);
                viewHolder.tvLine.setBackgroundColor(mContext.getResources().getColor(R.color.my_plan_state_0));
                break;

            default:
                break;
        }
        return convertView;
    }

    public static class ViewHolder {
        private TextView tvName;
        private TextView tvStartPlace;
        private TextView tvMemberNum;
        private TextView tvCreateTime;
        private TextView tvId;
        private TextView tvState;
        private TextView tvLine;


        public ViewHolder(View view) {
            tvLine = (TextView) view.findViewById(R.id.my_plan_item_left_line);
            tvName = (TextView) view.findViewById(R.id.my_plan_item_destion_name);
            tvStartPlace = (TextView) view.findViewById(R.id.my_plan_item_school_name);
            tvMemberNum = (TextView) view.findViewById(R.id.my_plan_item_member_num);
            tvCreateTime = (TextView) view.findViewById(R.id.my_plan_item_create_time);
            tvId = (TextView) view.findViewById(R.id.my_plan_item_order_num);
            tvState = (TextView) view.findViewById(R.id.my_plan_item_state);
        }
    }
}
