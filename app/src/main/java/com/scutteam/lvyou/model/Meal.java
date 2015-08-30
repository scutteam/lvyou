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
@Table(name = "Meal")
public class Meal extends Model {
    
    @Column(name = "meal_id")
    public Long meal_id;

    @Column(name = "intro")
    public String intro;

    @Column(name = "meal_type")
    public String meal_type;

    @Column(name = "price")
    public int price;

    public static List<Meal> insertWithArray(JSONArray dataArray) {
        try {
            List<Meal>mealList = new ArrayList<Meal>();

            for(int i = 0; i < dataArray.length(); i++) {
                Meal meal = insertOrReplace(dataArray.getJSONObject(i));
                mealList.add(meal);
            }

            return mealList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Meal insertOrReplace(JSONObject dataObject) {
        try {
            Long meal_id = 0L;
            if(dataObject.has("id")) {
                meal_id = dataObject.getLong("id");
            }
            Meal meal = new Meal();
            meal.meal_id = meal_id;

            if(dataObject.has("intro")) {
                meal.intro = dataObject.optString("intro");
            }
            if(dataObject.has("mealType")) {
                meal.meal_type = dataObject.optString("mealType");
            }
            if(dataObject.has("price")) {
                meal.price = dataObject.optInt("price");
            }

            return meal;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
