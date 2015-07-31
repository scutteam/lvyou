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
@Table(name = "Hotel")
public class Hotel extends Model {

    @Column(name = "config")
    public String config;

    @Column(name = "hotel_id")
    public Long hotel_id;

    @Column(name = "label")
    public String label;

    @Column(name = "level_name")
    public String level_name;

    @Column(name = "score")
    public double score;

    public static List<Hotel> insertWithArray(JSONArray dataArray) {
        try {
            List<Hotel>hotelList = new ArrayList<Hotel>();

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
                hotel.config = dataObject.getString("config");
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
            
            return hotel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
