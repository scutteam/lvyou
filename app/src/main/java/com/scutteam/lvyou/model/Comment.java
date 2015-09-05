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
 * Created by admin on 15/7/31.
 */
@Table(name = "Comment")
public class Comment extends Model {
    
    @Column(name = "cust")
    public String cust;

    @Column(name = "face")
    public String face;

    @Column(name = "total_score")
    public Double total_score;

    @Column(name = "total_comment")
    public String total_comment;

    @Column(name = "comment_date")
    public String comment_date;

    public static List<Comment> insertWithArray(JSONArray dataArray) {
        try {
            List<Comment>commentList = new ArrayList<Comment>();

            for(int i = 0; i < dataArray.length(); i++) {
                Comment comment = insertOrReplace(dataArray.getJSONObject(i));
                commentList.add(comment);
            }

            return commentList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Comment insertOrReplace(JSONObject dataObject) {
        try {
            Comment comment = new Comment();

            if(dataObject.has("cust")) {
                comment.cust = dataObject.optString("cust");
            }
            if(dataObject.has("face")) {
                comment.face = Constants.IMAGE_URL + dataObject.optString("face");
            }
            if(dataObject.has("totalScore")) {
                comment.total_score = dataObject.optDouble("totalScore");
            }
            if(dataObject.has("totalComment")) {
                comment.total_comment = dataObject.optString("totalComment");
            }
            if(dataObject.has("commentDate")) {
                comment.comment_date = dataObject.optString("commentDate");
            }
            
            return comment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
