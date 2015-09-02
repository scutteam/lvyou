package com.scutteam.lvyou.model;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liujie on 15/8/22.
 */
public class PlanDetail implements Serializable {

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
    public long destination_logic_id; //目的地逻辑id
    public String status_tips;          //状态提示
    public String hotel_name;           //酒店名
    public String guide;            //导游
    public String vehicle;          //交通工具
    public String meal;             //包餐
    public int play_item_num;       //游玩项目数
    public String head_image;       //上面的图片


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

    public static PlanDetail fromJson(JSONObject jsonObject) {
        PlanDetail plan = new PlanDetail();
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
        plan.destination_logic_id = jsonObject.optLong("destId");
        plan.status_tips = jsonObject.optString("statusTip");
        plan.hotel_name = jsonObject.optString("hotel");
        plan.guide = jsonObject.optString("guide");
        plan.vehicle = jsonObject.optString("vehicle");
        plan.meal = jsonObject.optString("meal");
        plan.play_item_num = jsonObject.optInt("viewspotNum");
        plan.head_image = jsonObject.optString("topPic");
        return plan;
    }

    private static String getFormatDateFromLong(Long longTime) {
        Date date = new Date(longTime);
        return (date.getYear() + 1900) + "年" + (date.getMonth() + 1) + "月" + date.getDate() + "日";
    }
}
