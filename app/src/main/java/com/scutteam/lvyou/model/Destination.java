package com.scutteam.lvyou.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.scutteam.lvyou.constant.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 15/7/27.
 */
@Table( name = "Destination")
public class Destination extends Model {

    @Column(name = "address")
    public String address;

    @Column(name = "cover_pic")
    public String cover_pic;

    @Column(name = "destination_id")
    public Long destination_id;

    @Column(name = "is_hot")
    public Boolean is_hot;

    @Column(name = "label")
    public String label;

    @Column(name = "limit_num")
    public int limit_num;

    @Column(name = "local")
    public String local;

    @Column(name = "max_num")
    public int max_num;

    @Column(name = "min_num")
    public int min_num;

    @Column(name = "score")
    public Double score;

    @Column(name = "short_intro")
    public String short_intro;

    @Column(name = "title")
    public String title;

    public static List<Destination> insertWithArray(JSONArray dataArray) {
        try {
            List<Destination>destinationList = new ArrayList<Destination>();

            for(int i = 0; i < dataArray.length(); i++) {
                Destination destination = insertOrReplace(dataArray.getJSONObject(i));
                destinationList.add(destination);
            }

            return destinationList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Destination insertOrReplace(JSONObject dataObject) {
        try {
            Long destination_id = 0L;
            if(dataObject.has("id")) {
                destination_id = dataObject.getLong("id");
            }
            Destination destination = findDestinationById(destination_id);
            if(destination == null) {
                destination = new Destination();
            }
            destination.destination_id = destination_id;

            if(dataObject.has("coverPic")) {
                destination.cover_pic = Constants.IMAGE_URL + dataObject.getString("coverPic");
            }
            if(dataObject.has("isHot")) {
                destination.is_hot = dataObject.getBoolean("isHot");
            }
            if(dataObject.has("label")) {
                destination.label = dataObject.getString("label");
            }
            if(dataObject.has("local")) {
                destination.local = dataObject.getString("local");
            }
            if(dataObject.has("score")) {
                destination.score = dataObject.getDouble("score");
            }
            if(dataObject.has("shortIntro")) {
                destination.short_intro = dataObject.getString("shortIntro");
            }
            if(dataObject.has("title")) {
                destination.title = dataObject.getString("title");
            }
            if(dataObject.has("limitNum")) {
                destination.limit_num = dataObject.getInt("limitNum");
            }
            if(dataObject.has("maxNum")) {
                destination.max_num = dataObject.getInt("maxNum");
            }
            if(dataObject.has("minNum")) {
                destination.min_num = dataObject.getInt("minNum");
            }
            if(dataObject.has("address")) {
                destination.address = dataObject.getString("address");
            }

            destination.save();
            return destination;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Destination findDestinationById(Long id) {
        return new Select().from(Destination.class).where("destination_id = ?",id).executeSingle();
    }


}
