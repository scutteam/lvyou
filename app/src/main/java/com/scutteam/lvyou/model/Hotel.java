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
@Table(name = "Hotel")
public class Hotel extends Model implements Serializable {

    private static final long serialVersionUID = -7060210544600464481L;

    @Column(name = "hotel_id")
    public Long hotel_id;
    
    @Column(name = "config")
    public String config;

    @Column(name = "label")
    public String label;

    @Column(name = "level_name")
    public String level_name;

    @Column(name = "score")
    public Double score;

    @Column(name = "is_select")
    public int is_select;
    
    @Column(name = "friday_price")
    public int friday_price;
    
    @Column(name = "pic")
    public String pic;
            
    @Column(name = "price")
    public int price;
    
    @Column(name = "saturday_price")
    public int saturday_price;

    @Column(name = "tip")
    public String tip;

    @Column(name = "intro")
    public String intro;
    

    public static ArrayList<Hotel> insertWithArray(JSONArray dataArray) {
        try {
            ArrayList<Hotel>hotelList = new ArrayList<Hotel>();

            for(int i = 0; i < dataArray.length(); i++) {
                Hotel hotel = insertOrReplace(dataArray.getJSONObject(i));
                hotelList.add(hotel);
            }

            return hotelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Hotel insertOrReplace(JSONObject dataObject) {
        try {
            Long hotel_id = 0L;
            if(dataObject.has("id")) {
                hotel_id = dataObject.getLong("id");
            }
            Hotel hotel = new Hotel();
            hotel.hotel_id = hotel_id;

            if(dataObject.has("config")) {
                hotel.config = dataObject.optString("config");
            }
            if(dataObject.has("label")) {
                hotel.label = dataObject.optString("label");
            }
            if(dataObject.has("levelName")) {
                hotel.level_name = dataObject.optString("levelName");
            }
            if(dataObject.has("score")) {
                hotel.score = dataObject.optDouble("score");
            }
            if(dataObject.has("friday_price")) {
                hotel.friday_price = dataObject.optInt("friday_price");
            }
            if(dataObject.has("pic")) {
                hotel.pic = Constants.IMAGE_URL + dataObject.optString("pic");
            }
            if(dataObject.has("price")) {
                hotel.price = dataObject.optInt("price");
            }
            if(dataObject.has("saturday_price")) {
                hotel.saturday_price = dataObject.optInt("saturday_price");
            }
            if(dataObject.has("tip")) {
                hotel.tip = dataObject.optString("tip");
            }
            if(dataObject.has("intro")) {
                hotel.intro = dataObject.optString("intro");
            }
            
            hotel.is_select = 0;
            return hotel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    

}
