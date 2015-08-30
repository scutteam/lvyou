package com.scutteam.lvyou.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 15/8/19.
 */
@Table(name = "Recommendtrip")
public class Recommendtrip extends Model implements Serializable {
    private static final long serialVersionUID = -7060210544600464488L;
    
    @Column(name = "trip_id")
    public long trip_id;

    @Column(name = "day_num")
    public int day_num;

    @Column(name = "dest")
    public String dest;

    @Column(name = "play_num")
    public int play_num;

    @Column(name = "remark") 
    public String remark;

    @Column(name = "trip_title")
    public String trip_title;

    public static List<Recommendtrip> insertWithArray(JSONArray dataArray) {
        try {
            List<Recommendtrip>recommendtripList = new ArrayList<Recommendtrip>();

            for(int i = 0; i < dataArray.length(); i++) {
                Recommendtrip recommendtrip = insertOrReplace(dataArray.getJSONObject(i));
                recommendtripList.add(recommendtrip);
            }

            return recommendtripList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Recommendtrip insertOrReplace(JSONObject dataObject) {
        try {
            Long trip_id = 0L;
            if(dataObject.has("id")) {
                trip_id = dataObject.getLong("id");
            }
            Recommendtrip trip = new Recommendtrip();
            trip.trip_id = trip_id;

            if(dataObject.has("dayNum")) {
                trip.day_num = dataObject.optInt("dayNum");
            }
            if(dataObject.has("dest")) {
                trip.dest = dataObject.optString("dest");
            }
            if(dataObject.has("playNum")) {
                trip.play_num = dataObject.optInt("playNum");
            }
            if(dataObject.has("remark")) {
                trip.remark = dataObject.optString("remark");
            }
            if(dataObject.has("tripTitle")) {
                trip.trip_title = dataObject.optString("tripTitle");
            }

            return trip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Recommendtrip{" +
                "trip_id=" + trip_id +
                ", day_num=" + day_num +
                ", dest='" + dest + '\'' +
                ", play_num=" + play_num +
                ", remark='" + remark + '\'' +
                ", trip_title='" + trip_title + '\'' +
                '}';
    }
}
