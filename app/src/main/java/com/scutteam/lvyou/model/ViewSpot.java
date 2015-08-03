package com.scutteam.lvyou.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.scutteam.lvyou.constant.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 15/7/31.
 */
@Table(name = "ViewSpot")
public class ViewSpot extends Model implements Serializable {

    private static final long serialVersionUID = -7060210544600464482L;
    
    @Column(name = "cover_pic")
    public String cover_pic;

    @Column(name = "view_spot_id")
    public Long view_spot_id;

    @Column(name = "is_hot")
    public Boolean is_hot;

    @Column(name = "price")
    public int price;

    @Column(name = "score")
    public Double score;

    @Column(name = "title")
    public String title;

    @Column(name = "is_select")
    public int is_select;

    public static ArrayList<ViewSpot> insertWithArray(JSONArray dataArray) {
        try {
            ArrayList<ViewSpot>viewSpotList = new ArrayList<ViewSpot>();

            for(int i = 0; i < dataArray.length(); i++) {
                ViewSpot viewSpot = insertOrReplace(dataArray.getJSONObject(i));
                viewSpotList.add(viewSpot);
            }

            return viewSpotList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ViewSpot insertOrReplace(JSONObject dataObject) {
        try {
            Long view_spot_id = 0L;
            if(dataObject.has("id")) {
                view_spot_id = dataObject.getLong("id");
            }
            ViewSpot viewSpot = new ViewSpot();
            viewSpot.view_spot_id = view_spot_id;

            if(dataObject.has("coverPic")) {
                viewSpot.cover_pic = Constants.IMAGE_URL + dataObject.optString("coverPic");
            }
            if(dataObject.has("isHot")) {
                viewSpot.is_hot = dataObject.optBoolean("isHot");
            }
            if(dataObject.has("price")) {
                viewSpot.price = dataObject.optInt("price");
            }
            if(dataObject.has("score")) {
                viewSpot.score = dataObject.optDouble("score");
            }
            if(dataObject.has("title")) {
                viewSpot.title = dataObject.optString("title");
            }
            
            viewSpot.is_select = 0;

            return viewSpot;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
