package com.scutteam.lvyou.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liujie on 15/8/22.
 */
public class Plan implements Serializable{

    public long id;                //行程对应逻辑id
    public String title;           //行程标题
    public String order_num;       //订单号
    public String place;           //大学校名
    public String area;            //校区
    public int member_num;         //成员人数
    public String start_date;      //出发日期
    public String end_date;        //返程日期
    public String create_time;     //创建订单时间
    public double unit_price;      //个人单价
    public int state;              //状态
    public String state_text;      //状态对应的文字

    /**
     * 1：计划已保存（等待提交需求订单）
     * 2：订单已提交（等待规划师制定行程方案）
     * 3：规划师已做好方案（等待确认）
     * 4：已确认行程方案（等待签约）
     * 5：已完成签约（请尽快获取保险）
     * 6：愉快出发（出游中）
     * 7：行程已结束（可以写点评了）
     * 0：订单已被中止
     */
    public static final int STATE_SAVE = 1;
    public static final int STATE_HAS_SUBMIT = 2;
    public static final int STATE_PLAN_MADE = 3;
    public static final int STATE_SURE = 4;
    public static final int STATE_SIGN = 5;
    public static final int STATE_TRAVELING = 6;
    public static final int STATE_COMPLETE = 7;
    public static final int STATE_STOP = 0;

    public static List<Plan>  fromJson(JSONArray response){
        ArrayList<Plan> plans = new ArrayList<>();
        try {
            for(int i = 0; i < response.length(); i++){
                JSONObject jsonObject = response.getJSONObject(i);
                Plan plan = new Plan();
                plan.id = jsonObject.optLong("id");
                plan.title = jsonObject.optString("title");
                plan.order_num = jsonObject.optString("orderNo");
                plan.place = jsonObject.optString("place");
                plan.area = jsonObject.optString("area");
                plan.member_num = jsonObject.optInt("peopleNum");
                plan.unit_price = jsonObject.optDouble("unitPrice");
                plan.state = jsonObject.optInt("status");
                plan.state_text = jsonObject.optString("statusText");
                plan.start_date = getFormatDateFromLong(jsonObject.optLong("startDate"));
                plan.end_date = getFormatDateFromLong(jsonObject.optLong("endDate"));
                plan.create_time = getFormatDateFromLong(jsonObject.optLong("createTime"));
                plans.add(plan);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return plans;
    }

    private static String getFormatDateFromLong(Long longTime){
        Date date = new Date(longTime);
        return (date.getYear() + 1900) + "年" + (date.getMonth() + 1) + "月" + date.getDate() + "日";
    }
}
