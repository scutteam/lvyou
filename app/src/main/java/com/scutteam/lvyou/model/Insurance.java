package com.scutteam.lvyou.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 15/7/31.
 */
@Table (name = "Insurance")
public class Insurance extends Model {
    
    @Column(name = "company")
    public String company;
    
    @Column(name = "create_time")
    public String create_time;
    
    @Column(name = "insurance_id")
    public Long insurance_id;
    
    @Column(name = "insurance_type")
    public String insurance_type;
    
    @Column(name = "is_delete")
    public Boolean is_delete;
    
    @Column(name = "persistent")
    public Boolean persistent;
    
    @Column(name = "price")
    public int price;
    
    @Column(name = "sum_insured")
    public String sum_insured;
    
    @Column(name = "update_opid")
    public Long update_opid;
    
    @Column(name = "update_time")
    public String update_time;

    public static List<Insurance> insertWithArray(JSONArray dataArray) {
        try {
            List<Insurance>insuranceList = new ArrayList<Insurance>();

            for(int i = 0; i < dataArray.length(); i++) {
                Insurance insurance = insertOrReplace(dataArray.getJSONObject(i));
                insuranceList.add(insurance);
            }

            return insuranceList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Insurance insertOrReplace(JSONObject dataObject) {
        try {
            Long insurance_id = 0L;
            if(dataObject.has("id")) {
                insurance_id = dataObject.getLong("id");
            }
            Insurance insurance = new Insurance();
            insurance.insurance_id = insurance_id;

            if(dataObject.has("company")) {
                insurance.company = dataObject.optString("company");
            }
            if(dataObject.has("createTime")) {
                insurance.create_time = dataObject.optString("createTime");
            }
            if(dataObject.has("insuranceType")) {
                insurance.insurance_type = dataObject.optString("insuranceType");
            }
            if(dataObject.has("isDeleted")) {
                insurance.is_delete = dataObject.optBoolean("isDeleted");
            }
            if(dataObject.has("persistent")) {
                insurance.persistent = dataObject.optBoolean("persistent");
            }
            if(dataObject.has("price")) {
                insurance.price = dataObject.optInt("price");
            }
            if(dataObject.has("sumInsured")) {
                insurance.sum_insured = dataObject.optString("sumInsured");
            }
            if(dataObject.has("updateOpid")) {
                insurance.update_opid = dataObject.optLong("updateOpid");
            }
            if(dataObject.has("updateTime")) {
                insurance.update_time = dataObject.optString("updateTime");
            }

            return insurance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    
}
