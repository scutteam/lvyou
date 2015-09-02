package com.scutteam.lvyou.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.scutteam.lvyou.constant.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 15/7/27.
 */
@Table(name = "Advert")
public class Advert extends Model{

    @Column(name = "advert_id")
    public Long advert_id;
    
    @Column(name = "title")
    public String title;
    
    @Column(name = "pic")
    public String pic;
    
    @Column(name = "url")
    public String url;
    
    @Column(name = "intro")
    public String intro;
    
    public static List<Advert> insertWithArray(JSONArray dataArray) {
        try {
            List<Advert>advertList = new ArrayList<Advert>();

            for(int i = 0; i < dataArray.length(); i++) {
                Advert advert = insertOrReplace(dataArray.getJSONObject(i));
                advertList.add(advert);
            }

            return advertList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Advert insertOrReplace(JSONObject dataObject) {
        try {
            Long advert_id = 0L;
            if(dataObject.has("id")) {
                advert_id = dataObject.getLong("id");
            }
            Advert advert = new Advert();
            advert.advert_id = advert_id;
            if(dataObject.has("title")) {
                advert.title = dataObject.getString("title");
            }
            if(dataObject.has("pic")) {
                advert.pic = Constants.IMAGE_URL + dataObject.getString("pic");
            }
            if(dataObject.has("url")) {
                advert.url = dataObject.getString("url");
            }
            if(dataObject.has("intro")) {
                advert.intro = dataObject.getString("intro");
            }
            advert.save();
            return advert;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
