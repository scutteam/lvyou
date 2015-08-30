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
@Table(name = "Guide")
public class Guide extends Model {
    
    @Column(name = "guide_id")
    public Long guide_id;

    @Column(name = "level_name")
    public String level_name;

    @Column(name = "price")
    public int price;

    public static List<Guide> insertWithArray(JSONArray dataArray) {
        try {
            List<Guide>guideList = new ArrayList<Guide>();

            for(int i = 0; i < dataArray.length(); i++) {
                Guide guide = insertOrReplace(dataArray.getJSONObject(i));
                guideList.add(guide);
            }

            return guideList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Guide insertOrReplace(JSONObject dataObject) {
        try {
            Long guide_id = 0L;
            if(dataObject.has("id")) {
                guide_id = dataObject.getLong("id");
            }
            Guide guide = new Guide();
            
            guide.guide_id = guide_id;

            if(dataObject.has("levelName")) {
                guide.level_name = dataObject.getString("levelName");
            } 
            if(dataObject.has("price")) {
                guide.price = dataObject.getInt("price");
            }

            return guide;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
