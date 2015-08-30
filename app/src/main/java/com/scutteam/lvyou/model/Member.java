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
 * 用在保险获取那里
 */
public class Member{

    public String avatar;           //行程标题
    public String name;       //订单号

    public static ArrayList<Member>  fromJson(JSONArray response){
        ArrayList<Member> members = new ArrayList<>();
        try {
            for(int i = 0; i < response.length(); i++){
                JSONObject jsonObject = response.getJSONObject(i);
                Member member = new Member();
                member.avatar = jsonObject.optString("face");
                member.name = jsonObject.optString("name");
                members.add(member);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return members;
    }
}
