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
@Table(name = "LvYouTheme")
public class LvYouTheme extends Model {
    @Column(name = "cover_pic")
    public String cover_pic;

    @Column(name = "theme_id")
    public Long theme_id;

    @Column(name = "is_hot")
    public Boolean is_hot;
    
    @Column(name = "shortIntro")
    public String shortIntro;

    @Column(name = "title")
    public String title;
    
    @Column(name = "dest_num")
    public int dest_num;
    
    @Column(name = "is_select")
    public Boolean is_select;
    
    public static List<LvYouTheme> insertWithArray(JSONArray dataArray) {
        try {
            List<LvYouTheme>themeList = new ArrayList<LvYouTheme>();

            for(int i = 0; i < dataArray.length(); i++) {
                LvYouTheme dest = insertOrReplace(dataArray.getJSONObject(i));
                themeList.add(dest);
            }

            return themeList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LvYouTheme insertOrReplace(JSONObject dataObject) {
        try {
            Long theme_id = 0L;
            if(dataObject.has("id")) {
                theme_id = dataObject.getLong("id");
            }
            LvYouTheme theme = findThemeById(theme_id);
            if(theme == null) {
                theme = new LvYouTheme();
            }
            
            theme.theme_id = theme_id;
            
            if(dataObject.has("coverPic")) {
                theme.cover_pic = Constants.IMAGE_URL + dataObject.getString("coverPic");
            }
            if(dataObject.has("isHot")) {
                theme.is_hot = dataObject.getBoolean("isHot");
            }
            if(dataObject.has("shortIntro")) {
                theme.shortIntro = dataObject.getString("shortIntro");
            }
            if(dataObject.has("title")) {
                theme.title = dataObject.getString("title");
            }
            if(dataObject.has("dest_num")) {
                theme.dest_num = dataObject.getInt("dest_num");
            }
                
            theme.is_select = false;

            theme.save();
            return theme;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LvYouTheme findThemeById(Long id) {
        return new Select().from(LvYouTheme.class).where("theme_id = ?",id).executeSingle();
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public Long getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(Long theme_id) {
        this.theme_id = theme_id;
    }

    public Boolean getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(Boolean is_hot) {
        this.is_hot = is_hot;
    }

    public String getShortIntro() {
        return shortIntro;
    }

    public void setShortIntro(String shortIntro) {
        this.shortIntro = shortIntro;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
