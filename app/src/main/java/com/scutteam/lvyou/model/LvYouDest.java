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
 * Created by admin on 15/7/24.
 */
@Table(name = "LvYouDest")
public class LvYouDest extends Model {

    @Column(name = "cover_pic")
    public String cover_pic;

    @Column(name = "dest_id")
    public Long dest_id;

    @Column(name = "is_hot")
    public Boolean is_hot;

    @Column(name = "label")
    public String label;

    @Column(name = "local")
    public String local;

    @Column(name = "score")
    public double score;

    @Column(name = "short_intro")
    public String short_intro;

    @Column(name = "title")
    public String title;
    
    public static List<LvYouDest>insertWithArray(JSONArray dataArray) {
        try {
            List<LvYouDest>destList = new ArrayList<LvYouDest>();

            for(int i = 0; i < dataArray.length(); i++) {
                LvYouDest dest = insertOrReplace(dataArray.getJSONObject(i));
                destList.add(dest);
            }     
            
            return destList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static LvYouDest insertOrReplace(JSONObject dataObject) {
        try {
            Long dest_id = 0L;
            if(dataObject.has("id")) {
                dest_id = dataObject.optLong("id");
            }
            LvYouDest dest = findDestById(dest_id);
            if(dest == null) {
                dest = new LvYouDest();
            }
            dest.dest_id = dest_id;
            
            if(dataObject.has("coverPic")) {
                dest.cover_pic = Constants.IMAGE_URL + dataObject.optString("coverPic");
            }
            if(dataObject.has("isHot")) {
                dest.is_hot = dataObject.optBoolean("isHot");
            }
            if(dataObject.has("label")) {
                dest.label = dataObject.optString("label");
            }
            if(dataObject.has("local")) {
                dest.local = dataObject.optString("local");
            }
            if(dataObject.has("score")) {
                dest.score = dataObject.optDouble("score");
            }
            if(dataObject.has("shortIntro")) {
                dest.short_intro = dataObject.optString("shortIntro");
            }
            if(dataObject.has("title")) {
                dest.title = dataObject.optString("title");
            }
            
            dest.save();
            return dest;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static LvYouDest findDestById(Long id) {
        return new Select().from(LvYouDest.class).where("dest_id = ?",id).executeSingle();
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public Long getDest_id() {
        return dest_id;
    }

    public void setDest_id(Long dest_id) {
        this.dest_id = dest_id;
    }

    public Boolean getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(Boolean is_hot) {
        this.is_hot = is_hot;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
